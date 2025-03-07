package it.polimi.ingsw.model.components;

enum BatteryType {
    DOUBLE,
    TRIPLE,
}

public class BatteryComponent extends BaseComponent {
    
    private BatteryType type = BatteryType.DOUBLE;
    private int contains = 0;
    private int max = 2;

    public BatteryComponent(ConnectorType[] connectors, 
                            ComponentRotation rotation, 
                            BatteryType type)
                            throws Exception{
        super(connectors, rotation);
        if(type == BatteryType.DOUBLE){
            this.max = 2;
            this.contains = 2; 
        }
        else{
            this.max = 3;
            this.contains = 3;
        }
    }

    @Override
    public void check(iVisitor v){
        v.visit(this);
    }

    public int getContains(){
        return this.contains;
    }

    //TODO exceptions
    public void takeOne() throws Exception{
        if(contains == 0){
            throw new Exception();
        }
        this.contains--;
    }

    public void putOne() throws Exception{
        if(contains == max){
            throw new Exception();
        }
        contains++;
    }
}
