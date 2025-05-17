package it.polimi.ingsw.view.tui.utils;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.player.PlayerColor;

public class ClientVoyageStateFormatter implements ClientCardStateVisitor {

    private final AttributedStringBuilder line;

    public ClientVoyageStateFormatter(){
        this.line = new AttributedStringBuilder();
    }

    public AttributedString getFormatted(){
        return line.toAttributedString();
    }

    @Override
    public void show(ClientAwaitConfirmCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
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
            .append("[Cargo penalty ")
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(this.getColor(state.getTurn())))
            .append(state.getTurn().toString())
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append(": [")
            .append(String.format("%02d",state.getShipments()[0]))
            .append("|")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append(String.format("%02d",state.getShipments()[1]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append("|")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append(String.format("%02d",state.getShipments()[2]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append("|")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append(String.format("%02d",state.getShipments()[3]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append("|")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append(String.format("%02d",state.getShipments()[4]))
            .style(AttributedStyle.DEFAULT)
            .append("]] | ");
    }

    @Override
    public void show(ClientCargoRewardCardStateDecorator state) {
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append("[Cargo penalty ")
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(this.getColor(state.getTurn())))
            .append(state.getTurn().toString())
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append(": [")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append(String.format("%02d",state.getShipments()[0]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append("|")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append(String.format("%02d",state.getShipments()[1]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append("|")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append(String.format("%02d",state.getShipments()[2]))
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.WHITE))
            .append("|")
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append(String.format("%02d",state.getShipments()[3]))
            .style(AttributedStyle.DEFAULT)
            .append(" | ");
    }

    @Override
    public void show(ClientCombatZoneIndexCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientCreditsRewardCardStateDecorator state) {
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.YELLOW))
            .append("[Credits reward ")
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(this.getColor(state.getTurn())))
            .append(state.getTurn().toString())
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.YELLOW))
            .append(" credits: "+state.getCredits()+" days:"+state.getDaysTaken()+"]")
            .style(AttributedStyle.DEFAULT)
            .append(" | ");
    }

    @Override
    public void show(ClientCrewPenaltyCardStateDecorator state) {
        line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.MAGENTA))
            .append("[Crew penalty ")
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(this.getColor(state.getTurn())))
            .append(state.getTurn().toString())
            .style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.MAGENTA))
            .append(" missing: "+state.getCrewLost()+"]")
            .style(AttributedStyle.DEFAULT)
            .append(" | ");
    }

    @Override
    public void show(ClientLandingCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientMeteoriteCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientNewCenterCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientProjectileCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
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
    
}
