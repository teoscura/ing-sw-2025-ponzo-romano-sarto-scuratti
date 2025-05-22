package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.message.server.*;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;

import java.util.regex.Pattern;

public class CommandBuilder {

	private final TUIView view;

	public CommandBuilder(TUIView view){
		this.view = view;
	}

	public ServerMessage build(String command) {
		if(command == null) return null; 
		command = command.trim();
		ServerMessage mess = null;
		boolean valid = false;
		String[] parts = command.split(" ", 16);
		switch (parts[0]) {
			case "entersetup":
				valid = Pattern.matches("^entersetup$", command);
				if (!valid) break;
				mess = new EnterSetupMessage();
				break;
			case "leavesetup":
				valid = Pattern.matches("^leavesetup$", command);
				if (!valid) break;
				mess = new LeaveSetupMessage();
				break;
			case "openlobby":
				valid = Pattern.matches("^openlobby [1-2]+ [2-4]$", command);
				if (!valid) break;
				GameModeType t = Integer.parseInt(parts[1]) == 1 ? GameModeType.TEST : GameModeType.LVL2;
				int tmp = Integer.parseInt(parts[2]);
				PlayerCount c = tmp != 2 ? tmp != 3 ? PlayerCount.FOUR : PlayerCount.THREE : PlayerCount.TWO;
				mess = new OpenLobbyMessage(t, c);
				break;
			case "openunfinished":
				valid = Pattern.matches("^openunfinished [0-9]+$", command);
				if (!valid) break;
				mess = new OpenUnfinishedMessage(Integer.parseInt(parts[1]));
				break;
			case "enterlobby":
				valid = Pattern.matches("^enterlobby [0-9]+$", command);
				if (!valid) break;
				mess = new EnterLobbyMessage(Integer.parseInt(parts[1]));
				break;
			case "blobselect":
				valid = Pattern.matches("^blobselect [0-9] [0-9]$", command);
				if (!valid) break;
				ShipCoords blobCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				mess = new SelectBlobMessage(blobCoords);
				break;
			case "discardcargo":
				valid = Pattern.matches("^discardcargo [0-9] [0-9] [0-4]$", command);
				if (!valid) break;
				ShipCoords discardCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				ShipmentType discardType = ShipmentType.values()[Integer.parseInt(parts[3])];
				mess = new DiscardCargoMessage(discardCoords, discardType);
				break;
			case "discardcomponent":
				valid = Pattern.matches("^discardcomponent [0-9]+", command);
				if (!valid)
					break;
				int discardId = Integer.parseInt(parts[1]);
				mess = new DiscardComponentMessage(discardId);
				break;
			case "movecargo":
				valid = Pattern.matches("^movecargo [0-9] [0-9] [0-4] [0-9] [0-9]$", command);
				if (!valid) break;
				ShipCoords cargoTarget = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				ShipCoords cargoSource = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[4]),
						Integer.parseInt(parts[5]));
				ShipmentType cargoType = ShipmentType.values()[4 - Integer.parseInt(parts[3])];
				mess = new MoveCargoMessage(cargoTarget, cargoSource, cargoType);
				break;
			case "takecomponent":
				valid = Pattern.matches("^takecomponent$", command);
				if (!valid) break;
				mess = new TakeComponentMessage();
				break;
			case "putcomponent":
				valid = Pattern.matches("^putcomponent [0-9]+ [0-9] [0-9] [0-3]$", command);
				if (!valid) break;
				ShipCoords componentCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3]));
				ComponentRotation componentRotation = ComponentRotation.values()[Integer.parseInt(parts[4])];
				mess = new PutComponentMessage(Integer.parseInt(parts[1]), componentCoords,
						componentRotation);
				break;
			case "removecomponent":
				valid = Pattern.matches("^removecomponent [0-9] [0-9]$", command);
				if (!valid) break;
				ShipCoords componentCoords1 = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				mess = new RemoveComponentMessage(componentCoords1);
				break;
			case "removecrew":
				valid = Pattern.matches("^removecrew [0-9] [0-9]$", command);
				if (!valid) break;
				ShipCoords crewCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				mess = new RemoveCrewMessage(crewCoords);
				break;
			case "selectlanding":
				valid = Pattern.matches("^selectlanding ([0-9]+|-[0-9]+)$", command);
				if (!valid) break;
				int landingId = Integer.parseInt(parts[1]);
				mess = new SelectLandingMessage(landingId);
				break;
			case "setcrew":
				valid = Pattern.matches("^setcrew [0-9] [0-9] [0-3]$", command);
				if (!valid) break;
				ShipCoords cabinCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				AlienType alienType = Integer.parseInt(parts[3]) == 1 ? AlienType.BROWN : Integer.parseInt(parts[3]) == 2 ? AlienType.PURPLE : AlienType.HUMAN;
				mess = new SetCrewMessage(cabinCoords, alienType);
				break;
			case "takecargo":
				valid = Pattern.matches("^takecargo [0-9] [0-9] [0-4]$", command);
				if (!valid) break;
				ShipCoords cargCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				ShipmentType cargoType1 = ShipmentType.values()[4 - Integer.parseInt(parts[3])];
				mess = new TakeCargoMessage(cargCoords, cargoType1);
				break;
			case "takediscarded":
				valid = Pattern.matches("^takediscarded [0-9]+", command);
				if (!valid) break;
				int takeId = Integer.parseInt(parts[1]);
				mess = new TakeDiscardedComponentMessage(
						takeId);
				break;
			case "takereward":
				valid = Pattern.matches("^takereward (true|false)$", command);
				if (!valid) break;
				boolean takeReward = Boolean.parseBoolean(parts[1]);
				mess = new TakeRewardMessage(takeReward);
				break;
			case "turnon":
				valid = Pattern.matches("^turnon [0-9] [0-9] [0-9] [0-9]$", command);
				if (!valid) break;
				ShipCoords targetCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				ShipCoords batteryCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[3]),
						Integer.parseInt(parts[4]));
				mess = new TurnOnMessage(targetCoords, batteryCoords);
				break;
			case "giveup":
				valid = Pattern.matches("^giveup$", command);
				if (!valid) break;
				mess = new PlayerGiveUpMessage();
				break;
			case "sendcontinue":
				valid = Pattern.matches("^sendcontinue$", command);
				if (!valid) break;
				mess = new SendContinueMessage();
				break;
			case "togglehourglass":
				valid = Pattern.matches("^togglehourglass$", command);
				if (!valid) break;
				mess = new ToggleHourglassMessage();
				break;
			default:
		}

		if (!valid) {
			view.showTextMessage("Command not valid");
		}

		return mess;
	}

}
