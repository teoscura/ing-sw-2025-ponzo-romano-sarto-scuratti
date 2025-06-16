package it.polimi.ingsw.view.gui.tiles;

import it.polimi.ingsw.message.server.DiscardComponentMessage;
import it.polimi.ingsw.message.server.TakeDiscardedComponentMessage;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;

public class ConstructionTile extends ComponentTile {
    
    private final Integer ID;
    private ComponentRotation rotation;

    public ConstructionTile(GUIView view, Integer ID, boolean discarded, boolean primary){
        super("galaxy_trucker_imgs/tiles/GT-tile-" + ID + ".jpg");
        this.ID = ID;
        this.rotation = ComponentRotation.U000;

        this.setOnDragDetected(event -> {
            if(discarded) return;
            Dragboard db = this.startDragAndDrop(TransferMode.ANY);
            var cb = new ClipboardContent();
            cb.putString(this.toString());
            db.setContent(cb);
            event.consume();
            db.setDragView(this.image.getImage(), 20.0, 20.0);
            this.setVisible(false);
        });

        this.setOnDragDone(event->{
            if(event.isDropCompleted()) return;
            this.setVisible(true);
        });

        this.setOnMouseClicked(event->{   
            if(discarded && event.getClickCount()>1){
                view.sendMessage(new TakeDiscardedComponentMessage(ID));
            }
            else if(discarded) {}
            else if(event.getClickCount()>1 && event.getButton()==MouseButton.PRIMARY){
                var new_shift = this.rotation.getShift() + event.getClickCount()-1;
                this.rotation = ComponentRotation.fromShift(new_shift%4);
                this.image.setRotate(90*this.rotation.getShift());
            }
            else if(event.getButton()==MouseButton.SECONDARY) {
                view.sendMessage(new DiscardComponentMessage());
            }
        });

    }

    public ComponentRotation getRotation(){
        return this.rotation;
    }

    public Integer getID(){
        return this.ID;
    }

}
