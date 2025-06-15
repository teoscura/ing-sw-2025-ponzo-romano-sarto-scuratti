package it.polimi.ingsw.model.client.card;

/**
 * Base client side card state, contains info about the card id it represents, and the card class name.
 */
public class ClientBaseCardState implements ClientCardState {

	private final String state;
	private final int id;

	public ClientBaseCardState(String state, int id) {
		if (state == null) throw new NullPointerException();
		if (id < 1 || id > 120 || (id > 20 && id < 100)) throw new IllegalArgumentException();
		this.state = state;
		this.id = id;
	}

	public String getState() {
		return this.state;
	}

	public int getID() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showCardState(ClientCardStateVisitor visitor) {
		visitor.show(this);
	}
}
