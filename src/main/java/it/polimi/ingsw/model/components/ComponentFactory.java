package it.polimi.ingsw.model.components;

import java.util.HashMap;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.*;

//TODO.
public class ComponentFactory {
    
    private HashMap<Integer,iBaseComponent> components;

    public ComponentFactory(){
        this.components = new HashMap<Integer, iBaseComponent>() {{
            put(1, new BatteryComponent(
                        new ConnectorType[]{ConnectorType.UNIVERSAL,
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
                        BatteryType.DOUBLE
                )
            );
            put(4, new BatteryComponent(
                        new ConnectorType[]{
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        BatteryType.DOUBLE
                )
            );
            put(5, new BatteryComponent(
                        new ConnectorType[]{
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        BatteryType.DOUBLE
                )
            );
            put(6, new BatteryComponent(
                        new ConnectorType[]{
                            ConnectorType.UNIVERSAL,
                        },
                )
            );
            put(7, new CaCACA
            );
            put(8, new CaCACA
                
            );
            put(9, new CaCACA
                
            );
            put(10, new CaCACA
                
            );
            put(11, new CaCACA
                
            );
            put(12, new CaCACA
                
            );
            put(13, new CaCACA
                
            );
            put(14, new CaCACA
                
            );
            put(15, new CaCACA
                
            );
            put(16, new CaCACA
                
            );
            put(17, new CaCACA
                
            );
            put(18, new StorageComponent(
                        new ConnectorType[]{
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        StorageType.DOUBLENORMAL
                )
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
            put(33, new BLUESTARTINGCABIN(
                new ConnectorType[]{
                    ConnectorType.SINGLE_CONNECTOR,
                    ConnectorType.EMPTY,
                    ConnectorType.SINGLE_CONNECTOR,
                    ConnectorType.EMPTY
                },
                ComponentRotation.U000,
                StorageType.TRIPLENORMAL)     
            );
            put(34, new GREESTARTINGCABIN
                
            );
            put(35, new CaCACA
                
            );
            put(36, new CaCACA
                
            );
            put(37, new CaCACA
                
            );
            put(38, new CaCACA
                
            );
            put(39, new CaCACA
                
            );
            put(40, new CaCACA
                
            );
            put(41, new CaCACA
                
            );
            put(42, new CaCACA
                
            );
            put(43, new CaCACA
                
            );
            put(44, new CaCACA
                
            );
            put(45, new CaCACA
                
            );
            put(46, new CaCACA
                
            );
            put(47, new CaCACA
                
            );
            put(48, new CaCACA
                
            );
            put(49, new CaCACA
                
            );
            put(50, new CaCACA
                
            );
            put(51, new CaCACA
                
            );
            put(52, new REDSTARTINGCABIN
                
            );
            put(53, new CaCACA
                
            );
            put(54, new CaCACA
                
            );
            put(55, new CaCACA
                
            );
            put(56, new CaCACA
                
            );
            put(57, new CaCACA
                
            );
            put(58, new CaCACA
                
            );
            put(59, new CaCACA
                
            );
            put(60, new CaCACA
                
            );
            put(61, new CaCACA
                
            );
            put(62, new YELLOWSTARTINGCABIN
                
            );
            put(63, new CaCACA
                
            );
            put(64, new CaCACA
                
            );
            put(65, new CaCACA
                
            );
            put(66, new CaCACA
                
            );
            put(67, new CaCACA
                
            );
            put(68, new CaCACA
                
            );
            put(69, new CaCACA
                
            );
            put(70, new CaCACA
                
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
                        EngineType.SINGLE;
                )
            );

    
            put(72, new CaCACA

            );
            put(73, new CaCACA

            );
            put(74, new CaCACA

            );
            put(75, new CaCACA

            );
            put(76, new CaCACA

            );
            put(77, new CaCACA

            );
            put(78, new CaCACA

            );
            put(79, new CaCACA

            );
            put(80, new CaCACA

            );
            put(81, new CaCACA

            );
            put(82, new CaCACA

            );
            put(83, new CaCACA

            );
            put(84, new CaCACA

            );
            put(85, new CaCACA

            );
            put(86, new CaCACA

            );
            put(87, new CaCACA

            );
            put(88, new CaCACA

            );
            put(89, new CaCACA

            );
            put(90, new CaCACA

            );
            put(91, new CaCACA

            );
			//END ENGINE
            put(92, new CaCACA

            );
            put(93, new CaCACA

            );
            put(94, new CaCACA

            );
            put(95, new CaCACA

            );
            put(96, new CaCACA

            );
            put(97, new CaCACA

            );
            put(98, new CaCACA

            );
            put(99, new CaCACA

            );
            put(100, new CaCACA
            
            );
            put(101, new CaCACA

            );
            put(102, new CaCACA

            );
            put(103, new CaCACA

            );
            put(104, new CaCACA

            );
            put(105, new CaCACA

            );
            put(106, new CaCACA

            );
            put(107, new CaCACA

            );
            put(108, new CaCACA

            );
            put(109, new CaCACA

            );
            put(110, new CaCACA

            );
            put(111, new CaCACA

            );
            put(112, new CaCACA

            );
            put(113, new CaCACA

            );
            put(114, new CaCACA

            );
            put(115, new CaCACA

            );
            put(116, new CaCACA

            );
            put(117, new CaCACA

            );
            put(118, new CaCACA

            );
            put(119, new CaCACA

            );
            put(120, new CaCACA

            );
            put(121, new CaCACA

            );
            put(122, new CaCACA

            );
            put(123, new CaCACA

            );
            put(124, new CaCACA

            );
            put(125, new CaCACA

            );
            put(126, new CaCACA

            );
            put(127, new CaCACA

            );
            put(128, new CaCACA

            );
            put(129, new CaCACA

            );
            put(130, new CaCACA

            );
            put(131, new CaCACA

            );
            put(132, new CaCACA

            );
            put(133, new CaCACA

            );
            put(134, new CaCACA

            );
            put(135, new CaCACA

            );
            put(136, new CaCACA

            );
            put(137, new CaCACA

            );
            put(138, new CaCACA

            );
            put(139, new CaCACA

            );
            put(140, new CaCACA

            );
            put(141, new CaCACA

            );
            put(142, new CaCACA

            );
            put(143, new CaCACA

            );
            put(144, new CaCACA

            );
            put(145, new CaCACA

            );
            put(146, new CaCACA

            );
            put(147, new CaCACA

            );
            put(148, new CaCACA

            );
            put(149, new CaCACA

            );
            put(150, new CaCACA

            );
            put(151, new CaCACA

            );
            put(152, new CaCACA

            );
            put(153, new CaCACA

            );
            put(154, new CaCACA

            );
            put(155, new CaCACA

            );
            put(156, new CaCACA

            );
            put(157, new CaCACA

            );
        }};
    }
}

//TODO: RIPROGRAMMARE LE CABIN PERMETTERE QUELLI SPECIALI XD XD XD XD XD XD 
//TODO: RIFARE SHIELD PERCHE' SONO UN MONA.