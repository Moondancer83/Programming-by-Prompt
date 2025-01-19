package hu.kalee.diplomacy;

import java.util.*;

public class DiplomacyGame {

    // Territory class to define provinces
    static class Territory {
        String name;
        boolean isSupplyCenter;
        boolean isInland;  // True for inland provinces
        boolean isCoastal; // True for coastal provinces
        boolean isWater;   // True for water provinces
        List<String> adjacentTerritories; // Names of adjacent provinces
        String unit; // The occupying unit, e.g., "A Rome" or "F Tyrrhenian Sea"

        public Territory(String name, boolean isSupplyCenter, boolean isInland, boolean isCoastal, boolean isWater) {
            this.name = name;
            this.isSupplyCenter = isSupplyCenter;
            this.isInland = isInland;
            this.isCoastal = isCoastal;
            this.isWater = isWater;
            this.adjacentTerritories = new ArrayList<>();
            this.unit = null; // No unit by default
        }

        public void addAdjacent(String adjacent) {
            adjacentTerritories.add(adjacent);
        }

        @Override
        public String toString() {
            return name + " (" + (unit == null ? "empty" : unit) + ")";
        }
    }

    public static void main(String[] args) {
        // 1. Initialize the board
        Map<String, Territory> board = initializeBoard();

        // 2. Place a Fleet in Rome for testing
        board.get("Rome").unit = "F Italy";

        // 3. Print possible movements for the Fleet in Rome
        List<String> possibleMoves = getPossibleMoves(board, "F Rome");
        System.out.println("Possible moves for Fleet in Rome: " + possibleMoves);
    }

    // Initialize the game board
    public static Map<String, Territory> initializeBoard() {
        Map<String, Territory> board = new HashMap<>();

        // Define territories
        board.put("Rome", new Territory("Rome", true, false, true, false));
        board.put("Naples", new Territory("Naples", true, false, true, false));
        board.put("Tuscany", new Territory("Tuscany", false, false, true, false));
        board.put("Tyrrhenian Sea", new Territory("Tyrrhenian Sea", false, false, false, true));
        board.put("Venice", new Territory("Venice", true, true, true, false));
        board.put("Apulia", new Territory("Apulia", false, false, true, false));

        // Define adjacencies
        board.get("Rome").addAdjacent("Naples");
        board.get("Rome").addAdjacent("Tuscany");
        board.get("Rome").addAdjacent("Tyrrhenian Sea");

        board.get("Naples").addAdjacent("Rome");
        board.get("Naples").addAdjacent("Tyrrhenian Sea");

        board.get("Tuscany").addAdjacent("Rome");
        board.get("Tuscany").addAdjacent("Tyrrhenian Sea");

        board.get("Tyrrhenian Sea").addAdjacent("Rome");
        board.get("Tyrrhenian Sea").addAdjacent("Naples");
        board.get("Tyrrhenian Sea").addAdjacent("Tuscany");

        return board;
    }

    // Get possible moves for a unit
    public static List<String> getPossibleMoves(Map<String, Territory> board, String unit) {
        String[] parts = unit.split(" ");
        String unitType = parts[0]; // "F" or "A"
        String currentTerritoryName = parts[1];

        Territory currentTerritory = board.get(currentTerritoryName);
        List<String> possibleMoves = new ArrayList<>();

        for (String adjacent : currentTerritory.adjacentTerritories) {
            Territory adjacentTerritory = board.get(adjacent);

            // Fleet movement: coastal or water provinces only
            if (unitType.equals("F") && (adjacentTerritory.isCoastal || adjacentTerritory.isWater)) {
                possibleMoves.add(adjacent);
            }
        }

        return possibleMoves;
    }
}

