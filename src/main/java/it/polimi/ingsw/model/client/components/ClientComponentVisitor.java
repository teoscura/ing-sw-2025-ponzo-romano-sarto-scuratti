package it.polimi.ingsw.model.client.components;

/**
 * Visitor interface that allows any implementer to distinguish between the various subclasses of {@link it.polimi.ingsw.model.client.components.ClientComponent}.
 */
public interface ClientComponentVisitor {
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientBaseComponent}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientBaseComponent} Component to display.
	 */
	void show(ClientBaseComponent component);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientEmptyComponent}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientEmptyComponent} Component to display.
	 */
	void show(ClientEmptyComponent component);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientBatteryComponentDecorator}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientBatteryComponentDecorator} Component to display.
	 */
	void show(ClientBatteryComponentDecorator component);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientBrokenVerifyComponentDecorator}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientBrokenVerifyComponentDecorator} Component to display.
	 */
	void show(ClientBrokenVerifyComponentDecorator component);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientCabinComponentDecorator}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientCabinComponentDecorator} Component to display.
	 */
	void show(ClientCabinComponentDecorator component);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientCannonComponentDecorator}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientCannonComponentDecorator} Component to display.
	 */
	void show(ClientCannonComponentDecorator component);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientEngineComponentDecorator}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientEngineComponentDecorator} Component to display.
	 */
	void show(ClientEngineComponentDecorator component);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientLifeSupportComponentDecorator}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientLifeSupportComponentDecorator} Component to display.
	 */
	void show(ClientLifeSupportComponentDecorator component);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientPoweredComponentDecorator}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientPoweredComponentDecorator} Component to display.
	 */
	void show(ClientPoweredComponentDecorator component);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientShieldComponentDecorator}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientShieldComponentDecorator} Component to display.
	 */
	void show(ClientShieldComponentDecorator component);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.components.ClientShipmentsComponentDecorator}.
	 * @param component {@link it.polimi.ingsw.model.client.components.ClientShipmentsComponentDecorator} Component to display.
	 */
	void show(ClientShipmentsComponentDecorator component);
	
}
