package it.polimi.ingsw.model.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;

public class Temp {
    
    private GameModeType type;
    private ArrayList<ArrayList<ShipCoords>> blobs;

    public int getBlobsSize(){
        this.updateShipBlobs();
        return this.blobs.size();
    }

    public void updateShipBlobs(){
        ArrayList<ArrayList<ShipCoords>> res = new ArrayList<>();
        VerifyResult[][] map = new VerifyResult[this.type.getHeight()][this.type.getWidth()];
        for(int y = 0; y<type.getHeight(); y++){
            for(int x = 0; x<type.getWidth(); x++){
                map[y][x] = this.components[y][x] == this.empty ? VerifyResult.UNCHECKED : VerifyResult.NOT_LNKED;
            }
        }
        for(int y = 0; y<type.getHeight(); y++){
            for(int x = 0; x<type.getWidth(); x++){
                if(map[y][x]!=VerifyResult.NOT_LNKED) continue;
                res.add(this.verifyBlob(map, new ShipCoords(this.type, x, y)));
            }
        }
        this.blobs = res;
    }

	private ArrayList<ShipCoords> verifyBlob(VerifyResult[][] map, ShipCoords starting_point) {
        ArrayList<ShipCoords> res = new ArrayList<>();
		Queue<ShipCoords> queue = new ArrayDeque<>();
		queue.add(starting_point);
		ShipCoords tmp = null;
		while (!queue.isEmpty()) {
			tmp = queue.poll();
            res.add(tmp);
			if (!this.getComponent(tmp).verify(this)) map[tmp.y][tmp.x] = VerifyResult.BRKN_COMP;
			else map[tmp.y][tmp.x] = VerifyResult.GOOD_COMP;
			for (iBaseComponent c : this.getComponent(tmp).getConnectedComponents(this)) {
				if (c == this.empty) continue;
				ShipCoords xy = c.getCoords();
				if (map[xy.y][xy.x] == VerifyResult.NOT_LNKED) queue.add(c);
			}
		}
        return res;
	}

    public void selectShipBlob(ShipCoords blob_coord){
        for(ArrayList<ShipCoords> blob : this.blobs){
            if(!blob.contains(blob_coord)) continue;
            ArrayList<ArrayList<ShipCoords>> previous = this.blobs;
            previous.remove(blob);
            previous.stream().forEach(b->b.stream().forEach(c->this.removeComponent(c)));
            this.blobs = new ArrayList<>(){{add(blob);}};
            return;
        }
        throw new IllegalTargetException("Blob coordinate was invalid!");
    }


}
