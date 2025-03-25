package it.polimi.ingsw.model.components;

import java.util.HashMap;

import it.polimi.ingsw.model.components.enums.*;
import it.polimi.ingsw.model.player.PlayerColor;

public class ComponentFactory implements iComponentFactory {

    private HashMap<Integer, iBaseComponent> components;

    //FIXME quando carichiamo dentro common board ricordarsi di non chiedere per le 4 starting cabin e per l'empty component.
    @Override
    public iBaseComponent getComponent(int id){
        if(this.components.containsKey(id)) throw new IllegalArgumentException("Asked for a non-existant component.");
        return this.components.get(id);
    }

    public ComponentFactory(){
        this.components = new HashMap<Integer, iBaseComponent>() {{

            put(1, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY}, 
                    ComponentRotation.U000, 
                    BatteryType.DOUBLE)                 
            );
            put(2, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE)
            );
            put(3, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE)
            );
            put(4, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE)
            );
            put(5, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE)
            );
            put(6, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE)
            );
            put(7, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE)
            );
            put(8, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE)
            );
            put(9, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE)
            );
            put(10, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE)
            );
            put(11, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE)
            );
            put(12, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE)
            );
            put(13, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE)
            );
            put(14, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE)
            );
            put(15, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE)
            );
            put(16, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE)
            );
            put(17, new BatteryComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE)
            );
            put(18, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLENORMAL)
            );
            put(19, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLENORMAL)       
            );
            put(20, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLENORMAL)
            );
            put(21, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLENORMAL)
            );
            put(22, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLENORMAL)
            );
            put(23, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLENORMAL)
            );
            put(24, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLENORMAL)
            );
            put(25, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLENORMAL)
            );
            put(26, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLENORMAL)
            );
            put(27, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.TRIPLENORMAL)
            );
            put(28, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    StorageType.TRIPLENORMAL)
            );
            put(29, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.TRIPLENORMAL)
            );
            put(30, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    StorageType.TRIPLENORMAL)
            );
            put(31, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.TRIPLENORMAL)
            );
            put(32, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.TRIPLENORMAL)
            );
            put(33, new StartingCabinComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    PlayerColor.BLUE)   
            );
            put(34, new StartingCabinComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    PlayerColor.GREEN)
            );
            put(35, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000) 
            );
            put(36, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(37, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(38, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(39, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(40, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(41, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(42, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(43, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(44, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(45, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(46, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(47, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(48, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(49, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(50, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(51, new CabinComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(52, new StartingCabinComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    PlayerColor.RED)
            );
            put(53, new StructuralComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(54, new StructuralComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(55, new StructuralComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(56, new StructuralComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(57, new StructuralComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(58, new StructuralComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(59, new StructuralComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(60, new StructuralComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(61, new StartingCabinComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    PlayerColor.YELLOW)
            );
            put(62, new StorageComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL)
            );
            put(63, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL)
            );
            put(64, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL)
            );
            put(65, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL)
            );
            put(66, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL)
            );
            put(67, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL)
            );
            put(68, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLESPECIAL)
            );
            put(69, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLESPECIAL)
            );
            put(70, new StorageComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLESPECIAL)
            );
			//BEGIN ENGINE
            put(71, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY    
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
		    );
		    put(72, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY    
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(73, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(74, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY           
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(75, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY           
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(76, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY           
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(77, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY           
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(78, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(79, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY           
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(80, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(81, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(82, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(83, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(84, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(85, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(86, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(87, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(88, new EngineComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(89, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
            put(90, new EngineComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    EngineType.SINGLE)
            );
		    put(91, new EngineComponent(
				    new ConnectorType[]{
					    ConnectorType.SINGLE_CONNECTOR,
					    ConnectorType.EMPTY,
					    ConnectorType.EMPTY,
					    ConnectorType.UNIVERSAL
				    },
				    ComponentRotation.U000,
				    EngineType.SINGLE)
			);
			put(92, new EngineComponent(
					new ConnectorType[]{
						ConnectorType.SINGLE_CONNECTOR,
						ConnectorType.EMPTY,
						ConnectorType.EMPTY,
						ConnectorType.EMPTY
					},
					ComponentRotation.U000,
			    	EngineType.DOUBLE)
			);
			put(93, new EngineComponent(
					new ConnectorType[]{
						ConnectorType.SINGLE_CONNECTOR,
			    		ConnectorType.UNIVERSAL,
						ConnectorType.EMPTY,
						ConnectorType.EMPTY
					},
					ComponentRotation.U000,
					EngineType.DOUBLE)
			);
			put(94, new EngineComponent(
					new ConnectorType[]{
						ConnectorType.DOUBLE_CONNECTOR,
						ConnectorType.EMPTY,
						ConnectorType.EMPTY,
						ConnectorType.EMPTY
					},
					ComponentRotation.U000,
					EngineType.DOUBLE)
			);
			put(95, new EngineComponent(
					new ConnectorType[]{
						ConnectorType.UNIVERSAL,
						ConnectorType.SINGLE_CONNECTOR,
						ConnectorType.EMPTY,
						ConnectorType.EMPTY
					},
					ComponentRotation.U000,
					EngineType.DOUBLE)
			);
			put(96, new EngineComponent(
					new ConnectorType[]{
						ConnectorType.SINGLE_CONNECTOR,
						ConnectorType.SINGLE_CONNECTOR,
						ConnectorType.EMPTY,
						ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000,
					EngineType.DOUBLE)
			);
			put(97, new EngineComponent(
					new ConnectorType[]{
						ConnectorType.DOUBLE_CONNECTOR,
						ConnectorType.DOUBLE_CONNECTOR,
						ConnectorType.EMPTY,
						ConnectorType.DOUBLE_CONNECTOR
					},
					ComponentRotation.U000,
					EngineType.DOUBLE)
            );
			put(98, new EngineComponent(
					new ConnectorType[]{
						ConnectorType.UNIVERSAL,
						ConnectorType.EMPTY,
						ConnectorType.EMPTY,
						ConnectorType.DOUBLE_CONNECTOR
					},
					ComponentRotation.U000,
					EngineType.DOUBLE)
			);
			put(99, new EngineComponent(
					new ConnectorType[]{
						ConnectorType.EMPTY,
						ConnectorType.UNIVERSAL,
						ConnectorType.EMPTY,
						ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000,
					EngineType.DOUBLE)
			);
			put(100, new EngineComponent(
					new ConnectorType[]{
						ConnectorType.DOUBLE_CONNECTOR,
						ConnectorType.EMPTY,
						ConnectorType.EMPTY,
						ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000,
				    EngineType.DOUBLE)
			);
            put(101, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(102, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(103, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(104, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(105, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(106, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(107, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(108, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(109, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(110, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(111, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(112, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(113, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(114, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(115, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(116, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(117, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(118, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(119, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(120, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(121, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(122, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(123, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(124, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(125, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    CannonType.SINGLE)
            );
            put(126, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(127, new CannonComponent(                        
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(128, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(129, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(130, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(131, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(132, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(133, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(134, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(135, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(136, new CannonComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    CannonType.DOUBLE)
            );
            put(137, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN)
            );
            put(138, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN)
            );
            put(139, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN)
            );
            put(140, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN)
            );
            put(141, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN)
            );
            put(142, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN)
            );
            put(143, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE)
            );
            put(144, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE)
            );
            put(145, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE)
            );
            put(146, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE)
            );
            put(147, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE)
            );
            put(148, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE)
            );
            put(149, new ShieldComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(150, new ShieldComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(151, new ShieldComponent(
                    new ConnectorType[]{
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(152, new ShieldComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(153, new ShieldComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(154, new ShieldComponent(
                    new ConnectorType[]{
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000)
            );
            put(155, new ShieldComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.EMPTY,
                        ConnectorType.SINGLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(156, new ShieldComponent(
                    new ConnectorType[]{
                        ConnectorType.EMPTY,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.DOUBLE_CONNECTOR,
                        ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000)
            );
            put(157, new EmptyComponent());
        }};
    }
}