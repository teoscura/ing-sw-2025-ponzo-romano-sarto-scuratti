package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.match.PlayerCount;
import it.polimi.ingsw.model.components.enums.*;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.rulesets.GameModeType;

public interface iServerController {
    //Lobby
    // public void getOpenWaitingRooms(ClientDescriptor client);
    // public void openWaitingRoom(ClientDescriptor client, GameModeType type, PlayerCount count);
    // public void quitWaitingRoom(ClientDescriptor client);
    // public void joinWaitingRoom(ClientDescriptor client, long match_id);
    // public void getUnfinishedGames(ClientDescriptor client);
    // public void getMyUnfinishedGames(ClientDescriptor client);
    //Construction
    public void putComponent(ClientDescriptor client, ShipCoords coords, ComponentRotation rotation, int component_id);
    public void takeComponent(ClientDescriptor client);
    public void takeDiscarded(ClientDescriptor client, int index);
    public void discardComponent(ClientDescriptor client, int component_id);
    public void toggleHourglass(ClientDescriptor client);
    //Validation
    public void removeComponent(ClientDescriptor client, ShipCoords coords);
    public void setCrewType(ClientDescriptor client, ShipCoords crew, AlienType type);
    //Voyage
    public void setNewShipCenter(ClientDescriptor client, ShipCoords new_center);
    public void turnOn(ClientDescriptor client, ShipCoords target, ShipCoords source);
    public void removeCrew(ClientDescriptor client, ShipCoords[] coords);
    public void takeCargo(ClientDescriptor client, ShipmentType type, ShipCoords destination);
    //Multiple
    public void askUpdateState(ClientDescriptor client);
    public void progressTurn(ClientDescriptor client);
    public void ping(ClientDescriptor client);
}
