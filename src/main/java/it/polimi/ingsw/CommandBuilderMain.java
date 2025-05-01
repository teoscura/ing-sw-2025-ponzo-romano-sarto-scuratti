package it.polimi.ingsw;

import it.polimi.ingsw.message.server.*;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;

import java.util.Scanner;
import java.util.regex.Pattern;

public class CommandBuilderMain {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Type a command or 'exit' to quit.");

		while (true) {
			String inputLine = scanner.nextLine().trim();

			if (inputLine.isEmpty()) {
				continue;
			}

			if (inputLine.equalsIgnoreCase("exit")) {
				System.out.println("Exiting.");
				break;
			}

			String[] parts = inputLine.split(" ", 16);
			String command = parts[0];

			boolean isValid = false;
			String expectedFormat;

			switch (command) {
				case "blobselect":

					isValid = Pattern.matches("^blobselect [0-9] [0-9]$", inputLine);
					expectedFormat = "blobselect [0-9] [0-9]";
					if (!isValid)
						break;
					ShipCoords blobCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]));
					SelectBlobMessage blobMessage = new SelectBlobMessage(blobCoords);
					System.out.println(blobMessage.getCoords());
					break;

				case "discardcargo":
					isValid = Pattern.matches("^discardcargo [0-9] [0-9] [0-4]$", inputLine);
					expectedFormat = "discardcargo [0-9] [0-9] [0-4]";
					if (!isValid)
						break;
					ShipCoords discardCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]));
					ShipmentType discardType = ShipmentType.values()[Integer.parseInt(parts[3])];
					DiscardCargoMessage discardMessage = new DiscardCargoMessage(discardCoords, discardType);
					System.out.println(discardMessage.getCoords());
					System.out.println(discardMessage.getShipmentType());

					break;

				case "discardcomponent":
					isValid = Pattern.matches("^discardcomponent [0-9]+", inputLine);
					expectedFormat = "discardcomponent [0-155]";
					if (!isValid)
						break;
					int discardId = Integer.parseInt(parts[1]);
					DiscardComponentMessage discardComponentMessage = new DiscardComponentMessage(discardId);
					System.out.println(discardComponentMessage.getId());
					break;

				case "movecargo":
					isValid = Pattern.matches("^movecargo [0-9] [0-9] [0-4] [0-9] [0-9]$", inputLine);
					expectedFormat = "movecargo [0-9] [0-9] [0-4] [0-9] [0-9]";
					if (!isValid)
						break;
					ShipCoords cargoTarget = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]));
					ShipCoords cargoSource = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[4]),
							Integer.parseInt(parts[5]));
					ShipmentType cargoType = ShipmentType.values()[4 - Integer.parseInt(parts[3])];
					MoveCargoMessage moveCargoMessage = new MoveCargoMessage(cargoTarget, cargoSource, cargoType);
					System.out.println("Target: " + moveCargoMessage.getTarget());
					System.out.println("Source: " + moveCargoMessage.getSource());
					System.out.println(moveCargoMessage.getShipmentType());
					break;

				case "putcomponent":
					isValid = Pattern.matches("^putcomponent [0-9] [0-9] [0-3]$", inputLine);
					expectedFormat = "putcomponent [0-9] [0-9] [0-3]";
					if (!isValid)
						break;
					ShipCoords componentCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]));
					ComponentRotation componentRotation = ComponentRotation.values()[Integer.parseInt(parts[3])];
					PutComponentMessage putComponentMessage = new PutComponentMessage(componentCoords,
							componentRotation);
					System.out.println(putComponentMessage.getCoords());
					System.out.println(putComponentMessage.getRotation());
					break;

				case "removecomponent":
					isValid = Pattern.matches("^removecomponent [0-9] [0-9]$", inputLine);
					expectedFormat = "removecomponent [0-9] [0-9]";
					if (!isValid)
						break;
					ShipCoords componentCoords1 = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]));
					RemoveComponentMessage removeComponentMessage = new RemoveComponentMessage(componentCoords1);
					System.out.println(removeComponentMessage.getCoords());
					break;

				case "removecrew":
					isValid = Pattern.matches("^removecrew [0-9] [0-9]$", inputLine);
					expectedFormat = "removecrew [0-9] [0-9]";
					if (!isValid)
						break;
					ShipCoords crewCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]));
					RemoveCrewMessage removeCrewMessage = new RemoveCrewMessage(crewCoords);
					System.out.println(removeCrewMessage.getCoords());
					break;

				case "selectlanding":
					isValid = Pattern.matches("^selectlanding [0-9]+$", inputLine);
					expectedFormat = "selectlanding [unsigned int]";
					if (!isValid)
						break;
					int landingId = Integer.parseInt(parts[1]);
					SelectLandingMessage selectLandingMessage = new SelectLandingMessage(landingId);
					System.out.println(selectLandingMessage.getId());
					break;

				case "setcrew":
					isValid = Pattern.matches("^setcrew [0-9] [0-9] [0-3]$", inputLine);
					expectedFormat = "setcrew [0-9] [0-9] [0-3]";
					if (!isValid)
						break;
					ShipCoords cabinCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]));
					AlienType alienType = AlienType.values()[Integer.parseInt(parts[3])];
					SetCrewMessage setCrewMessage = new SetCrewMessage(cabinCoords, alienType);
					System.out.println(setCrewMessage.getCoords());
					System.out.println(setCrewMessage.getAlienType());
					break;

				case "takecargo":
					isValid = Pattern.matches("^takecargo [0-9] [0-9] [0-4]$", inputLine);
					expectedFormat = "takecargo [0-9] [0-9] [0-4]";
					if (!isValid)
						break;
					ShipCoords cargCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]));
					ShipmentType cargoType1 = ShipmentType.values()[4 - Integer.parseInt(parts[3])];
					TakeCargoMessage takeCargoMessage = new TakeCargoMessage(cargCoords, cargoType1);
					System.out.println(takeCargoMessage.getCoords());
					System.out.println(takeCargoMessage.getShipmentType());
					break;

				case "takediscarded":
					isValid = Pattern.matches("^takediscarded [0-9]+", inputLine);
					expectedFormat = "takediscarded [0-155]";
					if (!isValid)
						break;
					int takeId = Integer.parseInt(parts[1]);
					TakeDiscardedComponentMessage takeDiscardedComponentMessage = new TakeDiscardedComponentMessage(
							takeId);
					System.out.println(takeDiscardedComponentMessage.getId());
					break;

				case "takereward":
					isValid = Pattern.matches("^takereward (true|false)$", inputLine);
					expectedFormat = "takereward [true|false]";

					if (!isValid)
						break;
					boolean takeReward = Boolean.parseBoolean(parts[1]);
					TakeRewardMessage takeRewardMessage = new TakeRewardMessage(takeReward);
					System.out.println(takeRewardMessage.getTook());
					break;

				case "turnon":
					isValid = Pattern.matches("^turnon [0-9] [0-9] [0-9] [0-9]$", inputLine);
					expectedFormat = "turnon [0-9] [0-9] [0-9] [0-9]";
					if (!isValid)
						break;
					ShipCoords targetCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[1]),
							Integer.parseInt(parts[2]));
					ShipCoords batteryCoords = new ShipCoords(GameModeType.TEST, Integer.parseInt(parts[3]),
							Integer.parseInt(parts[4]));
					TurnOnMessage turnOnMessage = new TurnOnMessage(targetCoords, batteryCoords);
					System.out.println("Target: " + turnOnMessage.getTarget());
					System.out.println("Battery: " + turnOnMessage.getBattery());
					break;

				case "usernamesetup":
					isValid = Pattern.matches("^usernamesetup [a-zA-Z0-9_]+$", inputLine);
					expectedFormat = "usernamesetup [a-zA-Z0-9_]+";
					if (!isValid)
						break;
					String username = parts[1];
					UsernameSetupMessage usernameSetupMessage = new UsernameSetupMessage(username);
					System.out.println(usernameSetupMessage.getUsername());
					break;

				default:
					System.out.println("Command not recognized: '" + command + "'");
					expectedFormat = null;
					break;
			}

			if (!isValid && expectedFormat != null) {
				System.out.println("Command not valid");
				System.out.println("Expected format is: " + expectedFormat);
			}

		}

		scanner.close();
	}
}
