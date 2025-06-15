package it.polimi.ingsw.model.client.components;

/**
 * Visitor interface that allows any implementer to distinguish between the various subclasses of {@link ClientComponent}.
 */
public interface ClientComponentVisitor {
	/**
	 * Displays info regarding a {@link ClientBaseComponent}.
	 * @param state {@link ClientBaseComponent} Component to display.
	 */
	void show(ClientBaseComponent component);
	/**
	 * Displays info regarding a {@link ClientEmptyComponent}.
	 * @param state {@link ClientEmptyComponent} Component to display.
	 */
	void show(ClientEmptyComponent component);
	/**
	 * Displays info regarding a {@link ClientBatteryComponentDecorator}.
	 * @param state {@link ClientBatteryComponentDecorator} Component to display.
	 */
	void show(ClientBatteryComponentDecorator component);
	/**
	 * Displays info regarding a {@link ClientBrokenVerifyComponentDecorator}.
	 * @param state {@link ClientBrokenVerifyComponentDecorator} Component to display.
	 */
	void show(ClientBrokenVerifyComponentDecorator component);
	/**
	 * Displays info regarding a {@link ClientCabinComponentDecorator}.
	 * @param state {@link ClientCabinComponentDecorator} Component to display.
	 */
	void show(ClientCabinComponentDecorator component);
	/**
	 * Displays info regarding a {@link ClientCannonComponentDecorator}.
	 * @param state {@link ClientCannonComponentDecorator} Component to display.
	 */
	void show(ClientCannonComponentDecorator component);
	/**
	 * Displays info regarding a {@link ClientEngineComponentDecorator}.
	 * @param state {@link ClientEngineComponentDecorator} Component to display.
	 */
	void show(ClientEngineComponentDecorator component);
	/**
	 * Displays info regarding a {@link ClientLifeSupportComponentDecorator}.
	 * @param state {@link ClientLifeSupportComponentDecorator} Component to display.
	 */
	void show(ClientLifeSupportComponentDecorator component);
	/**
	 * Displays info regarding a {@link ClientPoweredComponentDecorator}.
	 * @param state {@link ClientPoweredComponentDecorator} Component to display.
	 */
	void show(ClientPoweredComponentDecorator component);
	/**
	 * Displays info regarding a {@link ClientShieldComponentDecorator}.
	 * @param state {@link ClientShieldComponentDecorator} Component to display.
	 */
	void show(ClientShieldComponentDecorator component);
	/**
	 * Displays info regarding a {@link ClientShipmentsComponentDecorator}.
	 * @param state {@link ClientShipmentsComponentDecorator} Component to display.
	 */
	void show(ClientShipmentsComponentDecorator component);

}
