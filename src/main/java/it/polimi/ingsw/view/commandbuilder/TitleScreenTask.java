package it.polimi.ingsw.view.commandbuilder;

import it.polimi.ingsw.controller.client.state.TitleScreenState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TitleScreenTask extends Thread {

	private final TitleScreenState state;

	public TitleScreenTask(TitleScreenState state) {
		this.state = state;
	}

	public void run() {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (!r.ready()) {
			}
			String s = r.readLine();
			state.setUsername(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}