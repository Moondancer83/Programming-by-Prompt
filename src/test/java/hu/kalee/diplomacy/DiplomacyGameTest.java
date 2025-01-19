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
    void testLandAdjacencyFromRome() {
        List<String> landAdjacent = board.get("Rome").adjacentTerritories;

        // Verify land adjacency
        assertTrue(landAdjacent.contains("Tuscany"), "Rome should be adjacent to Tuscany on land");
        assertTrue(landAdjacent.contains("Naples"), "Rome should be adjacent to Naples on land");
        assertTrue(landAdjacent.contains("Venice"), "Rome should be adjacent to Venice on land");
        assertTrue(landAdjacent.contains("Apulia"), "Rome should be adjacent to Apulia on land");
    }

    @Test
    void testFleetMovementFromRome() {
        // Place a Fleet in Rome
        board.get("Rome").unit = "F Rome";

        // Get possible moves for the Fleet in Rome
        List<String> possibleMoves = DiplomacyGame.getPossibleMoves(board, "F Rome");

        // Verify possible moves
        List<String> expectedMoves = Arrays.asList("Tuscany", "Naples", "Tyrrhenian Sea");
        assertEquals(expectedMoves.size(), possibleMoves.size(), "Incorrect number of possible moves");
        assertTrue(possibleMoves.containsAll(expectedMoves), "Possible moves do not match expected");
        assertFalse(possibleMoves.contains("Venice"), "Fleet should not be able to move to Venice");
        assertFalse(possibleMoves.contains("Apulia"), "Fleet should not be able to move to Apulia");
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

    @Test
    void testArmyMovementFromRome() {
        // Place an Army in Rome
        board.get("Rome").unit = "A Rome";

        // Get possible moves for the Army in Rome
        List<String> possibleMoves = DiplomacyGame.getPossibleMoves(board, "A Rome");

        // Verify possible moves
        List<String> expectedMoves = Arrays.asList("Tuscany", "Venice", "Apulia", "Naples");
        assertEquals(expectedMoves.size(), possibleMoves.size(), "Incorrect number of possible moves");
        assertTrue(possibleMoves.containsAll(expectedMoves), "Possible moves do not match expected");
        assertFalse(possibleMoves.contains("Tyrrhenian Sea"), "Army should not be able to move to the Tyrrhenian Sea");
    }


}
