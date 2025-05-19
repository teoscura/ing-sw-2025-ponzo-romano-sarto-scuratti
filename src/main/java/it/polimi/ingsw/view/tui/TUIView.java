package it.polimi.ingsw.view.tui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.LevelTwoCardFactory;
import it.polimi.ingsw.model.cards.state.PiratesAnnounceState;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileDimension;
import it.polimi.ingsw.model.cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientMeteoriteCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator;
import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;
import it.polimi.ingsw.model.client.player.ClientVoyagePlayer;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.tui.concurrent.*;
import it.polimi.ingsw.view.tui.formatters.*;

public class TUIView implements ClientView {

    private final TerminalWrapper terminal;
    private final Object input_lock;
    private PlayerColor selected_color;
    private ClientState client_state;
    private ServerMessage input;
    private Thread inputthread;
    private ConnectedState state;

    public TUIView() throws IOException {
        this.terminal = new TerminalWrapper();
        this.input_lock = new Object();
    }

    @Override
    public void show(TitleScreenState state) {
        if(this.inputthread!=null) this.inputthread.interrupt();
        this.inputthread = new TitleScreenThread(this.terminal, state);
        this.inputthread.start();
    }

    @Override
    public void show(ConnectingState state) {
        this.inputthread.interrupt();
        this.inputthread = new ConnectingThread(this.terminal, state);
        this.inputthread.start();
    }

    @Override
    public void show(ClientLobbySelectState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        testShowVerify();
        // this.client_state = state;
        // terminal.puts(Capability.clear_screen);
        // ClientLobbyStatesFormatter.format(terminal, state);
    }

    @Override
    public void show(ClientSetupState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        ClientLobbyStatesFormatter.format(terminal, state);
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        terminal.puts(Capability.clear_screen);
        ClientWaitingStateFormatter.format(terminal, state);
    }

    @Override
    public void show(ClientConstructionState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        ClientConstructionStateFormatter.format(terminal, state, selected_color);
    }

    @Override
    public void show(ClientVerifyState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        ClientVerifyStateFormatter.format(terminal, state, selected_color);
    }

    @Override
    public void show(ClientVoyageState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        ClientVoyageStateFormatter.format(terminal, state, selected_color);
    }

    @Override
    public void show(ClientEndgameState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        ClientEndingStateFormatter.format(terminal, state);
    }

    @Override
    public void showTextMessage(String message) {
        if(state == null) throw new UnsupportedOperationException();
    }

    public ClientState getClientState(){
        return this.client_state;
    }

    public void changeShip(String s){
        switch(s){
            case "red":
                this.selected_color = PlayerColor.RED;
                return;
            case "blue":
                this.selected_color = PlayerColor.BLUE;
                return;
            case "green":
                this.selected_color = PlayerColor.GREEN;
                return;
            case "yellow":
                this.selected_color = PlayerColor.YELLOW;
                return;
            default: return;
        }
    }

    @Override
    public void setInput(ServerMessage input){
        synchronized(this.input_lock){
            this.input = input;
            this.input_lock.notifyAll();
        }
    }

    @Override
    public Object getLock() {
        return this.input_lock;
    }

    @Override
    public boolean inputAvailable() {
        synchronized(this.input_lock){
            return this.input != null;
        }
    }

    @Override
    public ServerMessage getInput() {
        synchronized(this.input_lock){
            ServerMessage res = this.input;
            this.input = null;
            return res;
        }
    }

    @Override
    public void connect(ConnectedState state){
        this.inputthread.interrupt();
        this.inputthread = new ConnectedThread(terminal, this);
        this.inputthread.start();
        this.state = state;
    }

    @Override
    public void disconnect(){
        this.inputthread.interrupt();
        this.state = null;
        this.client_state = null;
    }

    private void testShowWaiting(){
        ArrayList<ClientWaitingPlayer> players = new ArrayList<>();

        players.add(new ClientWaitingPlayer("bingus", PlayerColor.RED));
        players.add(new ClientWaitingPlayer("sbingus", PlayerColor.BLUE));
        players.add(new ClientWaitingPlayer("sbongus", PlayerColor.GREEN));
        ClientWaitingRoomState cs = new ClientWaitingRoomState(GameModeType.LVL2, PlayerCount.FOUR, players);
        ClientWaitingStateFormatter.format(terminal, cs);
    }

    private void testShowVerify(){
        ArrayList<ClientVerifyPlayer> players = new ArrayList<>();

        ComponentFactory f1 = new ComponentFactory();
        ComponentFactory f2 = new ComponentFactory();

        BaseComponent c;

        //player stabile
		Player player1 = new Player(GameModeType.LVL2, "player1", PlayerColor.RED);
		c = f1.getComponent(41);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 1));
		c = f1.getComponent(79);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 3));
		c = f1.getComponent(26);
		c.rotate(ComponentRotation.U180);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 2, 1));
		c = f1.getComponent(25);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 2, 2));

		//player ciambella
		Player player2 = new Player(GameModeType.LVL2, "player2", PlayerColor.BLUE);
		c = f2.getComponent(41);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 1));
		c = f2.getComponent(78);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 3));
		c = f2.getComponent(67);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 4, 1));
		c = f2.getComponent(58);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 1));
		c = f2.getComponent(43);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 2));
		c = f2.getComponent(59);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 3));
		c = f2.getComponent(23);
		c.rotate(ComponentRotation.U090);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 4, 3));

        players.add(new ClientVerifyPlayer(player1.getUsername(), player1.getColor(), player1.getSpaceShip().getClientSpaceShip().getVerifyShip(player1.getSpaceShip().bulkVerify()), true, 0));
        players.add(new ClientVerifyPlayer(player2.getUsername(), player2.getColor(), player2.getSpaceShip().getClientSpaceShip().getVerifyShip(player2.getSpaceShip().bulkVerify()), false, -1));
        
        ClientVerifyState cs = new ClientVerifyState(players);
        ClientVerifyStateFormatter.format(terminal, cs, PlayerColor.GREEN);
    }

    private void testShowVoyage(){
        LevelTwoCardFactory f2 = new LevelTwoCardFactory();
        ArrayList<ClientVoyagePlayer> players = new ArrayList<>();

        BaseComponent c;
        ComponentFactory f =new ComponentFactory();

        Player dummy2 = new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
		c = f.getComponent(126);
		c.rotate(ComponentRotation.U000);
		dummy2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
		c = f.getComponent(5);
		c.rotate(ComponentRotation.U000);
		dummy2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));

		Player dummy3 = new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
		c = f.getComponent(53);
		c.rotate(ComponentRotation.U000);
		dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f.getComponent(38);
		c.rotate(ComponentRotation.U270);
		dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 3));
		c = f.getComponent(36);
		c.rotate(ComponentRotation.U180);
		dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 3));
		c = f.getComponent(137);
		c.rotate(ComponentRotation.U000);
		dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 4));
		c = f.getComponent(49);
		c.rotate(ComponentRotation.U000);
		dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 3));

        players.add(new ClientVoyagePlayer("p1", PlayerColor.RED, dummy3.getSpaceShip().getClientSpaceShip(), 25, 10, false, false));
        players.add(new ClientVoyagePlayer("p2", PlayerColor.BLUE, dummy2.getSpaceShip().getClientSpaceShip(), 26, 10, false, false));
        players.add(new ClientVoyagePlayer("p3", PlayerColor.GREEN, dummy2.getSpaceShip().getClientSpaceShip(), 30, 10, false, false));
        ClientCardState st =  new ClientMeteoriteCardStateDecorator(
				new ClientAwaitConfirmCardStateDecorator(
					new ClientBaseCardState(
						"MeteorAnnounceState",
						12),
					new ArrayList<>(Arrays.asList(new PlayerColor[]{PlayerColor.RED, PlayerColor.GREEN}))),
				new Projectile(ProjectileDirection.U090, ProjectileDimension.BIG, 4));
        ClientVoyageState s = new ClientVoyageState(GameModeType.TEST, players, st);
        ClientVoyageStateFormatter.format(terminal, s, PlayerColor.RED);
    }

    private void testShowEndscreen(){
        ArrayList<ClientEndgamePlayer> players = new ArrayList<>();

        players.add(new ClientEndgamePlayer("bingus", PlayerColor.RED, 11, 12, new int[]{2,1,0,10,0}, 5));
        players.add(new ClientEndgamePlayer("sbingus", PlayerColor.BLUE, 12, 14, new int[]{1,2,1,4,0}, 10));
        players.add(new ClientEndgamePlayer("sbongus", PlayerColor.GREEN, -1, 0,new int[]{0,1,1,2,4}, 4));
        ClientEndgameState cs = new ClientEndgameState(players);
        ClientEndingStateFormatter.format(terminal, cs);
    }

}
