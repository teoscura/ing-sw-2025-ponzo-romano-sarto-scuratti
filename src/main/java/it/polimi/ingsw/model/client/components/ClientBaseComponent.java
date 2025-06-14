package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;

/**
 * Client side component containing the info about its id and rotation.
 */
public class ClientBaseComponent implements ClientComponent {

	private final int id;
	private final ComponentRotation rotation;
	private final ConnectorType[] connectors;

	public ClientBaseComponent(int id, ComponentRotation rotation, ConnectorType[] connectors) {
		if (connectors == null) throw new NullPointerException();
		if (id < 1 || id > 157 || connectors.length != 4) throw new IllegalArgumentException();
		this.id = id;
		this.rotation = rotation;
		this.connectors = connectors;
	}

	public int getId() {
		return this.id;
	}

	public ComponentRotation getRotation() {
		return this.rotation;
	}

	public ConnectorType getConnector(ComponentRotation direction) {
		int shift = direction.getShift() + (4 - this.rotation.getShift());
		shift = shift % 4;
		return connectors[shift];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showComponent(ClientComponentVisitor visitor) {
		visitor.show(this);
	}

}
