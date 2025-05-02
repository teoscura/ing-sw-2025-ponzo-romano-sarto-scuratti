package it.polimi.ingsw.model.components;

import java.util.HashMap;

import it.polimi.ingsw.model.components.enums.*;
import it.polimi.ingsw.model.player.PlayerColor;

public class ComponentFactory {

	private final HashMap<Integer, BaseComponent> components;

	public BaseComponent getComponent(int id) {
		if (!this.components.containsKey(id)) throw new IllegalArgumentException("Asked for a non-existant component.");
		return this.components.get(id);
	}

	public int[] getForbiddenID() {
		return new int[]{33, 34, 52, 61, 157};
	}

	public ComponentFactory() {
		this.components = new HashMap<Integer, BaseComponent>() {{

			put(1, new BatteryComponent(
					1,
					new ConnectorType[]{
							ConnectorType.UNIVERSAL,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY},
					ComponentRotation.U000,
					BatteryType.DOUBLE)
			);
			put(2, new BatteryComponent(
					2,
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
					3,
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
					4,
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
					5,
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
					6,
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
					7,
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
					8,
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
					9,
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
					10,
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
					11,
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
					12,
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
					13,
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
					14,
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
					15,
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
					16,
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
					17,
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
					18,
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
					19,
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
					20,
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
					21,
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
					22,
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
					23,
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
					24,
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
					25,
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
					26,
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
					27,
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
					28,
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
					29,
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
					30,
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
					31,
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
					32,
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
					33,
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
					34,
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
					35,
					new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL,
							ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(36, new CabinComponent(
					36,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(37, new CabinComponent(
					37,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(38, new CabinComponent(
					38,
					new ConnectorType[]{
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(39, new CabinComponent(
					39,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(40, new CabinComponent(
					40,
					new ConnectorType[]{
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(41, new CabinComponent(
					41,
					new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.UNIVERSAL,
							ConnectorType.DOUBLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(42, new CabinComponent(
					42,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.DOUBLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(43, new CabinComponent(
					43,
					new ConnectorType[]{
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(44, new CabinComponent(
					44,
					new ConnectorType[]{
							ConnectorType.UNIVERSAL,
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.DOUBLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(45, new CabinComponent(
					45,
					new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(46, new CabinComponent(
					46,
					new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(47, new CabinComponent(
					47,
					new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(48, new CabinComponent(
					48,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(49, new CabinComponent(
					49,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(50, new CabinComponent(
					50,
					new ConnectorType[]{
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(51, new CabinComponent(
					51,
					new ConnectorType[]{
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(52, new StartingCabinComponent(
					52,
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
					53,
					new ConnectorType[]{
							ConnectorType.UNIVERSAL,
							ConnectorType.UNIVERSAL,
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(54, new StructuralComponent(
					54,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.UNIVERSAL,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(55, new StructuralComponent(
					55,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.UNIVERSAL,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(56, new StructuralComponent(
					56,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.UNIVERSAL,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(57, new StructuralComponent(
					57,
					new ConnectorType[]{
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.UNIVERSAL,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(58, new StructuralComponent(
					58,
					new ConnectorType[]{
							ConnectorType.UNIVERSAL,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(59, new StructuralComponent(
					59,
					new ConnectorType[]{
							ConnectorType.UNIVERSAL,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(60, new StructuralComponent(
					60,
					new ConnectorType[]{
							ConnectorType.UNIVERSAL,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(61, new StartingCabinComponent(
					61,
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
					62,
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
					63,
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
					64,
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
					65,
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
					66,
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
					67,
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
					68,
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
					69,
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
					70,
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
					71,
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
					72,
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
					73,
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
					74,
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
					75,
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
					76,
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
					77,
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
					78,
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
					79,
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
					80,
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
					81,
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
					82,
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
					83,
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
					84,
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
					85,
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
					86,
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
					87,
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
					88,
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
					89,
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
					90,
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
					91,
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
					92,
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
					93,
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
					94,
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
					95,
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
					96,
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
					97,
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
					98,
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
					99,
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
					100,
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
					101,
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
					102,
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
					103,
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
					104,
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
					105,
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
					106,
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
					107,
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
					108,
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
					109,
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
					110,
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
					111,
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
					112,
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
					113,
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
					114,
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
					115,
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
					116,
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
					117,
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
					118,
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
					119,
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
					120,
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
					121,
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
					122,
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
					123,
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
					124,
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
					125,
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
					126,
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
					127,
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
					128,
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
					129,
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
					130,
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
					131,
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
					132,
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
					133,
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
					134,
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
					135,
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
					136,
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
					137,
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
					138,
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
					139,
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
					140,
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
					141,
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
					142,
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
					143,
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
					144,
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
					145,
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
					146,
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
					147,
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
					148,
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
					149,
					new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.UNIVERSAL,
							ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(150, new ShieldComponent(
					150,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(151, new ShieldComponent(
					151,
					new ConnectorType[]{
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(152, new ShieldComponent(
					152,
					new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.UNIVERSAL,
							ConnectorType.DOUBLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(153, new ShieldComponent(
					153,
					new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(154, new ShieldComponent(
					154,
					new ConnectorType[]{
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.DOUBLE_CONNECTOR
					},
					ComponentRotation.U000)
			);
			put(155, new ShieldComponent(
					155,
					new ConnectorType[]{
							ConnectorType.EMPTY,
							ConnectorType.EMPTY,
							ConnectorType.SINGLE_CONNECTOR,
							ConnectorType.UNIVERSAL
					},
					ComponentRotation.U000)
			);
			put(156, new ShieldComponent(
					156,
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