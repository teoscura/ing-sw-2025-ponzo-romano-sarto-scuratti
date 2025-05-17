package it.polimi.ingsw.model.client.components;

public class ClientEmptyComponent implements ClientComponent {

    @Override
	public void showComponent(ClientComponentVisitor visitor) {
		visitor.show(this);
	}

}
