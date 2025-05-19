package it.polimi.ingsw.gui;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.components.*;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.view.ClientView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.stream.Collectors;

public class GUIView implements ClientView {

    private Stage stage;

    public GUIView(Stage stage) {
        this.stage = stage;
    }

    public void setup() throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/StartingMenuView.fxml")));
        StartingMenuController controller = new StartingMenuController();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    //Game states
    public void show (ClientWaitingRoomState state) {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/WaitingRoomMenuView.fxml")));
        } catch (IOException e) {}

        if (state.getType() == GameModeType.TEST){
            scene.setFill(Color.LIGHTBLUE);

        }
        else scene.setFill(Color.MEDIUMPURPLE);

        stage.setScene(scene);
    }

    public void show(ClientConstructionState state) {};

    public void show(ClientVerifyState state) {};

    public void show(ClientVoyageState state) {};

    public void show(ClientEndgameState state) {};

    //Components
    public void show(ClientBaseComponent component) {};

    public void show(ClientPoweredComponentDecorator component) {};

    public void show(ClientShipmentsComponentDecorator component) {};

    public void show(ClientBatteryComponentDecorator component) {};

    public void show(ClientCrewComponentDecorator component) {};

    public void show(ClientBrokenVerifyComponentDecorator component) {};

    //Card states
    public void show(ClientAwaitConfirmCardStateDecorator state) {};

    public void show(ClientBaseCardState state) {};

    public void show(ClientCargoPenaltyCardStateDecorator state) {};

    public void show(ClientCargoRewardCardStateDecorator state) {};

    public void show(ClientCombatZoneIndexCardStateDecorator state) {};

    public void show(ClientCreditsRewardCardStateDecorator state) {};

    public void show(ClientCrewPenaltyCardStateDecorator state) {};

    public void show(ClientLandingCardStateDecorator state) {};

    public void show(ClientMeteoriteCardStateDecorator state) {};

    public void show(ClientNewCenterCardStateDecorator state) {};

    public void show(ClientProjectileCardStateDecorator state) {};

    //Misc and debug
    public void showTextMessage(String message){

    };
}
