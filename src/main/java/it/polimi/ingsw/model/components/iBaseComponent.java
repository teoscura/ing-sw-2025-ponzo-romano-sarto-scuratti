//Done.
package it.polimi.ingsw.model.components;

import java.io.Serializable;

import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;


public interface iBaseComponent extends Serializable {

	//Connectors are stored in this order: UP, RIGHT, DOWN, LEFT;

	int getID();

	boolean verify(SpaceShip ship);

	void check(iVisitor v);

	//On delete/Creation
	void onCreation(SpaceShip ship, ShipCoords coords);

	void onDelete(SpaceShip ship);

	//Return the connector pointing up.
	void rotate(ComponentRotation rotation);

	ShipCoords getCoords();

	ConnectorType getConnector(ComponentRotation direction);

	ConnectorType[] getConnectors();

	ComponentRotation getRotation();

	iBaseComponent[] getConnectedComponents(SpaceShip ship);

	boolean powerable();

	ClientComponent getClientComponent();
}