package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.model.cards.utils.CombatZonePenalty;
import it.polimi.ingsw.model.cards.utils.Planet;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.player.PlayerColor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class ClientCardStateFormatter implements ClientCardStateVisitor {

	private final AttributedStringBuilder line;

	public ClientCardStateFormatter() {
		this.line = new AttributedStringBuilder();
	}

	public AttributedString getFormatted() {
		return line.toAttributedString();
	}

	@Override
	public void show(ClientAwaitConfirmCardStateDecorator state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
				.append("Awaiting confirm from: ");
		for (PlayerColor c : state.getAwaiting()) {
			line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(getColor(c)))
					.append(c.toString())
					.style(AttributedStyle.DEFAULT)
					.append(" | ");
		}
	}

	@Override
	public void show(ClientBaseCardState state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLUE).foreground(AttributedStyle.WHITE))
				.append("[Card id: " + state.getID() + " - " + state.getState() + "]")
				.style(AttributedStyle.DEFAULT)
				.append(" | ");
	}

	@Override
	public void show(ClientCargoPenaltyCardStateDecorator state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
				.append("Cargo penalty: ")
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(this.getColor(state.getTurn())))
				.append(state.getTurn().toString())
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
				.append(": [")
				.append(String.format("%02d", state.getShipments()[0]))
				.append(" |")
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
				.append(String.format("%02d", state.getShipments()[1]))
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
				.append(" |")
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
				.append(String.format("%02d", state.getShipments()[2]))
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
				.append(" |")
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
				.append(String.format("%02d", state.getShipments()[3]))
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
				.append(" |")
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
				.append(String.format("%02d", state.getShipments()[4]))
				.style(AttributedStyle.DEFAULT)
				.append("] | ");
	}

	@Override
	public void show(ClientCargoRewardCardStateDecorator state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(getColor(state.getTurn())))
				.append(state.getTurn().toString())
				.style(AttributedStyle.DEFAULT)
				.append(" | ")
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
				.append("Cargo reward: ")
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(this.getColor(state.getTurn())))
				.append(state.getTurn().toString())
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
				.append(": [")
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
				.append(String.format("%02d", state.getShipments()[0]))
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
				.append(" | ")
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
				.append(String.format("%02d", state.getShipments()[1]))
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
				.append(" | ")
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
				.append(String.format("%02d", state.getShipments()[2]))
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
				.append(" | ")
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
				.append(String.format("%02d", state.getShipments()[3]))
				.style(AttributedStyle.DEFAULT)
				.append("] | ");
	}

	@Override
	public void show(ClientCombatZoneIndexCardStateDecorator state) {
		String sectioninfo = state.getSection().getPenalty() != CombatZonePenalty.SHOTS ? state.getSection().getPenalty() + ": " + state.getSection().getAmount() : state.getSection().getPenalty().toString();
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.RED))
				.append("[" + state.getIndex() + "]: " + state.getSection().getCriteria() + "/" + sectioninfo + " ]")
				.style(AttributedStyle.DEFAULT)
				.append(" | ");
	}

	@Override
	public void show(ClientCreditsRewardCardStateDecorator state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.YELLOW))
				.append("Credits reward for ")
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(this.getColor(state.getTurn())))
				.append(state.getTurn().toString())
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.YELLOW))
				.append(" credits: " + state.getCredits() + " days: " + state.getDaysTaken())
				.style(AttributedStyle.DEFAULT)
				.append(" | ");
	}

	@Override
	public void show(ClientCrewPenaltyCardStateDecorator state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.MAGENTA))
				.append("Crew penalty: ")
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(this.getColor(state.getTurn())))
				.append(state.getTurn().toString())
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.MAGENTA))
				.append(", missing: " + state.getCrewLost())
				.style(AttributedStyle.DEFAULT)
				.append(" | ");
	}

	@Override
	public void show(ClientLandingCardStateDecorator state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.CYAN))
				.append("Landing: ")
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(getColor(state.getTurn())))
				.append(state.getTurn().toString())
				.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.CYAN))
				.append(": ");
		if (state.getAvailable() == null) {
			line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.YELLOW))
					.append("0: credits: " + state.getCredits() + " days: " + state.getDaysTaken() + " crew: " + state.getCrewNeeded());
		} else {
			if (state.getCrewNeeded() > 0) {
				line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
						.append("0: [")
						.style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
						.append(String.format("%02d", state.getAvailable().get(0).getContains()[0]))
						.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
						.append("|")
						.style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
						.append(String.format("%02d", state.getAvailable().get(0).getContains()[1]))
						.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
						.append("|")
						.style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
						.append(String.format("%02d", state.getAvailable().get(0).getContains()[2]))
						.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
						.append("|")
						.style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
						.append(String.format("%02d", state.getAvailable().get(0).getContains()[3]))
						.style(AttributedStyle.DEFAULT)
						.append("]")
						.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
						.append(" Days: " + state.getDaysTaken() + " Crew: " + state.getCrewNeeded());
			} else {
				line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
						.append("Days: " + state.getDaysTaken() + " ");
				int i = 0;
				for (Planet p : state.getAvailable()) {
					if (p.getVisited()) {
						line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.RED))
								.append(i + ": N/A ")
								.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
								.append("| ");
					} else {
						line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
								.append(i + ": [")
								.style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
								.append(String.format("%02d", state.getAvailable().get(i).getContains()[0]))
								.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
								.append("|")
								.style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
								.append(String.format("%02d", state.getAvailable().get(i).getContains()[1]))
								.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
								.append("|")
								.style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
								.append(String.format("%02d", state.getAvailable().get(i).getContains()[2]))
								.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
								.append("|")
								.style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
								.append(String.format("%02d", state.getAvailable().get(i).getContains()[3]))
								.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
								.append("] ");
					}
					i++;
				}
			}
			line.style(AttributedStyle.DEFAULT);
		}
	}

	@Override
	public void show(ClientMeteoriteCardStateDecorator state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.BRIGHT))
				.append(state.getProjectile().getDimension() + " Meteor " + state.getProjectile().getDirection() + " " + normalizeOffset(state.getProjectile().getDirection().getShift(), state.getProjectile().getOffset()))
				.style(AttributedStyle.DEFAULT)
				.append(" | ");
	}

	@Override
	public void show(ClientNewCenterCardStateDecorator state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
				.append("Need new center from: ");
		for (PlayerColor c : state.getAwaiting()) {
			line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(getColor(c)))
					.append(c.toString())
					.style(AttributedStyle.DEFAULT)
					.append(" | ");
		}
	}

	@Override
	public void show(ClientProjectileCardStateDecorator state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.MAGENTA))
				.append(state.getProjectile().getDimension() + " Shot " + state.getProjectile().getDirection() + " " + normalizeOffset(state.getProjectile().getDirection().getShift(), state.getProjectile().getOffset()))
				.style(AttributedStyle.DEFAULT)
				.append(" | ");
	}

	@Override
	public void show(ClientEnemyCardStateDecorator state) {
		line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.RED))
				.append(" Enemy power: " + state.getPower() + " | Penalty: " + state.getPenalty() + "/" + (state.getAmount() > 0 ? state.getAmount() : ""))
				.style(AttributedStyle.DEFAULT)
				.append(" | ");
	}

	private int getColor(PlayerColor color) {
		switch (color) {
			case BLUE:
				return AttributedStyle.BLUE;
			case GREEN:
				return AttributedStyle.GREEN;
			case RED:
				return AttributedStyle.RED;
			case YELLOW:
				return AttributedStyle.YELLOW;
			default:
				return AttributedStyle.WHITE;
		}
	}

	private int normalizeOffset(int shift, int roll) {
		if (shift % 2 == 0) {
			if (roll < 4 || roll > 10) return -1;
			return roll - 4;
		}
		if (roll < 5 || roll > 9) return -1;
		return roll - 5;
	}


}
