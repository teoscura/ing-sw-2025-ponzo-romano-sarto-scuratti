package it.polimi.ingsw.model.components;

import java.util.HashMap;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.*;

//TODO.
public class ComponentFactory {

    private HashMap<Integer, iBaseComponent> components;

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
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        BatteryType.DOUBLE
                )
            );
            put(7, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.UNIVERSAL,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE
                    )
            );
            put(8, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE
                    )
            );
            put(9, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE
                    )
            );
            put(10, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE
                    )
            );
            put(11, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    BatteryType.DOUBLE
                    )
            );
            put(12, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE
                    )
            );
            put(13, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE
                    )
            );
            put(14, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE
                    )
            );
            put(15, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE
                    )
            );
            put(16, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE
                    )
            );
            put(17, new BatteryComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    BatteryType.TRIPLE
                    )
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
            /*put(33, new BLUESTARTINGCABIN(
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
            put(61, new YELLOWSTARTINGCABIN
                
            );*/
            put(62, new StorageComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL
                    )
            );
            put(63, new StorageComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL
                    )
            );
            put(64, new StorageComponent(
                    new ConnectorType[]{
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL
                    )
            );
            put(65, new StorageComponent(
                    new ConnectorType[]{
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL
                    )
            );
            put(66, new StorageComponent(
                    new ConnectorType[]{
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL
                    )
            );
            put(67, new StorageComponent(
                    new ConnectorType[]{
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    StorageType.SINGLESPECIAL
                    )
            );
            put(68, new StorageComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLESPECIAL
                    )
            );
            put(69, new StorageComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLESPECIAL
                    )
            );
            put(70, new StorageComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    StorageType.DOUBLESPECIAL
                    )
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
				EngineType.SINGLE
		));

		put(72, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.EMPTY,
					ConnectorType.UNIVERSAL,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY    
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(73, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.SINGLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(74, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.SINGLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY           
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(75, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.SINGLE_CONNECTOR,
					ConnectorType.SINGLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY           
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(76, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.DOUBLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY           
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(77, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.DOUBLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY           
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(78, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.DOUBLE_CONNECTOR,
					ConnectorType.UNIVERSAL,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(79, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.UNIVERSAL,
					ConnectorType.DOUBLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY           
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(80, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.EMPTY,
					ConnectorType.UNIVERSAL,
					ConnectorType.EMPTY,
					ConnectorType.SINGLE_CONNECTOR
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(81, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.DOUBLE_CONNECTOR,
					ConnectorType.SINGLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.SINGLE_CONNECTOR
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(82, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.UNIVERSAL,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY,
					ConnectorType.SINGLE_CONNECTOR
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(83, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.UNIVERSAL,
					ConnectorType.DOUBLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.SINGLE_CONNECTOR
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(84, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.EMPTY,
					ConnectorType.SINGLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.DOUBLE_CONNECTOR
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(85, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.SINGLE_CONNECTOR,
					ConnectorType.DOUBLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.DOUBLE_CONNECTOR
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(86, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.DOUBLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY,
					ConnectorType.DOUBLE_CONNECTOR
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(87, new EngineComponent(
				new ConnectorType[]{
					ConnectorType.UNIVERSAL,
					ConnectorType.SINGLE_CONNECTOR,
					ConnectorType.EMPTY,
					ConnectorType.DOUBLE_CONNECTOR
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(88, new EngineComponent(
				new ConnectorType[]{
						ConnectorType.EMPTY,
					ConnectorType.EMPTY,
					ConnectorType.EMPTY,
					ConnectorType.UNIVERSAL
				},
				ComponentRotation.U000,
				EngineType.SINGLE
		));

		put(89, new EngineComponent(
				new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
				},
						ComponentRotation.U000,
						EngineType.SINGLE
		));

		put(90, new EngineComponent(
				new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
				},
						ComponentRotation.U000,
						EngineType.SINGLE
		));

		put(91, new EngineComponent(
						new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
				},
						ComponentRotation.U000,
						EngineType.SINGLE
			));


			put(92, new EngineComponent(
						new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY
						},
						ComponentRotation.U000,
						EngineType.DOUBLE
			));

			put(93, new EngineComponent(
						new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.UNIVERSAL,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY
						},
						ComponentRotation.U000,
						EngineType.DOUBLE
			));

			put(94, new EngineComponent(
						new ConnectorType[]{
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY
						},
						ComponentRotation.U000,
						EngineType.DOUBLE
			));

			put(95, new EngineComponent(
						new ConnectorType[]{
							ConnectorType.UNIVERSAL,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY
						},
						ComponentRotation.U000,
						EngineType.DOUBLE
			));

			put(96, new EngineComponent(
						new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR
						},
						ComponentRotation.U000,
						EngineType.DOUBLE
			));

			put(97, new EngineComponent(
						new ConnectorType[]{
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.DOUBLE_CONNECTOR
						},
						ComponentRotation.U000,
						EngineType.DOUBLE
			));

			put(98, new EngineComponent(
						new ConnectorType[]{
							ConnectorType.UNIVERSAL,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.DOUBLE_CONNECTOR
						},
						ComponentRotation.U000,
						EngineType.DOUBLE
			));

			put(99, new EngineComponent(
						new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
						},
						ComponentRotation.U000,
						EngineType.DOUBLE
			));

			put(100, new EngineComponent(
						new ConnectorType[]{
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
						},
						ComponentRotation.U000,
						EngineType.DOUBLE
			));

			//END ENGINE 
            
            //BEGIN CANNON


            put(101, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(102, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(103, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(104, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(105, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(106, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(107, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(108, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(109, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(110, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(111, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(112, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(113, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(114, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(115, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(116, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(117, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(118, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(119, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(120, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.DOUBLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));



            put(121, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(122, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.DOUBLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(123, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(124, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.UNIVERSAL
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));

            put(125, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                        },
                        ComponentRotation.U000,
                        CannonType.SINGLE
            ));
            put(126, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            put(127, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            put(128, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            put(129, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            put(130, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            put(131, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            put(132, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            put(133, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL,
                            ConnectorType.DOUBLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            put(134, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            put(135, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            put(136, new CannonComponent(
                        new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.UNIVERSAL
                        },
                        ComponentRotation.U000,
                        CannonType.DOUBLE
            ));

            // END CANNON


            put(137, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN
                    )
            );
            put(138, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN
                    )
            );
            put(139, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN
                    )
            );
            put(140, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN
                    )
            );
            put(141, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN
                    )
            );
            put(142, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.BROWN
                    )
            );
            put(143, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE
                    )
            );
            put(144, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE
                    )
            );
            put(145, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE
                    )
            );
            put(146, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE
                    )
            );
            put(147, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.EMPTY,
                            ConnectorType.SINGLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE
                    )
            );
            put(148, new AlienLifeSupportComponent(
                    new ConnectorType[]{
                            ConnectorType.DOUBLE_CONNECTOR,
                            ConnectorType.EMPTY,
                            ConnectorType.EMPTY,
                            ConnectorType.UNIVERSAL
                    },
                    ComponentRotation.U000,
                    AlienType.PURPLE
                    )
            );
            /*put(149, new CaCACA

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

            );*/
        }};
    }
}

// TODO: RIPROGRAMMARE LE CABIN PERMETTERE QUELLI SPECIALI XD XD XD XD XD XD
// TODO: RIFARE SHIELD PERCHE' SONO UN MONA.