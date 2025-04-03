package it.polimi.ingsw.model.client;

import it.polimi.ingsw.model.components.enums.ComponentRotation;

public class ClientComponent {
    private final int id;
    private final ComponentRotation rotation;

    public ClientComponent(int id, ComponentRotation rotation){
        if(id<1||id>157) throw new IllegalArgumentException();
        this.id = id;
        this.rotation = rotation;
    }

    public int getId(){
        return this.id;
    }

    public ComponentRotation getRotation(){
        return this.rotation;
    }
}
