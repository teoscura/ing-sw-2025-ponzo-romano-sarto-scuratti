package it.polimi.ingsw.controller;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.DummyView;

public class ClientControllerTest {
    
    private ClientController controller;
    private ClientView view; 

    @BeforeEach
    void setup(){
        view = new DummyView();
        controller = new ClientController(view);
        controller.setState(new TitleScreenState(controller, view));      
    }

    @Test
    void invalidUsernameTest(){
        this.controller.setState(new TitleScreenState(controller, view));
        this.controller.getState().setUsername("???invalid");
        assertInstanceOf(TitleScreenState.class, controller.getState());
        this.controller.getState().setUsername("bingus");
        assertInstanceOf(ConnectingState.class, controller.getState());
    }

}
