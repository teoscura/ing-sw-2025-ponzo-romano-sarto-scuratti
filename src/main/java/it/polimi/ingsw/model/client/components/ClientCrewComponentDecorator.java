package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.view.ClientView;

public class ClientCrewComponentDecorator implements ClientComponent {

	private final ClientComponent base;
	private final AlienType type;
	private final int crew;

	public ClientCrewComponentDecorator(ClientComponent base, AlienType type, int crew) {
		if (base == null || (!type.getLifeSupportExists() && type.getArraypos() != 0) || crew < 0 || crew > type.getMaxCapacity())
			throw new NullPointerException();
		this.base = base;
		this.type = type;
		this.crew = crew;
	}

	public AlienType getAlienType() {
		return this.type;
	}

	public int getCrew() {
		return this.crew;
	}

	@Override
	public void showComponent(ClientView view) {
		base.showComponent(view);
		view.show(this);
	}
}
