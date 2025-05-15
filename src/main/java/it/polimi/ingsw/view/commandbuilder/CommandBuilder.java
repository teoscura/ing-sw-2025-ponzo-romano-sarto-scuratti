package it.polimi.ingsw.view.commandbuilder;

import java.util.regex.Pattern;

import it.polimi.ingsw.message.server.DiscardCargoMessage;
import it.polimi.ingsw.message.server.DiscardComponentMessage;
import it.polimi.ingsw.message.server.MoveCargoMessage;
import it.polimi.ingsw.message.server.PutComponentMessage;
import it.polimi.ingsw.message.server.RemoveComponentMessage;
import it.polimi.ingsw.message.server.RemoveCrewMessage;
import it.polimi.ingsw.message.server.SelectBlobMessage;
import it.polimi.ingsw.message.server.SelectLandingMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.SetCrewMessage;
import it.polimi.ingsw.message.server.TakeCargoMessage;
import it.polimi.ingsw.message.server.TakeDiscardedComponentMessage;
import it.polimi.ingsw.message.server.TakeRewardMessage;
import it.polimi.ingsw.message.server.TurnOnMessage;
import it.polimi.ingsw.message.server.UsernameSetupMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;

public class CommandBuilder {
    
    public ServerMessage build(String command){
		
        ServerMessage mess = null;
        boolean valid = false;
        String[] parts = command.split(" ", 16);

        switch (parts[0]) {
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
				mess  = new DiscardComponentMessage(discardId);
				break;
			case "movecargo":
				valid = Pattern.matches("^movecargo [0-9] [0-9] [0-4] [0-9] [0-9]$", command);
				if (!valid) break;
				ShipCoords cargoTarget = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				ShipCoords cargoSource = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[4]),
						Integer.parseInt(parts[5]));
				ShipmentType cargoType = ShipmentType.values()[4 - Integer.parseInt(parts[3])];
				mess  = new MoveCargoMessage(cargoTarget, cargoSource, cargoType);
				break;
			case "putcomponent":
				valid = Pattern.matches("^putcomponent [0-9] [0-9] [0-3]$", command);
				if (!valid) break;
				ShipCoords componentCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				ComponentRotation componentRotation = ComponentRotation.values()[Integer.parseInt(parts[3])];
				mess = new PutComponentMessage(componentCoords,
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
				mess  = new RemoveCrewMessage(crewCoords);
				break;
			case "selectlanding":
				valid = Pattern.matches("^selectlanding [0-9]+$", command);
				if (!valid) break;
				int landingId = Integer.parseInt(parts[1]);
				mess = new SelectLandingMessage(landingId);
				break;
			case "setcrew":
				valid = Pattern.matches("^setcrew [0-9] [0-9] [0-3]$", command);
				if (!valid) break;
				ShipCoords cabinCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				AlienType alienType = AlienType.values()[Integer.parseInt(parts[3])];
				mess = new SetCrewMessage(cabinCoords, alienType);
				break;
			case "takecargo":
				valid = Pattern.matches("^takecargo [0-9] [0-9] [0-4]$", command);
				if (!valid) break;
				ShipCoords cargCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
				ShipmentType cargoType1 = ShipmentType.values()[4 - Integer.parseInt(parts[3])];
				mess= new TakeCargoMessage(cargCoords, cargoType1);
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
			case "usernamesetup":
				valid = Pattern.matches("^usernamesetup [a-zA-Z0-9_]+$", command);
				if (!valid) break;
				String username = parts[1];
				mess = new UsernameSetupMessage(username);
				break;
			default:
				System.out.println("Command not recognized: '" + command + "'");
				break;
		}
		if (!valid) {
			System.out.println("Command not valid");
		}

		return mess;
    }

}
