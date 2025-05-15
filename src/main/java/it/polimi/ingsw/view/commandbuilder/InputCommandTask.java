package it.polimi.ingsw.view.commandbuilder;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.message.server.ServerMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputCommandTask extends Thread {

	private final ConnectedState cc;

	public InputCommandTask(ConnectedState cc) {
		this.cc = cc;
	}

	public void run() {
		CommandBuilder cb = new CommandBuilder();
		while (true) {
			BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
			try {
				while (!r.ready()) {
				}
				String s = r.readLine();
				if (s.equals("exit")) break;
				ServerMessage mess = cb.build(s);
				if (mess == null) continue;
				System.out.println("Sent message!: " + mess.getClass().getSimpleName());
				cc.sendMessage(mess);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException | IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

}
