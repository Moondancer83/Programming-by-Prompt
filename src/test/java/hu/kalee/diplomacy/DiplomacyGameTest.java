package hu.kalee.diplomacy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DiplomacyGameTest {

    private Map<String, DiplomacyGame.Territory> board;

    @BeforeEach
    void setUp() {
        board = DiplomacyGame.initializeBoard();
    }

    @Test
    void testFleetMovementFromRome() {
        // Place a Fleet in Rome
        board.get("Rome").unit = "F Italy";

        // Get possible moves
        List<String> possibleMoves = DiplomacyGame.getPossibleMoves(board, "F Rome");

        // Verify possible moves
        List<String> expectedMoves = Arrays.asList("Naples", "Tuscany", "Tyrrhenian Sea");
        assertEquals(expectedMoves.size(), possibleMoves.size(), "Incorrect number of possible moves");
        assertTrue(possibleMoves.containsAll(expectedMoves), "Possible moves do not match expected");
    }

    @Test
    void testNoMovementFromEmptyRome() {
        // No unit in Rome
        board.get("Rome").unit = null;

        // Get possible moves
        List<String> possibleMoves = DiplomacyGame.getPossibleMoves(board, "F Rome");

        // Verify no moves
        assertTrue(possibleMoves.isEmpty(), "Fleet in empty Rome should have no possible moves");
    }

    @Test
    void testFleetCannotMoveInland() {
        // Place a Fleet in Rome
        board.get("Rome").unit = "F Italy";

        // Add inland territory Venice
        board.get("Rome").addAdjacent("Venice");

        // Get possible moves
        List<String> possibleMoves = DiplomacyGame.getPossibleMoves(board, "F Rome");

        // Verify that Venice (inland) is not included
        assertFalse(possibleMoves.contains("Venice"), "Fleet should not move to inland territory Venice");
    }
}
