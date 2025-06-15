//Done.
package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.components.ClientEmptyComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.visitors.ComponentVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

import java.util.Arrays;

public class EmptyComponent extends BaseComponent {

	public EmptyComponent() {
		super(157, new ConnectorType[4], ComponentRotation.U000);
	}

	public EmptyComponent(ShipCoords coords) {
		super(157, new ConnectorType[4], ComponentRotation.U000, coords);
	}

	/**
	 * @return Always returns true.
	 */
	@Override
	public boolean verify(SpaceShip ship) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void check(ComponentVisitor v) {
		v.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnectorType[] getConnectors() {
		ConnectorType[] tmp = new ConnectorType[4];
		Arrays.fill(tmp, ConnectorType.EMPTY);
		return tmp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreation(SpaceShip ship, ShipCoords coords) {
		this.coords = coords;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDelete(SpaceShip ship) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientComponent getClientComponent() {
		return new ClientEmptyComponent();
	}

}
