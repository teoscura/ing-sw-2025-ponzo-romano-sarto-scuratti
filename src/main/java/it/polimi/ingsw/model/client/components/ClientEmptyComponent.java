package it.polimi.ingsw.model.client.components;

/**
 * Empty client side component.
 */
public class ClientEmptyComponent implements ClientComponent {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showComponent(ClientComponentVisitor visitor) {
		visitor.show(this);
	}

}
