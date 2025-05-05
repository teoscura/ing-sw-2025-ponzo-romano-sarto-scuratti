package it.polimi.ingsw.controller.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;

public class LobbyController extends Thread {

    private final int id;

    private final HashMap<String, ClientDescriptor> listeners = new HashMap<>();
    private final HashMap<String, Player> disconnected_usernames = new HashMap<>();
    private final Queue<ServerMessage> queue = new ArrayDeque<>();
    private final Object queue_lock = new Object();

    private final String serializer_path;

    private final ModelInstance model;

    public LobbyController(int id, ModelInstance model){
        if(model == null) throw new NullPointerException();
        if(id<=0) throw new IllegalArgumentException();
        this.id = id;
        this.model = model;
        this.serializer_path = "gtunfinished-" + this.id + ".gtuf";
    }

    @Override
	public void run() {
		while (model.getState()!=null) {
			synchronized (queue_lock) {
				if (this.queue.isEmpty())
					try {
						queue_lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				while (!queue.isEmpty()) {
					ServerMessage message = this.queue.poll();
					try {
						message.receive(this);
					} catch (ForbiddenCallException e) {
						System.out.println("Client: '" + message.getDescriptor().getUsername() + "' attempted a forbidden command!");
						e.printStackTrace();
					}
				}
				queue_lock.notifyAll();
			}
		}
        this.endGame();
	}

    

    public ModelInstance getModel() throws ForbiddenCallException {
		return this.model;
	}

    public void serializeCurrentGame() {
		synchronized (queue_lock) {
			try (FileOutputStream file = new FileOutputStream(this.serializer_path);
				 ObjectOutputStream oos = new ObjectOutputStream(file)) {
				oos.reset();
				oos.writeObject(this.model);
				oos.reset();
			} catch (IOException e) {
				System.out.println("Failed to serialize the current modelinstance, closing server!");
				this.endGame();
			}
		}
	}

    private void endGame(){
        File f = new File(this.serializer_path);
        f.delete();
        MainServerController s = MainServerController.getInstance();
        for(var e : this.listeners.entrySet()){
            e.getValue().setID(-1);
        }
        for(var e : this.disconnected_usernames.keySet()){
            s.removeDisconnected(e);
        }
    }

}
