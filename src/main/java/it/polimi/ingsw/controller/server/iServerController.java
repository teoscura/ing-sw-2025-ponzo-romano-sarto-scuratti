package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;

public interface iServerController {

    //ServerController ones.
    public void connect(ClientDescriptor client);
    public void disconnect(ClientDescriptor client);
    public void openRoom(ClientDescriptor client, GameModeType type, PlayerCount count);
    public void getUnfinishedList(ClientDescriptor client);
    public void getMyUnfinishedList(ClientDescriptor client);
    public void openUnfinished(ClientDescriptor client, int id);
    public void ping(ClientDescriptor client);
    
    //ModelInstance


    //GameState
    // public void sendContinue(ClientDescriptor client);
    // public void connect(ClientDescriptor client);
    // public void disconnect(ClientDescriptor client);

    //WaitingRoom
    // public void connect(ClientDescriptor client);
    // public void disconnect(ClientDescriptor client);

    // //Construction
    // public void putComponent(ClientDescriptor client, ShipCoords coords, ComponentRotation rotation, int id);
    // public void takeComponent(ClientDescriptor client);
    // public void takeDiscarded(ClientDescriptor client, int id);
    // public void discardComponent(ClientDescriptor client, int component_id);
    // public void toggleHourglass(ClientDescriptor client);

    // //Validation
    // public void removeComponent(ClientDescriptor client, ShipCoords coords);
    // public void setCrewType(ClientDescriptor client, ShipCoords coords, AlienType type);

    // //Voyage
    // public void setNewShipCenter(ClientDescriptor client, ShipCoords new_center);
    // public void turnOn(ClientDescriptor client, ShipCoords target_coords, ShipCoords battery_coords);
    // public void removeCrew(ClientDescriptor client, ShipCoords coords);
    // public void removeCargo(ClientDescriptor client, ShipmentType type, ShipCoords coords);
    // public void takeCargo(ClientDescriptor client, ShipmentType type, ShipCoords coords);
    // public void selectLanding(ClientDescriptor client, int planet);
    // public void askUpdateState(ClientDescriptor client);
    // public void progressTurn(ClientDescriptor client);

}
