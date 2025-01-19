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
        board.put("Rome", new Territory("Rome", true, false, true, false));  // Coastal
        board.put("Naples", new Territory("Naples", true, false, true, false));  // Coastal
        board.put("Tuscany", new Territory("Tuscany", false, false, true, false));  // Coastal
        board.put("Tyrrhenian Sea", new Territory("Tyrrhenian Sea", false, false, false, true));  // Water
        board.put("Adriatic Sea", new Territory("Adriatic Sea", false, false, false, true));  // Water
        board.put("Venice", new Territory("Venice", true, false, true, false));  // Coastal
        board.put("Apulia", new Territory("Apulia", false, false, true, false));  // Coastal

        // Define adjacencies
        board.get("Rome").addAdjacent("Naples");       // Land and sea adjacency
        board.get("Rome").addAdjacent("Tuscany");      // Land and sea adjacency
        board.get("Rome").addAdjacent("Tyrrhenian Sea"); // Sea adjacency
        board.get("Rome").addAdjacent("Venice");       // Land adjacency only
        board.get("Rome").addAdjacent("Apulia");       // Land adjacency only

        board.get("Naples").addAdjacent("Rome");
        board.get("Naples").addAdjacent("Tyrrhenian Sea");

        board.get("Tuscany").addAdjacent("Rome");
        board.get("Tuscany").addAdjacent("Tyrrhenian Sea");

        board.get("Tyrrhenian Sea").addAdjacent("Rome");
        board.get("Tyrrhenian Sea").addAdjacent("Naples");
        board.get("Tyrrhenian Sea").addAdjacent("Tuscany");

        board.get("Venice").addAdjacent("Rome");  // Land adjacency only
        board.get("Venice").addAdjacent("Adriatic Sea");

        board.get("Adriatic Sea").addAdjacent("Venice");
        board.get("Adriatic Sea").addAdjacent("Apulia");

        board.get("Apulia").addAdjacent("Rome");
        board.get("Apulia").addAdjacent("Adriatic Sea");

        return board;
    }

    // Get possible moves for a unit
    public static List<String> getPossibleMoves(Map<String, Territory> board, String unit) {
        String[] parts = unit.split(" ");
        String unitType = parts[0]; // "F" for Fleet or "A" for Army
        String currentTerritoryName = parts[1];

        Territory currentTerritory = board.get(currentTerritoryName);
        List<String> possibleMoves = new ArrayList<>();

        // Check if the specified unit exists in the current territory
        if (currentTerritory.unit == null || !currentTerritory.unit.equals(unit)) {
            return possibleMoves; // Return an empty list if the unit doesn't exist or doesn't match
        }

        // Determine possible moves based on adjacency and unit type
        for (String adjacent : currentTerritory.adjacentTerritories) {
            Territory adjacentTerritory = board.get(adjacent);

            if (unitType.equals("A")) {
                // Army movement: inland or coastal provinces only
                if (adjacentTerritory.isInland || adjacentTerritory.isCoastal) {
                    possibleMoves.add(adjacent);
                }
            } else if (unitType.equals("F")) {
                // Fleet movement: coastal or water provinces only
                if (adjacentTerritory.isWater || (adjacentTerritory.isCoastal && sharesSeaRoute(board, currentTerritory, adjacentTerritory))) {
                    possibleMoves.add(adjacent);
                }
            }
        }

        return possibleMoves;
    }

    private static boolean sharesSeaRoute(Map<String, Territory> board, Territory origin, Territory destination) {
        // Check if there's a shared sea province between origin and destination
        for (String adjacentToOrigin : origin.adjacentTerritories) {
            Territory seaTerritory = board.get(adjacentToOrigin);
            if (seaTerritory.isWater && destination.adjacentTerritories.contains(seaTerritory.name)) {
                return true; // Shared sea route found
            }
        }
        return false; // No shared sea route
    }

}

