package it.polimi.ingsw.model.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;

public class ClientGameEntryTest {
    
    @Test
    public void entryTest(){
        ClientGameListEntry e = new ClientGameListEntry(GameModeType.TEST, PlayerCount.THREE, "Gigio", new ArrayList<>(){{add("aaa");add("bbb");}}, 1);
        assertEquals(e.getType(), GameModeType.TEST);
        assertEquals(e.getCount(), PlayerCount.THREE);
        assertEquals(e.getModelId(), 1);
        assertEquals(e.getState(), "Gigio");
        assertEquals(e.getPlayers(), new ArrayList<>(){{add("aaa");add("bbb");}});
    }

}
