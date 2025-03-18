package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.iSpaceShip;

public class MeteorSwarmCard extends Card{

    private Projectile[] meteorites;

    public MeteorSwarmCard(Projectile[] meteorites, int id){
        super(id);
        this.meteorites = meteorites;
    }

    @Override
    public int apply(iSpaceShip state, iPlayerResponse response){
        //TODO

        return 0;
    }
    //tirfo dadi
    /*for(every_player){
        for (int i = 0; i < meteorites.size(); i++){
            cords = shipCords findComponentHit(player, tiro_dadi, meteorites[i].direction){
                //find cords, if no component return (0,0)
            }
            void hit(shipCords){
                if shipCords == (0,0) missed;
                getComponent();
                if (meteorites[i].size == SMALL){
                    if (side not connector or shield active or cannon active){
                        deflected;
                    }
                    else component_destroyed();
                }
                else if (meteorites[i].size == BIG){
                    if (cannon active){
                        deflected;
                    }
                    else component_destroyed();
                }
            }
        } 
    } */
}

