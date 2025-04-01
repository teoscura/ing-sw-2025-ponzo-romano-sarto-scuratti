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

<!-- getResponse\
getAfterResponse\
getOrder\
getExhausted\

// public void action(){
//     try synchronized(this.cardlock){    
//         if(this.card==null){
//             this.model.getLead().getDescriptor().sendMessage(new AskPickCardMessage());
//             return;
//         }
//         //TODO: show all the card
//         if(this.card.getOrder()==COMBATZONE){
//             this.color = ALL;
//             this.expected = this.card.getResponse();
//             for(iSpaceShip s : this.model.getOrder(NORMAL)){
//                 s.getDescriptor().sendMessage(new AskTurnOnMessage());
//             }

//             while(this.accepted.length!=4) this.cardlock.wait();
//             for(CombatZoneLine c : ((CombatZoneCard)this.card).getCriteriaOrder()){
//                 iSpaceShip s = this.model.getTarget(c);
//                 PlayerResponse x = new PlayerResponse(c.getNumber());
//                 s.getDescriptor().sendMessage(this.card.apply(s, x));
//                 if(this.card.getAfterResponse!=CardResponseType.NONE){
//                     this.response = null;
//                     this.expected = this.card.getAfterResponse();
//                     while(this.response==null) this.cardlock.wait();
//                     //TODO if ship crew 0 lose the game
//                 }
//             }
//             for(iSpaceShip s : this.model.getOrder(NORMAL)){
//                 s.getDescriptor().sendMessage(new UpdateShipMessage(s));
//             }
//         }
//         else if(this.card.getOrder()==METEORSWARM){
//             int i = 0;
//             for(Projectile p : ((MeteorSwarmCard)this.card).getProjectiles()){
//                 this.color = ALL;
//                 //TODO show meteor message
//                 this.expected = this.card.getResponse();
//                 for(iSpaceShip s : this.model.getOrder(NORMAL)){
//                     s.getDescriptor().sendMessage(new AskTurnOnMessage());
//                 }
//                 while(!this.missing.empty()) this.cardlock.wait();
//                 List<PlayerColor> broken_cabin_players = new List();
//                 for(iSpaceShip s : this.model.getOrder(NORMAL)){
//                     PlayerResponse x = new PlayerResponse(i);
//                     ClientMessage tmp = this.card.apply(s, x);
//                     if(tmp!=null){
//                         s.getDescriptor().sendMessage(tmp);
//                         broken_cabin_players.add(s.getColor());
//                     }
//                 }
//                 if(!broken_cabin_players.empty()){
//                     this.waiting = broken_cabin_players;
//                     while(!this.missing.empty()) this.cardlock.wait();
//                 }
//                 for(iSpaceShip s : this.model.getOrder(NORMAL)){
//                     s.getDescriptor().sendMessage(new UpdateShipMessage(s));
//                 }
//                 i++;
//             }
//         }
//         else {
//             iSpaceShip[] tmp = this.model.getOrder(this.card.getOrder());
//             for(iSpaceShip s : tmp){
//                 if(this.card.getExpectedResponse()!=null){
//                     this.expected = this.card.getExpectedResponse();
//                     this.color = s.getColor();
//                     s.getDescriptor().sendMessage(this.card.getRequest());
//                     while(this.player_response==NULL) this.cardlock.wait();
//                 }
//                 ClientMessage card_response = this.card.apply(s, this.player_response);
//                 if(response!=null) s.getDescriptor().sendMessage(card_response);
//                 if(this.card.getAfterResponse()!=NULL){
//                     this.expected = this.card.getAfterResponse();
//                     this.player_response = null;
//                     s.getDescriptor().sendMessage(this.card.getAfterRequest(s.getColor()));
//                     while(!this.done) this.cardlock.wait();
//                 }
//                 s.getDescriptor().sendMessage(new UpdateShipMessage(s)); 
//                 this.player_response = null;
//                 if(this.card.getExhausted()){
//                     break;
//                 }
//             }
//             for(iSpaceShip s : this.model.getOrder(NORMAL)){
//                 //Send phase update.
//                 //Send continue request.
//             }
//             //TODO await for everyone to agree to go to next turn;
//             this.card = null;
//             this.color = NONE;
//             this.expected = NONE;
//         }
//     } catch (Expression e){
//         //TODO.
//     }
//     //TODO: ricordare di fare check di crew, se crew ==0 lost game.
// }



Stati carta:
    pesco e informo
    aspetto risposte richieste, nel fratte niente
    valido risposta, se valida muovo avanti
    appena finito applico carta, o vado al next stage della carta
    se ha una risposta dopo aspetta quella.





    

