package it.polimi.ingsw.view.tui.utils;

import it.polimi.ingsw.model.client.components.*;
import it.polimi.ingsw.model.components.enums.AlienType;

public class ClientSmallComponentPrinter implements ClientComponentVisitor {

    private String character_component = "..";

    public void reset(){
        this.character_component = "..";
    }

    public String getComponentStringSmall(){
        return this.character_component;
    }

    @Override
    public void show(ClientBaseComponent component) {
        this.character_component = "🔗"; 
    }

    @Override
    public void show(ClientEmptyComponent component){
        this.character_component = "..";
    }

    @Override
    public void show(ClientBatteryComponentDecorator component) {
        this.character_component = "🔋";
    }

    @Override
    public void show(ClientBrokenVerifyComponentDecorator component) {
        return;
    }

    @Override
    public void show(ClientCabinComponentDecorator component) {
        this.character_component = component.getStarting() ? "🏦" : "🛖";
    }

    @Override
    public void show(ClientCannonComponentDecorator component) {
        this.character_component = "🔫";
    }

    @Override
    public void show(ClientEngineComponentDecorator component) {
        this.character_component = "🚀";
    }

    @Override
    public void show(ClientLifeSupportComponentDecorator component) {
        this.character_component = component.getAlienType()==AlienType.BROWN ? "🤎" : "💜";
    }

    @Override
    public void show(ClientPoweredComponentDecorator component) {
        return;
    }

    @Override
    public void show(ClientShieldComponentDecorator component) {
        this.character_component = "🛡️";
    }

    @Override
    public void show(ClientShipmentsComponentDecorator component) {
        this.character_component = "🎁";
    }
    
}
