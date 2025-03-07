package it.polimi.ingsw.model.components;

//TODO: refactor enum to use smart java enums.

enum StorageType{
    DOUBLENORMAL,
    TRIPLENORMAL,
    SINGLESPECIAL,
    DOUBLESPECIAL,
}

enum ShipmentType{
    RED,     //4 - Special
    YELLOW,  //3 - Normal
    GREEN,   //2 - Normal
    BLUE,    //1 - Normal
}

public class StorageComponent extends BaseComponent{
    
    private StorageType type;
    private ShipmentType[] shipments;

    public StorageComponent(ConnectorType[] connectors, 
                            ComponentRotation rotation,
                            StorageType type)
                            throws Exception {
        super(connectors, rotation);
        this.type = type;
        if(type == StorageType.DOUBLENORMAL || 
           type == StorageType.DOUBLESPECIAL) this.shipments = new ShipmentType[2];
        else if(type == StorageType.TRIPLENORMAL) this.shipments = new ShipmentType[3];
        else this.shipments = new ShipmentType[1];
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    //TODO Exceptions
    public void putIn(ShipmentType shipment) throws Exception{
        //TODO
    }

    public ShipmentType takeOut(int position){
        //TODO
        return ShipmentType.BLUE;
    }

    public boolean contains(ShipmentType container){
        //TODO
        return false;
    }

    public int getCapacity(){
        return shipments.length;
    }

}
