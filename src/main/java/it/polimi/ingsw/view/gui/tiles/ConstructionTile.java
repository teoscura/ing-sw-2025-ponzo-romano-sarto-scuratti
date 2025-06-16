package it.polimi.ingsw.view.gui.tiles;

import it.polimi.ingsw.message.server.DiscardComponentMessage;
import it.polimi.ingsw.message.server.PutComponentMessage;
import it.polimi.ingsw.message.server.TakeDiscardedComponentMessage;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.view.gui.GUIView;

public class ConstructionTile extends ComponentTile {
    
    private final Integer ID;
    private ComponentRotation rotation;

    public ConstructionTile(GUIView view, Integer ID, boolean discarded, boolean primary){
        super("galaxy_trucker_imgs/tiles/GT-tile-" + ID + ".jpg");
        this.ID = ID;

        this.setOnMouseClicked(event->{   
            if(discarded){
                view.sendMessage(new TakeDiscardedComponentMessage(ID));
            }
            else if(event.getClickCount()==2){
                var new_shift = this.rotation.getShift() + 1;
                this.rotation = ComponentRotation.fromShift(new_shift%4);
            }
            else if(event.isSecondaryButtonDown() && primary){
                view.sendMessage(new DiscardComponentMessage());
            }
        });

    }

    public Integer getID(){
        return this.ID;
    }

}
