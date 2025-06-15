package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.server.connections.*;
import it.polimi.ingsw.message.client.ClientDisconnectMessage;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import it.polimi.ingsw.model.client.state.ClientSetupState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.NotYetConnectedException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Singleton class in charge of managing the opening and closing of lobbies, and the reception of each message to the respective lobby.
 */
public class MainServerController extends Thread implements VirtualServer {

	static private MainServerController instance = new MainServerController();

	private final NetworkServer server;
	private final HashMap<String, ClientDescriptor> all_listeners;
	private final HashMap<String, ClientDescriptor> lob_listeners;
	private final HashMap<String, ClientDescriptor> stp_listeners;
	private final HashMap<String, Integer> disconnected;
	private final List<SocketClient> to_setup_tcp;
	private final Object listeners_lock;
	private final ThreadSafeMessageQueue<ServerMessage> queue;
	private final Object lobbies_lock;
	private final Object saved_lock;
	private final HashMap<Integer, LobbyController> lobbies;
	private final HashMap<Integer, ModelInstance> saved;
	private boolean init = false;
	private int next_id;


	private MainServerController() {
		all_listeners = new HashMap<>();
		lob_listeners = new HashMap<>();
		stp_listeners = new HashMap<>();
		disconnected = new HashMap<>();
		to_setup_tcp = new ArrayList<>();
		listeners_lock = new Object();
		queue = new ThreadSafeMessageQueue<>(1000);
		lobbies = new HashMap<>();
		lobbies_lock = new Object();
		saved = new HashMap<>();
		saved_lock = new Object();
		this.updateUnfinishedList();
		this.next_id = this.saved.keySet().stream().max(Integer::compare).orElse(0) + 1;
		server = new NetworkServer();
	}

	/**
     * Main access method for the singleton object.
	 * 
	 * @return {@link MainServerController} Instance of the singleton, if not present, constructs a new one.
     */
	static public MainServerController getInstance() {
		if (instance == null) instance = new MainServerController();
		return instance;
	}

	/**
     * Resets the singleton, mostly used during tests.
     */
	static public void reset() {
		Logger.getInstance().print(LoggerLevel.DEBUG, "Reset MainServerController.");
		instance = null;
	}

	/**
	 * Initialize the MainServerController and the NetworkServer tied to it.
	 *
	 * @param address IP address on to which the server is opened.
	 * @param tcpport Port used to open the ServerSocket.
	 * @param rmiport Port used to open the RMI Registry.
	 */
	public void init(String address, int tcpport, int rmiport) {
		if (this.init) throw new AlreadyConnectedException();
		this.server.init(address, tcpport, rmiport);
		this.init = true;
	}

	// -------------------------------------------------------------
	// Message handling, reception and sending.
	// -------------------------------------------------------------

	/**
     * Main loop of the {@code MainServerController} class, retrieves messages from the queue and processes them.
     */
	@Override
	public void run() {
		if (!this.init) throw new NotYetConnectedException();
		this.server.start();
		while (true) {
			try {
				queue.take().receive(this);
			} catch (ForbiddenCallException e) {
				Logger.getInstance().print(LoggerLevel.WARN, e.getMessage());
			} catch (InterruptedException e) {
				Logger.getInstance().print(LoggerLevel.NOTIF, "Shutting down server.");
			}
		}
	}

	/**
     * Takes a {@code ServerMessage} and inserts it into the correct queue, either the lobby controller's or the main server controller's.
     *
     * @param message {@link it.polimi.ingsw.message.server.ServerMessage} Message to be added to the queue.
     */
	public void receiveMessage(ServerMessage message) {
		if (message.getDescriptor() == null) {
			Logger.getInstance().print(LoggerLevel.WARN, "Received a message from a Client: not properly connected! (Null descriptor)");
			return;
		}
		synchronized (listeners_lock) {
			if (!this.all_listeners.containsKey(message.getDescriptor().getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Received a message from a Client: not properly connected! (Player not connected)");
				return;
			}
		}
		if (message.getDescriptor().getId() == -1) {
			Logger.getInstance().print(LoggerLevel.DEBUG, "Received message from Client: '" + message.getDescriptor().getUsername() + "' of class: " + message.getClass().getSimpleName() + ".");
			this.queue.insert(message);
		}
		var target = this.lobbies.get(message.getDescriptor().getId());
		if (target == null) {
			message.getDescriptor().setID(-1);
		} else {
			target.receiveMessage(message);
		}
	}

	/**
     * Takes a {@code ClientMessage} and sends it to each connected listener not currently playing in a lobby.
     *
     * @param message {@link ClientMessage} Message to be broadcast.
     */
	public void broadcast(ClientMessage message) {
		synchronized (listeners_lock) {
			Logger.getInstance().print(LoggerLevel.DEBUG, "Broadcasting message of class: " + message.getClass().getSimpleName() + ".");
			for (ClientDescriptor c : this.lob_listeners.values()) {
				if (stp_listeners.containsKey(c.getUsername())) continue;
				this.sendMessage(c, message);
			}
		}
	}

	/**
     * Takes a {@code ClientMessage} and sends it to a specific client.
     *
     * @param message {@link ClientMessage} Message to be sent.
     */
	public void sendMessage(ClientDescriptor client, ClientMessage message) {
		try {
			Logger.getInstance().print(LoggerLevel.DEBUG, "Sent message to Client: '" + client.getUsername() + "'.");
			client.sendMessage(message);
		} catch (IOException e) {
			this.disconnect(client);
		}
	}

	/**
     * Returns the {@code ClientDescriptor} associated to a username.
     *
     * @param username Name corresponding to the client to fetch.
	 * @return {@link it.polimi.ingsw.controller.server.ClientDescriptor} Corresponding to the username, if present, if not returns null.
     */
	public ClientDescriptor getDescriptor(String username) {
		return this.all_listeners.get(username);
	}

	// -------------------------------------------------------------
	// ClientDescriptor connection and disconnection methods.
	// -------------------------------------------------------------

	/**
     * Connects a {@code SocketClient} to the server, and adds it to the list of sockets that need to complete their connection, starts a timeout timer in case they don't complete it in time.
     *
     * @param client {@link SocketClient} Socket connecting to the server
     */
	public void connectListener(SocketClient client) {
		synchronized (listeners_lock) {
			if (this.to_setup_tcp.contains(client)) {
				Logger.getInstance().print(LoggerLevel.WARN, "TCP connection: '" + client.getSocket().getInetAddress() + "'' attempted to connect twice!");
				return;
			}
			Logger.getInstance().print(LoggerLevel.LOBSL, "TCP connection: '" + client.getSocket().getInetAddress() + "'' connected, awaiting username.");
			this.to_setup_tcp.add(client);
			TimerTask task = this.TCPTimeoutTask(instance, client);
			Timer t = new Timer(true);
			client.setTimeout(task);
			t.schedule(task, 20000L);
		}
	}

	/**
     * Finalizes the connection of a {@code SocketClient} to the server by setting its username.
	 * 
     * @param client {@link SocketClient} Socket connecting to the server
	 * @param username Username the client wishes to connect with.
     */
	public void setupSocketListener(SocketClient client, String username) {
		if (!this.to_setup_tcp.contains(client)) {
			Logger.getInstance().print(LoggerLevel.WARN, "A Client: attempted to change his username after connecting!");
			return;
		}
		this.to_setup_tcp.remove(client);
		client.cancelTimeout();
		if (!this.validateUsername(username)) {
			Logger.getInstance().print(LoggerLevel.WARN, "A Client: attempted to connect with an invalid name!");
			return;
		}
		synchronized (listeners_lock) {
			if (this.all_listeners.containsKey(username)) {
				try {
					client.sendMessage(new ClientDisconnectMessage());
				} catch (IOException e) {
				} finally {
					client.close();
				}
			}
		}
		Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + username + "' connected from '" + client.getSocket().getInetAddress() + "'.");
		ClientDescriptor new_listener = new ClientDescriptor(username, client);
		try {
			this.connect(new_listener);
		} catch (ForbiddenCallException e) {
			Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + username + "' failed to connect!");
			return;
		}
		new_listener.setPingTimerTask(this.timeoutTask(this, new_listener));
	}

	/**
     * Connects an {@code RMIClientConnection}  to the server
     *
     * @param client {@link RMIClientConnection} Client connecting to server.
	 * @return {@link it.polimi.ingsw.controller.server.ClientDescriptor} A ClientDescriptor tied to the RMI connection requesting it. 
	 * @throws RemoteException
     */
	public ClientDescriptor connectListener(RMIClientConnection client) throws RemoteException {
		String name;
		try {
			name = client.getUsername();
		} catch (RemoteException e) {
			throw new RemoteException("Failed to retrieve username from stub");
		}
		ClientDescriptor new_listener = new ClientDescriptor(name, client);
		synchronized (listeners_lock) {
			if (this.all_listeners.containsKey(name) || !validateUsername(name)){
				Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + client.getUsername() + "' attemped to connect with an invalid/taken name.");
				return null;
			}
			try {
				Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + client.getUsername() + "' connected with RMI.");
				this.connect(new_listener);
			} catch (ForbiddenCallException e) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + name + "' failed to connect properly!");
				return null;
			}
			new_listener.setPingTimerTask(this.timeoutTask(this, new_listener));
			return new_listener;
		}
	}

	/**
     * Returns a {@code RMIServerStubImpl} linked to a {@code ClientDescriptor} for the {@code RMIClientConnection} to send messages with.
     *
     * @param new_client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client requesting the stub.
	 * @return {@link VirtualServer} A stub linked specifically to the client for it to communicate with the server.
	 * @throws RemoteException
     */
	public VirtualServer getStub(ClientDescriptor new_client) throws RemoteException {
		return new RMIServerStubImpl(this, new_client);
	}

	/**
     * Validates a username using a regex.
	 * 
	 * @return Whether the username is valid or not.
     */
	private boolean validateUsername(String username) {
		Pattern allowed = Pattern.compile("^[a-zA-Z0-9_.-]*$");
		Matcher matcher = allowed.matcher(username);
		return matcher.matches();
	}

	/**
     * Connects a {@code ClientDescriptor} to the lobby and adds it to any list it may belong to, or reconnects it to a ongoing game in case they belonged to it.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client requesting the connection.
	 * @throws ForbiddenCallException if for any reason connecting is forbidden
     */
	public void connect(ClientDescriptor client) throws ForbiddenCallException {
		//Either add him to a game if hes reconnecting, or add
		int id = -1;
		boolean disconnected = false;
		synchronized (listeners_lock) {
			if (all_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' tried to connect twice!");
				throw new ForbiddenCallException();
			}
			this.all_listeners.put(client.getUsername(), client);
			if (this.disconnected.containsKey(client.getUsername())) {
				id = this.disconnected.get(client.getUsername());
				client.setID(id);
				this.disconnected.remove(client.getUsername());
				disconnected = true;
			} else {
				this.lob_listeners.put(client.getUsername(), client);
				client.setID(-1);
				Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + client.getUsername() + "' finished connecting.");
				this.sendMessage(client, new NotifyStateUpdateMessage(new ClientLobbySelectState(this.getLobbyList())));
			}
		}
		if (disconnected) {
			synchronized (lobbies_lock) {
				if (this.lobbies.get(id) == null) {
					client.setID(-1);
					Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + client.getUsername() + "' tried reconnecting to lobby: [" + id + "] but it had closed, sending him to lobby select.");
					return;
				}
				this.lobbies.get(id).connect(client);
				Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + client.getUsername() + "' reconnected to lobby: [" + id + "].");
			}
		}
	}

	/**
     * Disconnects a {@code ClientDescriptor} from the server and closes its connection, if the {@code ClientDescriptor} is connected to a lobby then the lobby handles whatever procedure may be needed.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client being disconnected.
     */
	public void disconnect(ClientDescriptor client) {
		int id = client.getId();
		if (client.getPingTimerTask() != null) client.getPingTimerTask().cancel();
		synchronized (listeners_lock) {
			if (!all_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' disconnected, but was never connected!");
				return;
			}
			Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + client.getUsername() + "' disconnected.");
			this.all_listeners.remove(client.getUsername());
			this.stp_listeners.remove(client.getUsername());
			this.lob_listeners.remove(client.getUsername());
			if (id == -1) {
				this.lob_listeners.remove(client.getUsername());
			}
		}
		try {
			client.getConnection().close();
		} catch (IOException e) {
			Logger.getInstance().print(LoggerLevel.DEBUG, "Client: '" + client.getUsername() + "' connection closed.");
		}
		if (id == -1) return;
		synchronized (lobbies_lock) {
			var l = this.lobbies.get(id);
			if (l == null) return;
			l.disconnectProcedure(client);
		}
	}

	/**
     * Adds an entry into the disconnected list for disconnection resilience purposes.
	 * 
     * @param username Name of the client disconnecting
	 * @param id Id of the lobby they were in when playing.
     */
	public void addDisconnected(String username, int id) {
		synchronized (listeners_lock) {
			Logger.getInstance().print(LoggerLevel.DEBUG, "Added username: '" + username + "' to disconnected list with id: [" + id + "].");
			this.disconnected.put(username, id);
		}
	}

	/**
     * Removes an entry from the disconnected list, either because of a reconnect or because of the game they belonged to closing.
	 * 
     * @param username Name of the client disconnecting
     */
	public void removeDisconnected(String username) {
		synchronized (listeners_lock) {
			Logger.getInstance().print(LoggerLevel.DEBUG, "Removed username: '" + username + "' to disconnected list, had id: [" + disconnected.get(username) + "].");
			this.disconnected.remove(username);
		}
	}

	// -------------------------------------------------------------
	// Disconnection Resilience utility.
	// -------------------------------------------------------------

	/**
     * Refreshes the timeout timer for a particular {@code ClientDescriptor} and restarts the timeout clock belonging to it.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client pinging the server
     */
	public void ping(ClientDescriptor client) {
		client.getPingTimerTask().cancel();
		Logger.getInstance().print(LoggerLevel.DEBUG, "Received ping from client: '" + client.getUsername() + "'.");
		client.setPingTimerTask(this.timeoutTask(this, client));
	}

	/**
     * Creates a timeout task for a {@code SocketClient} in its setup state, closing the connection and removing them from any list in case they don't setup in time.
	 * 
	 * @param controller {@link MainServerController} Instance of the server to disconnect the client from.
	 * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client to be eventually timed out.
     */
	private TimerTask TCPTimeoutTask(MainServerController controller, SocketClient client) {
		return new TimerTask() {
			public void run() {
				synchronized (listeners_lock) {
					Logger.getInstance().print(LoggerLevel.NOTIF, "Socket '" + client.getSocket().getInetAddress() + "' didn't setup before timing out!");
					to_setup_tcp.remove(client);
					client.close();
				}
			}
		};
	}

	/**
     * Creates a timeout task for any {@code ClientDescriptor}, if the {@code ClientDescriptor} doesn't send a ping before it expires, task runs and disconnects the {@code ClientDescriptor}.
	 * 
	 * @param controller {@link MainServerController} Instance of the server to disconnect the client from.
	 * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client to be eventually timed out.
     */
	private TimerTask timeoutTask(MainServerController controller, ClientDescriptor client) {
		return new TimerTask() {
			public void run() {
				synchronized (listeners_lock) {
					Logger.getInstance().print(LoggerLevel.NOTIF, "Client: '" + client.getUsername() + "' failed to ping in between timeout!");
					controller.disconnect(client);
				}
			}
		};
	}

	// -------------------------------------------------------------
	// Games management utilities and methods.
	// -------------------------------------------------------------

	public int getNext() {
		return this.next_id;
	}

	/**
     * Updates the list of {@code ModelInstance} available for loading during creation of a lobby.
     */
	public void updateUnfinishedList() {
		Pattern saved_game_pattern = Pattern.compile("^gtunfinished-[0-9]+\\.gtuf$");
		File current_directory = new File(".");
		File[] files = current_directory.listFiles();
		if (files == null) throw new NullPointerException();
		ArrayList<File> regex_pass = new ArrayList<>();
		for (File f : files) {
			Matcher matcher = saved_game_pattern.matcher(f.getName());
			if (!matcher.matches()) continue;
			regex_pass.add(f);
		}
		ArrayList<ModelInstance> saved_tmp = new ArrayList<>();
		for (File f : regex_pass) {
			try (FileInputStream fis = new FileInputStream(f.getAbsolutePath());
				 ObjectInputStream ois = new ObjectInputStream(fis)) {
				ModelInstance loaded = (ModelInstance) ois.readObject();
				if (this.lobbies.get(loaded.getID()) != null) continue;
				saved_tmp.add(loaded);
			} catch (IOException e) {
				Logger.getInstance().print(LoggerLevel.NOTIF, "Read error during loading of File: '" + f.getName() + ", cleaning it up.");
				f.delete();
				Logger.getInstance().print(LoggerLevel.DEBUG, "Deleted file: '" + f.getName() + "'.");
			} catch (ClassNotFoundException e) {
				Logger.getInstance().print(LoggerLevel.NOTIF, "File: '" + f.getName() + "' is not a valid savefile, cleaning it up.");
				f.delete();
				Logger.getInstance().print(LoggerLevel.DEBUG, "Deleted file: '" + f.getName() + "'.");
			}
		}
		synchronized (lobbies_lock) {
			for (ModelInstance m : saved_tmp) {
				if (this.lobbies.get(m.getID()) != null) saved_tmp.remove(m);
			}
		}
		synchronized (saved_lock) {
			this.saved.clear();
			for (ModelInstance m : saved_tmp) {
				this.saved.put(m.getID(), m);
			}
		}
	}

	/**
     * Enters lobby setup state for a {@code ClientDescriptor} requesting it.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client requesting to enter the lobby creation and setup state.
     */
	public void enterSetup(ClientDescriptor client) {
		synchronized (listeners_lock) {
			if (!lob_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' started setting up a lobby, but he's already playing!");
				return;
			} else if (stp_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' started setting up a lobby, but he's already doing that!");
				return;
			}
			this.stp_listeners.put(client.getUsername(), client);
		}
		ClientSetupState state = null;
		ArrayList<ClientGameListEntry> tmp = new ArrayList<>();
		synchronized (saved_lock) {
			for (var m : this.saved.values()) {
				tmp.add(m.getEntry());
			}
		}
		state = new ClientSetupState(client.getUsername(), tmp);
		Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + client.getUsername() + "' has entered setup state.");
		this.sendMessage(client, new NotifyStateUpdateMessage(state));
	}

	/**
     * Leaves lobby setup state for a {@code ClientDescriptor} requesting it.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client requesting to leave the lobby creation and setup state.
     */
	public void leaveSetup(ClientDescriptor client) {
		synchronized (listeners_lock) {
			if (!lob_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' tried to leave setup, but he's already playing!");
				return;
			} else if (!stp_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' tried to leave setup, but he wasn't never setupping!");
				return;
			}
			this.stp_listeners.remove(client.getUsername());
		}
		Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + client.getUsername() + "' has left setup state.");
		this.notifyLobbyListeners();
	}

	/**
     * Opens a room with given type and size for a {@code ClientDescriptor} in setup state requesting it.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client requesting to open the lobby.
	 * @param type {@link GameModeType} Gamemode of the lobby being opened.
	 * @param count {@link it.polimi.ingsw.model.PlayerCount} Size of the lobby being opened.
     */
	public void openNewRoom(ClientDescriptor client, GameModeType type, PlayerCount count) throws ForbiddenCallException {
		synchronized (listeners_lock) {
			if (!this.stp_listeners.containsKey(client.getUsername()) && this.lob_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' tried opening a lobby, but he's not setupping!");
				return;
			}
			if (!this.lob_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' tried opening a lobby, but he's already playing!");
				return;
			}
			this.stp_listeners.remove(client.getUsername());
			this.lob_listeners.remove(client.getUsername());
		}
		Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + client.getUsername() + "' opened a new lobby: [Type: " + type + " | Size: " + count + "].");
		LobbyController new_lobby = new LobbyController(this.next_id);
		ModelInstance model = new ModelInstance(this.next_id, new_lobby, type, count);
		new_lobby.setModel(model);
		new_lobby.start();
		synchronized (lobbies_lock) {
			Logger.getInstance().print(LoggerLevel.DEBUG, "Added lobby [" + new_lobby.getID() + "] to lobbies list.");
			this.lobbies.put(this.next_id, new_lobby);
			this.next_id++;
		}
		client.setID(new_lobby.getID());
		new_lobby.connect(client);
		this.notifyLobbyListeners();
	}

	/**
     * Opens a room tied to an unfinished game for a {@code ClientDescriptor} that requests it.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client requesting to open the lobby.
	 * @param id ID of the saved game being opened.
	 * @throws ForbiddenCallException when the lobby is being opened in a forbidden or unsupported way.
     */
	public void openUnfinished(ClientDescriptor client, int id) throws ForbiddenCallException {
		synchronized (listeners_lock) {
			if (!this.stp_listeners.containsKey(client.getUsername()) && this.lob_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' tried opening a lobby, but he's not setupping!");
				return;
			}
			if (!this.lob_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' tried opening a lobby, but he's already playing!");
				return;
			}
			this.stp_listeners.remove(client.getUsername());
			this.lob_listeners.remove(client.getUsername());
		}
		ModelInstance loaded = null;
		boolean reset = false;
		synchronized (saved_lock) {
			if (!saved.containsKey(id)) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' attempted to open a non-existant saved game!");
				reset = true;
			}
		}
		if (reset) {
			synchronized (listeners_lock) {
				this.stp_listeners.remove(client.getUsername());
				this.lob_listeners.put(client.getUsername(), client);
			}
			this.sendMessage(client, new NotifyStateUpdateMessage(new ClientLobbySelectState(this.getLobbyList())));
			return;
		}
		reset = false;
		synchronized (saved_lock) {
			Logger.getInstance().print(LoggerLevel.DEBUG, "Opening unfinished lobby [" + id + "].");
			loaded = this.saved.get(id);
			if (!loaded.getEntry().getPlayers().contains(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.NOTIF, "Client: '" + client.getUsername() + "' attempted to resume a game, but he wasn't playing in it before!");
				reset = true;
			}
		}
		if (reset) {
			synchronized (listeners_lock) {
				this.stp_listeners.remove(client.getUsername());
				this.lob_listeners.put(client.getUsername(), client);
			}
			this.sendMessage(client, new NotifyStateUpdateMessage(new ClientLobbySelectState(this.getLobbyList())));
			return;
		}
		this.updateUnfinishedList();
		Logger.getInstance().print(LoggerLevel.LOBSL, "Client: '" + client.getUsername() + "' opened a new lobby from unfinished: [Type: " + loaded.getState().getType() + " | Size: " + loaded.getState().getCount() + "].");
		LobbyController new_lobby = new LobbyController(id);
		loaded.setController(new_lobby);
		loaded.afterSerialRestart();
		new_lobby.setModel(loaded);
		new_lobby.start();
		synchronized (lobbies_lock) {
			Logger.getInstance().print(LoggerLevel.DEBUG, "Added lobby [" + new_lobby.getID() + "] to lobbies list.");
			this.lobbies.put(new_lobby.getID(), new_lobby);
		}
		client.setID(new_lobby.getID());
		new_lobby.connect(client);
		this.notifyLobbyListeners();
	}

	/**
     * Cleans up the lobby controller and all tied objects when a lobby closes.
     *
	 * @param id ID of the lobby being closed.
     */
	public void lobbyCloseCleanup(int id) {
		synchronized (lobbies_lock) {
			var l = this.lobbies.get(id);
			if (l == null) throw new RuntimeException();
			this.lobbies.remove(id);
			try {
				l.interrupt();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			Logger.getInstance().print(LoggerLevel.LOBSL, "Game [" + id + "] ended, closing its controller!");
		}
		this.updateUnfinishedList();
		this.notifyLobbyListeners();
	}

	/**
     * Joins a lobby for a {@code ClientDescriptor} requesting it.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client requesting to join the lobby.
	 * @param id ID of the lobby being joined
     */
	public void connectToLobby(ClientDescriptor client, int id) throws ForbiddenCallException {
		synchronized (listeners_lock) {
			if (!all_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' attempted to join a lobby, but was never connected!");
				return;
			} else if (client.getId() != -1) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' attempted to join a lobby, but is already playing!");
				return;
			}
			this.lob_listeners.remove(client.getUsername());
		}
		synchronized (lobbies_lock) {
			if (this.lobbies.containsKey(id)) {
				client.setID(id);
				this.lobbies.get(id).connect(client);
			} else {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' tried to connect to a non existant lobby! [ID:" + id + "]");
			}
		}
		this.notifyLobbyListeners();
	}

	/**
     * Notifies all {@code ClientDescriptor} in the lobby select screen with the new available lobby list.
     */
	public void notifyLobbyListeners() {
		ClientLobbySelectState state = new ClientLobbySelectState(this.getLobbyList());
		ClientMessage message = new NotifyStateUpdateMessage(state);
		this.broadcast(message);
	}

	public ArrayList<ClientGameListEntry> getLobbyList() {
		ArrayList<ClientGameListEntry> tmp = new ArrayList<>();
		synchronized (lobbies_lock) {
			for (var l : this.lobbies.values()) {
				tmp.add(l.getClientInfo());
			}
			return tmp;
		}
	}

	/**
     * Rejoins the lobby select screen after a lobby controller has closed.
     *
     * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client being sent back to lobby select.
     */
	public void joinFromClosedLobby(ClientDescriptor client) {
		synchronized (listeners_lock) {
			if (this.lob_listeners.containsKey(client.getUsername())) {
				Logger.getInstance().print(LoggerLevel.WARN, "Client: '" + client.getUsername() + "' is already in lobby!");
				return;
			}
			Logger.getInstance().print(LoggerLevel.DEBUG, "Rejoining Client: '" + client.getUsername() + "' to lobby select");
			this.lob_listeners.put(client.getUsername(), client);
		}
		client.setID(-1);
	}

}
