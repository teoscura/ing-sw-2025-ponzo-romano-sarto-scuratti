package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.message.client.AskTurnOnMessage;
import it.polimi.ingsw.message.client.BrokenCabinMessage;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CardResponseType;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.iSpaceShip;

public class MeteorSwarmCard extends Card{

    private final ProjectileArray meteorites;

    public MeteorSwarmCard(int id, ProjectileArray meteorites){
        super(id, 0);
        this.meteorites = meteorites;
    }

    @Override
    public ClientMessage getRequest() {
        return new AskTurnOnMessage();
    }

    @Override
    public CardResponseType getResponse() {
        return CardResponseType.TURNON_ACCEPT;
    }

    @Override
    public CardResponseType getAfterResponse() {
        return this.after_response;
    }

    @Override
    public CardOrder getOrder() {
        return CardOrder.METEORS;
    }

    @Override
    public ClientMessage apply(ModelInstance model, iSpaceShip ship, PlayerResponse response){
        if(model==null||ship==null) throw new NullPointerException();
        this.after_response=CardResponseType.NONE;
        boolean broken_center_cabin = ship.handleMeteorite(this.meteorites.getProjectiles()[response.getId()]);
        if(broken_center_cabin) return new BrokenCabinMessage();
        return null;
    }

}

