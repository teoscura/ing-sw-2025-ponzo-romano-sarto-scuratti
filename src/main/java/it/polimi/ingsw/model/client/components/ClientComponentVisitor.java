package it.polimi.ingsw.model.client.components;

public interface ClientComponentVisitor {
    
	void show(ClientBaseComponent component);

    void show(ClientEmptyComponent component);

    void show(ClientBatteryComponentDecorator component);

    void show(ClientBrokenVerifyComponentDecorator component);

    void show(ClientCabinComponentDecorator component);

    void show(ClientCannonComponentDecorator component);

    void show(ClientEngineComponentDecorator component);

    void show(ClientLifeSupportComponentDecorator component);

	void show(ClientPoweredComponentDecorator component);

    void show(ClientShieldComponentDecorator component);

	void show(ClientShipmentsComponentDecorator component);

}
