package it.polimi.ingsw.view.tui;

import java.io.IOException;

import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.controller.server.ClientDescriptor;
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
import it.polimi.ingsw.view.tui.concurrent.TitleScreenThread;
import it.polimi.ingsw.view.tui.utils.ClientSpaceShipFormatter;

public class TUIView implements ClientView {

    private final TerminalWrapper terminal;
    private Thread inputthread;

    public TUIView() throws IOException {
        this.terminal = new TerminalWrapper();
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
        // this.inputthread = new ConnectingThread(this.terminal, state);
        // this.inputthread.start();
        String name = "tiziobagongo2020";

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
        // terminal.print(cl.get(1), 1, 0);
        // terminal.print(cl.get(2), 2, 0);
    }

    @Override
    public void show(ClientLobbySelectState state) {
    }

    @Override
    public void show(ClientSetupState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientConstructionState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientVerifyState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientVoyageState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientEndgameState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }


    @Override
    public void showTextMessage(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showTextMessage'");
    }
    
}
