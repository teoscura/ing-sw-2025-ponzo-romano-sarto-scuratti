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

setExpectedResponse\
getOrder\
getExhausted\










    

