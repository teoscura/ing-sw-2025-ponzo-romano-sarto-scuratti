package it.polimi.ingsw.model.client.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;
import it.polimi.ingsw.model.client.player.ClientVoyagePlayer;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.player.PlayerColor;

public class ClientStateTest {
    
    @Test
    public void clientStateTest(){
        ArrayList<ClientGameListEntry> lls = new ArrayList<>();
        ClientLobbySelectState sls = new ClientLobbySelectState(lls);
        assertEquals(sls.getLobbyList(), lls);

        ClientSetupState sss = new ClientSetupState("Gigione", lls);
        assertEquals(sss.getUnfinishedList(), lls);
        assertEquals(sss.getSetupperName(), "Gigione");

        ArrayList<ClientWaitingPlayer> lw = new ArrayList<>();
        ClientWaitingRoomState sw = new ClientWaitingRoomState(GameModeType.TEST, PlayerCount.TWO, lw);
        assertEquals(sw.getType(), GameModeType.TEST);
        assertEquals(sw.getCount(), PlayerCount.TWO);
        assertEquals(sw.getPlayerList(), lw);

        ArrayList<ClientConstructionPlayer> lc = new ArrayList<>();
        ClientConstructionState sc = new ClientConstructionState(GameModeType.TEST, lc, null, new ArrayList<>(), 1, 2, 1, null, null);
        assertEquals(sc.getType(), GameModeType.TEST);
        assertEquals(sc.getLastToggle(), null);
        assertEquals(sc.getHourglassDuration(), null);
        assertEquals(sc.getPlayerList(), new ArrayList<>());
        assertEquals(sc.getConstructionCards(), null);
        assertEquals(sc.getDiscardedTiles(), new ArrayList<>());
        assertEquals(sc.getTilesLeft(),1);
        assertEquals(sc.getTogglesTotal(), 2);
        assertEquals(sc.getTogglesLeft(), 1);

        ArrayList<ClientVerifyPlayer> lv = new ArrayList<>();
        ClientVerifyState sv = new ClientVerifyState(GameModeType.TEST, lv);
        assertEquals(sv.getPlayerList(), new ArrayList<>());
        assertEquals(sv.getType(), GameModeType.TEST);

        ClientBaseCardState state = new ClientBaseCardState("AA", 2);
        ArrayList<ClientVoyagePlayer> lvg = new ArrayList<>();
        ClientVoyageState svg = new ClientVoyageState(GameModeType.TEST, lvg, state, 1);
        assertEquals(svg.getCardState(), state);
        assertEquals(svg.getCardsLeft(), 1);
        assertEquals(svg.getPlayerList(), lvg);
        assertEquals(svg.getType(), GameModeType.TEST);

        ArrayList<ClientEndgamePlayer> le = new ArrayList<>();
        ClientEndgameState se = new ClientEndgameState(le, new ArrayList<>(){{add(PlayerColor.RED);}});
        assertEquals(se.getPlayerList(), le);
        assertEquals(se.awaiting(), new ArrayList<>(){{add(PlayerColor.RED);}});

        DummyStateVisitor v = new DummyStateVisitor();
        sls.sendToView(v);
        sss.sendToView(v);
        sw.sendToView(v);
        sc.sendToView(v);
        sv.sendToView(v);
        svg.sendToView(v);
        se.sendToView(v);
        assertEquals(v.visited(), 7);
    }


}
