package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.tui.concurrent.*;
import it.polimi.ingsw.view.tui.formatters.*;
import it.polimi.ingsw.view.tui.states.TUIConnectionSetupState;
import it.polimi.ingsw.view.tui.states.TUIInGameState;
import it.polimi.ingsw.view.tui.states.TUIState;
import it.polimi.ingsw.view.tui.states.TUITitleState;

import org.jline.utils.InfoCmp.Capability;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class TUIView implements ClientView {
	
	//Drawing fields.
	private final TerminalWrapper terminal;
	private final ArrayList<TUINotification> notifications;
	private final Thread drawthread;
	private Runnable screen_runnable;
	private Runnable status_runnable;
	private boolean overlay;
	private Runnable overlay_runnable;

	//Controller communication fields.
	private final ThreadSafeMessageQueue<ServerMessage> queue;
	private final Thread inputthread;
	private TUIState tuistate;

	//Game info fields.
	private ClientState client_state;
	private String username;
	private PlayerColor selected_color;

	public TUIView() throws IOException {
		this.terminal = new TerminalWrapper(this);
		this.queue = new ThreadSafeMessageQueue<>(10);
		this.inputthread = new KeyboardInputThread(terminal, this);
		inputthread.start();
		this.screen_runnable = () -> {};
		this.status_runnable = () -> {};
		this.overlay_runnable = () -> {};
		this.notifications = new ArrayList<>();
		this.drawthread = new RedrawThread(this);
		drawthread.start();
	}

	public void redraw() {
		synchronized (notifications) {
			if (TextMessageFormatter.trimExpired(notifications)) terminal.puts(Capability.clear_screen);
		}
		if (username != null) {
			String topline = "You are: " + username;
			terminal.print(topline, 0, (128 - topline.length()) / 2);
		}
		if(!overlay){
			this.screen_runnable.run();
			this.status_runnable.run();
			synchronized (notifications) {
				if (!notifications.isEmpty()) TextMessageFormatter.format(terminal, notifications);
			}
		} else {
			synchronized (notifications) {
				if (!notifications.isEmpty()) TextMessageFormatter.format(terminal, notifications);
			}
			this.overlay_runnable.run();
		}
	}

	@Override
	public void show(TitleScreenState state) {
		this.tuistate = new TUITitleState(this, state);
		this.screen_runnable = () -> this.tuistate.getRunnable(terminal);
	}

	@Override
	public void show(ConnectingState state) {
		this.tuistate = new TUIConnectionSetupState(this, state);
		this.screen_runnable = () -> this.tuistate.getRunnable(terminal);
	}

	@Override
	public void show(ClientLobbySelectState state) {
		this.screen_runnable = () -> ClientLobbyStatesFormatter.format(terminal, state);
		this.status_runnable = () -> ClientLobbyStatesFormatter.formatStatus(terminal, state);
	}

	@Override
	public void show(ClientSetupState state) {
		this.screen_runnable = () -> ClientLobbyStatesFormatter.format(terminal, state);
		this.status_runnable = () -> ClientLobbyStatesFormatter.formatStatus(terminal, state);
	}

	@Override
	public void show(ClientWaitingRoomState state) {
		if (this.selected_color == PlayerColor.NONE)
			this.selected_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);
		this.screen_runnable = () -> ClientWaitingStateFormatter.format(terminal, state);
		this.status_runnable = () -> ClientWaitingStateFormatter.formatStatus(terminal, state);
	}

	@Override
	public void show(ClientConstructionState state) {
		if (this.selected_color == PlayerColor.NONE)
			this.selected_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);
		this.screen_runnable = () -> ClientConstructionStateFormatter.format(terminal, state, selected_color);
		this.status_runnable = () -> ClientConstructionStateFormatter.formatStatus(terminal, state);
	}

	@Override
	public void show(ClientVerifyState state) {
		if (this.selected_color == PlayerColor.NONE)
			this.selected_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);
		this.screen_runnable = () -> ClientVerifyStateFormatter.format(terminal, state, selected_color);
		this.status_runnable = () -> ClientVerifyStateFormatter.formatStatus(terminal, state);
	}

	@Override
	public void show(ClientVoyageState state) {
		if (this.selected_color == PlayerColor.NONE)
			this.selected_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);
		this.screen_runnable = () -> ClientVoyageStateFormatter.format(terminal, state, selected_color);
		this.status_runnable = () -> ClientVoyageStateFormatter.formatStatus(terminal, state);
	}

	@Override
	public void show(ClientEndgameState state) {
		this.selected_color = PlayerColor.NONE;
		this.screen_runnable = () -> ClientEndingStateFormatter.format(terminal, state);
		this.status_runnable = () -> ClientEndingStateFormatter.formatStatus(terminal, state);
	}

	@Override
	public void showTextMessage(String message) {
		synchronized (this.notifications) {
			notifications.add(new TUINotification(message, Instant.now(), Duration.ofSeconds(10)));
		}
	}

	@Override
	public void setClientState(ClientState state) {
		this.client_state = state;
	}

	public void showHelpScreen() {
		this.overlay_runnable = () -> HelpScreenFormatter.format(terminal);
		overlay = true;
	}

	public void showStateInfo() {
		this.overlay_runnable = () -> this.client_state.sendToView(new ClientStateOverlayFormatter(terminal));
		overlay = true;
	}

	public void resetOverlay() {
		terminal.puts(Capability.clear_screen);
		this.overlay_runnable = () -> {};
		overlay = false;
	}

	public Runnable getStatusRunnable() {
		return this.status_runnable;
	}

	public void handleLine(String line) {
		this.tuistate.handleLine(line);
	}

	public void changeShip(String s) {
		switch (s) {
			case "red":
				this.selected_color = PlayerColor.RED;
				break;
			case "blue":
				this.selected_color = PlayerColor.BLUE;
				break;
			case "green":
				this.selected_color = PlayerColor.GREEN;
				break;
			case "yellow":
				this.selected_color = PlayerColor.YELLOW;
				break;
			default:
				this.selected_color = PlayerColor.NONE;
		}
		this.redraw();
	}

	@Override
	public void setInput(ServerMessage input) {
		queue.insert(input);
	}

	@Override
	public ServerMessage takeInput() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			this.showTextMessage("Shutting down input command thread!");
			return null;
		}
	}

	@Override
	public void connect(ConnectedState state) {
		terminal.puts(Capability.clear_screen);
		this.selected_color = PlayerColor.NONE;
		this.username = state.getUsername();
		this.tuistate = new TUIInGameState(this);
	}

	@Override
	public void disconnect() {
		this.status_runnable = () -> {};
		this.overlay_runnable = null;
		this.client_state = null;
	}

}
