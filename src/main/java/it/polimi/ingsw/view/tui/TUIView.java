package it.polimi.ingsw.view.tui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jline.utils.AttributedString;
import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.tui.concurrent.*;
import it.polimi.ingsw.view.tui.utils.*;

public class TUIView implements ClientView {

    private final TerminalWrapper terminal;
    private final Object input_lock;
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
        terminal.puts(Capability.clear_screen);
        if(state.getLobbyList().size()==0) terminal.printCentered(List.of("No lobbies open yet!"));
        else terminal.print(ClientLobbyStatesFormatter.format(state), 0, 0);
        terminal.updateStatusTopLines(new ArrayList<AttributedString>());
        terminal.updateStatus();
    }

    @Override
    public void show(ClientSetupState state) {
        if(state == null) throw new UnsupportedOperationException();
        terminal.puts(Capability.clear_screen);
        if(state.getUnfinishedList().size()==0) terminal.printCentered(List.of("No unfinished lobbies available!"));
        else terminal.print(ClientLobbyStatesFormatter.format(state), 0, 0);
        terminal.updateStatusTopLines(new ArrayList<AttributedString>());
        terminal.updateStatus();
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        if(state == null) throw new UnsupportedOperationException();
    }

    @Override
    public void show(ClientConstructionState state) {
        if(state == null) throw new UnsupportedOperationException();
    }

    @Override
    public void show(ClientVerifyState state) {
        if(state == null) throw new UnsupportedOperationException();
    }

    @Override
    public void show(ClientVoyageState state) {
        if(state == null) throw new UnsupportedOperationException();
    }

    @Override
    public void show(ClientEndgameState state) {
        if(state == null) throw new UnsupportedOperationException();

    }

    @Override
    public void showTextMessage(String message) {
        if(state == null) throw new UnsupportedOperationException();

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
    }
    
    private void testShow(){
        ComponentFactory f1 = new ComponentFactory();
        BaseComponent c;
        Player player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
		c = f1.getComponent(14);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f1.getComponent(18);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f1.getComponent(126);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
		c = f1.getComponent(132);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		c = f1.getComponent(118);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 2));

        String name = "tiziobagongo2020";
        // c.getClientComponent().showComponent(ps);
        // p.setCenter(ps.getComponentStringSmall());
        // var cl = p.getComponentStringsLarge();
        terminal.puts(Capability.clear_screen);
        var a = ClientSpaceShipFormatter.formatLarge(player1.getSpaceShip().getClientSpaceShip(), name, PlayerColor.GREEN, 69, 100).stream().map(b->b.toString()).toList();
        terminal.print(a, 0, 0);
        var c1 = ClientSpaceShipFormatter.formatSmall(player1.getSpaceShip().getClientSpaceShip(), name, PlayerColor.GREEN, 69, 100).stream().map(b->b.toString()).toList();
        terminal.print(c1, 0, 46);
        terminal.print(c1, 8, 46);
        terminal.print(c1, 16, 46);

        var tmp = List.of(PlayerColor.YELLOW);
        ClientCardState cs = new ClientNewCenterCardStateDecorator(new ClientBaseCardState("WOW",1), new ArrayList<>(tmp));
        ClientCardStateFormatter cvsf = new ClientCardStateFormatter();
        cs.showCardState(cvsf);
        terminal.print(cvsf.getFormatted().toAnsi(), 31, 0);
        // terminal.print(cl.get(1), 1, 0);
        // terminal.print(cl.get(2), 2, 0);
    }

    

}
