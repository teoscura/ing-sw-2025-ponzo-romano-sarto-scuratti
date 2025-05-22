package it.polimi.ingsw.view.tui.formatters;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import it.polimi.ingsw.model.cards.utils.CombatZonePenalty;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.player.PlayerColor;

public class ClientCardStateFormatter implements ClientCardStateVisitor {

    private final AttributedStringBuilder line;

    public ClientCardStateFormatter(){
        this.line = new AttributedStringBuilder();
    }

    public AttributedString getFormatted(){
        return line.toAttributedString();
    }

    @Override
    public void show(ClientAwaitConfirmCardStateDecorator state) {
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
            .append("Awaiting confirm from: ");
        for(PlayerColor c : state.getAwaiting()){
            line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(getColor(c)))
            .append(c.toString())
            .style(AttributedStyle.DEFAULT)
            .append(" | ");
        }
    }

    @Override
    public void show(ClientBaseCardState state) {
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLUE).foreground(AttributedStyle.WHITE))
            .append("[Card id: "+state.getID()+" - "+state.getState()+"]")
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
            .append(String.format("%02d",state.getShipments()[0]))
            .append(" |")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append(String.format("%02d",state.getShipments()[1]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append(" |")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
            .append(String.format("%02d",state.getShipments()[2]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append(" |")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
            .append(String.format("%02d",state.getShipments()[3]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append(" |")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
            .append(String.format("%02d",state.getShipments()[4]))
            .style(AttributedStyle.DEFAULT)
            .append("] | ");
    }

    @Override
    public void show(ClientCargoRewardCardStateDecorator state) {
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append("Cargo reward: ")
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(this.getColor(state.getTurn())))
            .append(state.getTurn().toString())
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append(": [")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append(String.format("%02d",state.getShipments()[0]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append(" | ")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
            .append(String.format("%02d",state.getShipments()[1]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append(" | ")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
            .append(String.format("%02d",state.getShipments()[2]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append(" | ")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
            .append(String.format("%02d",state.getShipments()[3]))
            .style(AttributedStyle.DEFAULT)
            .append("] | ");
    }

    @Override
    public void show(ClientCombatZoneIndexCardStateDecorator state) {
        String sectioninfo = state.getSection().getPenalty()!=CombatZonePenalty.SHOTS ? state.getSection().getPenalty()+": "+state.getSection().getAmount() : state.getSection().getPenalty().toString();
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.RED))
            .append("CombatZone ["+state.getIndex()+"]: "+state.getSection().getCriteria()+"/"+sectioninfo+" ]")
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
            .append(" credits: "+state.getCredits()+" days: "+state.getDaysTaken()+"")
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
            .append(", missing: "+state.getCrewLost())
            .style(AttributedStyle.DEFAULT)
            .append(" | ");
    }

    @Override
    public void show(ClientLandingCardStateDecorator state) {
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.CYAN))
            .append("Landing available for ")
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(getColor(state.getTurn())))
            .append(state.getTurn().toString())
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.CYAN))
            .append(": ");
        int i = 0;
        for(Boolean p : state.getAvailable()){
            line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(p?AttributedStyle.GREEN:AttributedStyle.RED))
            .append(i+" - "+(p?"available":"not available"))
            .style(AttributedStyle.DEFAULT)
            .append(" | ");
            i++;
        }
    }

    @Override
    public void show(ClientMeteoriteCardStateDecorator state) {
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.BRIGHT))
            .append(state.getProjectile().getDimension()+" Meteor points: "+state.getProjectile().getDirection()+" "+normalizeOffset(state.getProjectile().getDirection().getShift(), state.getProjectile().getOffset()))
            .style(AttributedStyle.DEFAULT)
            .append(" | ");
    }

    @Override
    public void show(ClientNewCenterCardStateDecorator state) {
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.GREEN))
            .append("Need new center from: ");
        for(PlayerColor c : state.getAwaiting()){
            line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(getColor(c)))
            .append(c.toString())
            .style(AttributedStyle.DEFAULT)
            .append(" | ");
        }
    }

    @Override
    public void show(ClientProjectileCardStateDecorator state) {
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.MAGENTA))
            .append(state.getProjectile().getDimension()+" Projectile points: "+state.getProjectile().getDirection()+" "+normalizeOffset(state.getProjectile().getDirection().getShift(), state.getProjectile().getOffset()))
            .style(AttributedStyle.DEFAULT)
            .append(" | ");
    }

    private int getColor(PlayerColor color){
        switch(color){
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
