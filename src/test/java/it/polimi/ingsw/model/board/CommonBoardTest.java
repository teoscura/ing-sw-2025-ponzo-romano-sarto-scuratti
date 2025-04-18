package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.StructuralComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.iBaseComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
// TODO
/*class CommonBoardTest {

    CommonBoard board;

    @BeforeEach
    void setup(){
        board = new CommonBoard();
    }

    @Test
    void pullComponent() {
        iBaseComponent component = board.pullComponent();
        assertNotNull(component);
    }

    @Test
    void discardComponent() {
        iBaseComponent component = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000);
        board.discardComponent(component);
        //assertTrue(board.uncovered_components.contains(component));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> board.discardComponent(component));
        assertEquals("Tried to insert a duplicate component.", e.getMessage());
    }

    @Test
    void pullDiscarded() {
        assertEquals(null, board.pullDiscarded(1));
        iBaseComponent component1 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000);
        iBaseComponent component2 = new StructuralComponent(2, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000);
        board.discardComponent(component1);
        board.discardComponent(component2);
        assertThrows(OutOfBoundsException.class, () -> board.pullDiscarded(-1));
        assertThrows(OutOfBoundsException.class, () -> board.pullDiscarded(2));
        assertEquals(component1, board.pullDiscarded(0));
        assertEquals(component2, board.pullDiscarded(0));
        //assertTrue(board.uncovered_components.isEmpty());
    }
}*/