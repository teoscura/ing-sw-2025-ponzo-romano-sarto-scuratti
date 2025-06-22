package it.polimi.ingsw.model.client.components;

public class DummyComponentVisitor implements ClientComponentVisitor {

    private int i = 0;

    public int visited(){
        return i;
    }

    @Override
    public void show(ClientBaseComponent component) {
        i++;
    }

    @Override
    public void show(ClientEmptyComponent component) {
        i++;
    }

    @Override
    public void show(ClientBatteryComponentDecorator component) {
        i++;
    }

    @Override
    public void show(ClientBrokenVerifyComponentDecorator component) {
        i++;
    }

    @Override
    public void show(ClientCabinComponentDecorator component) {
        i++;
    }

    @Override
    public void show(ClientCannonComponentDecorator component) {
        i++;
    }

    @Override
    public void show(ClientEngineComponentDecorator component) {
        i++;
    }

    @Override
    public void show(ClientLifeSupportComponentDecorator component) {
        i++;
    }

    @Override
    public void show(ClientPoweredComponentDecorator component) {
        i++;
    }

    @Override
    public void show(ClientShieldComponentDecorator component) {
        i++;
    }

    @Override
    public void show(ClientShipmentsComponentDecorator component) {
        i++;
    }
    
}
