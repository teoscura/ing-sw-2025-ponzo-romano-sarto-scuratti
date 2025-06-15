package it.polimi.ingsw.view.gui.tiles;

import it.polimi.ingsw.message.server.DiscardComponentMessage;
import it.polimi.ingsw.message.server.PutComponentMessage;
import it.polimi.ingsw.message.server.TakeDiscardedComponentMessage;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.view.gui.MainApplication;

public class ConstructionTile extends ComponentTile {
    
    private ComponentRotation rotation;

    public ConstructionTile(Integer ID, boolean discarded, boolean primary){
        super("galaxy_trucker_imgs/tiles/GT-tile-" + ID + ".jpg");

        this.setOnMouseDragReleased(event->{
            var node = event.getPickResult().getIntersectedNode();
            if(node==null || !(node instanceof PlacedTile)) return;
            var coords = ((PlacedTile)node).getCoords();
            MainApplication.getView().sendMessage(new PutComponentMessage(ID, coords, this.rotation));
        });

        this.setOnMouseClicked(event->{   
            if(discarded){
                MainApplication.getView().sendMessage(new TakeDiscardedComponentMessage(ID));
            }
            else if(event.getClickCount()==2){
                var new_shift = this.rotation.getShift() + 1;
                this.rotation = ComponentRotation.fromShift(new_shift%4);
            }
            else if(event.isSecondaryButtonDown() && primary){
                MainApplication.getView().sendMessage(new DiscardComponentMessage());
            }
        });

    }

}
