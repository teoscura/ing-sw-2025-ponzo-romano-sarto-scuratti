//Done.

>PLANETS,   

    order: first to last
    offer avail planets
    get response from player, send him cargo, await answer
    go to next player if not exhausted


>ABANDONED_STATION,

    order first to last:
        offer ship if enough crew
        await response
        go to next if still avail

>ABANDONED_SHIP,

    offer first to last:
        offer ship if enough crew:
        await response 
        remove designated crew if good, if not skip
        go to next if still avail

>SMUGGLERS,

    order first to last:
        fight, if beat offer cargo
        else request cargo
        await response both cases
        go next if still avail

>OPEN_SPACE,

    order first to alst:
        move
        if 0 send lost
        go next;

>METEOR_SWARM,

    order: one at a time, at the same time
        show meteorite,
        await all continues, allow turn ons;
        hit all
        next meteorite.

>COMBAT_ZONE,

    order: one per criteria, same criteria hits first in line between ties.
        show card,
        allow turn ons
        await continues
        apply all.

>STARDUST,

    order: inverse, last to first;
        count exposed,
        remove days
        go next

>SLAVERS,

    order first to last:
        fight, if beat give money
        else request crew
        await response both cases
        go next if still avail
 

>PIRATES,

    order first to last:
        fight, if beat offer cargo
        else shots
        await response if won
        go next if still avail


>EPIDEMIC,

    order: first to last
        apply
        go next.

>SABOTAGE

    ask prof if present.

EACH CARD GOT:

getResponse\
getAfterResponse\
getOrder\
getExhausted\

public void action(){
    try synchronized(this.cardlock){    
        if(this.card==null){
            this.model.getLead().getDescriptor().sendMessage(new AskPickCardMessage());
            return;
        }
        if(this.card.getOrder()==COMBATZONE){
            this.color = ALL;
            this.expected = this.card.getResponse();
            for(iSpaceShip s : this.model.getOrder(NORMAL)){
                s.getDescriptor().sendMessage(new AskTurnOnMessage());
            }
            while(this.accepted.length!=4) this.cardlock.wait();
            int i = 0;
            for(iSpaceShip s : this.model.getCriteriaOrder(((CombatZoneCard)this.card).getCriteriaOrder())){
                PlayerResponse x = new PlayerResponse(i);
                s.getDescriptor().sendMessage(this.card.apply(s, x));
                i++;
            }
            for(iSpaceShip s : this.model.getOrder(NORMAL)){
                s.getDescriptor().sendMessage(new UpdateShipMessage(s));
            }
            this.card = null;
            this.color = NONE;
            this.expected = NONE;
        }
        else if(this.card.getOrder()==METEORSWARM){
            int i = 0;
            for(Projectile p : ((MeteorSwarmCard)this.card).getProjectiles()){
                this.color = ALL;
                this.expected = this.card.getResponse();
                for(iSpaceShip s : this.model.getOrder(NORMAL)){
                    s.getDescriptor().sendMessage(new AskTurnOnMessage());
                }
                while(!this.missing.empty()) this.cardlock.wait();
                for(iSpaceShip s : this.model.getOrder(NORMAL)){
                    PlayerResponse x = new PlayerResponse(i);
                    s.getDescriptor().sendMessage(this.card.apply(s, x));
                }
                for(iSpaceShip s : this.model.getOrder(NORMAL)){
                    s.getDescriptor().sendMessage(new UpdateShipMessage(s));
                }
                i++;
            }
            this.card = null;
            this.color = NONE;
            this.expected = NONE;
        }
        else {
            iSpaceShip[] tmp = this.model.getOrder(this.card.getOrder());
            for(iSpaceShip s : tmp){
                if(this.card.getExpectedResponse()!=NULL){
                    this.expected = this.card.getExpectedResponse();
                    this.color = s.getColor();
                    s.getDescriptor().sendMessage(this.card.getRequest());
                    while(this.response==NULL) this.cardlock.wait();
                }
                s.getDescriptor().sendMessage(this.card.apply(s, this.response));
                if(this.card.getAfterResponse()!=NULL){
                    this.expected = this.card.getAfterResponse();
                    this.response = null;
                    s.getDescriptor().sendMessage(this.card.getAfterRequest());
                    while(!this.done) this.cardlock.wait();
                }
                s.getDescriptor().sendMessage(new UpdateShipMessage(s)); 
                this.response = null;
                if(this.card.getExhausted()){
                    this.card = null;
                    this.color = NONE;
                    this.expected = NONE;
                    return;
                }
            }
            this.card = null;
            this.color = NONE;
            this.expected = NONE;
        }
    } catch (Expression e){
        //TODO.
    }
}









    

