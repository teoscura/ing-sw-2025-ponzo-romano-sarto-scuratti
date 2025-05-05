package it.polimi.ingsw.model.board;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.StructuralComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;

class CommonBoardTest {

    CommonBoard board;

    @BeforeEach
    void setup(){
        board = new CommonBoard();
    }

    @Test
    void pullComponent() {
        BaseComponent component = board.pullComponent();
        assertNotNull(component);
    }

    @Test
    void discardComponent() {
        BaseComponent component = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000);
        board.discardComponent(component);
        //assertTrue(board.uncovered_components.contains(component));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> board.discardComponent(component));
        assertEquals("Tried to insert a duplicate component.", e.getMessage());
    }

    @Test
    void pullDiscarded() {
        assertThrows(ContainerEmptyException.class, ()-> board.pullDiscarded(1));
        BaseComponent component1 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000);
        BaseComponent component2 = new StructuralComponent(2, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000);
        board.discardComponent(component1);
        board.discardComponent(component2);
        assertThrows(ContainerEmptyException.class, () -> board.pullDiscarded(-1));
        assertDoesNotThrow(()-> board.pullDiscarded(2));
        board.discardComponent(component2);
        assertEquals(component1, board.pullDiscarded(1));
        assertEquals(component2, board.pullDiscarded(2));
    }
}