package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.model.components.enums.AlienType;

/**
 * Client side component decorator containing info about the crew inside it.
 */
public class ClientCabinComponentDecorator implements ClientComponent {

	private final ClientComponent base;
	private final AlienType type;
	private final int crew;
	private final boolean starting;

	public ClientCabinComponentDecorator(ClientComponent base, AlienType type, int crew, boolean starting) {
		if (base == null || (!type.getLifeSupportExists() && type.getArraypos() != 0) || crew < 0 || crew > type.getMaxCapacity())
			throw new NullPointerException();
		this.base = base;
		this.type = type;
		this.crew = crew;
		this.starting = starting;
	}

	public ClientComponent getBase() {
		return this.base;
	}

	public AlienType getAlienType() {
		return this.type;
	}

	public int getCrew() {
		return this.crew;
	}

	public boolean getStarting() {
		return this.starting;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showComponent(ClientComponentVisitor visitor) {
		base.showComponent(visitor);
		visitor.show(this);
	}
}
