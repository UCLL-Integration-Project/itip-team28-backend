package team28.backend.service;

import team28.backend.model.Coordinate;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class PathfindingService {

    public List<String> findPath(Coordinate start, Coordinate end, List<Coordinate> allCoordinates) {
        Map<String, Coordinate> grid = new HashMap<>();
        for (Coordinate c : allCoordinates) {
            grid.put(c.getLatitude() + "," + c.getLongitude(), c);
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::fCost));
        Map<Coordinate, Integer> gScores = new HashMap<>();
        Set<Coordinate> closedSet = new HashSet<>();

        Node startNode = new Node(start, 0, heuristic(start, end), null);
        openSet.add(startNode);
        gScores.put(start, 0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.coordinate.equals(end)) {
                List<String> path = reconstructPath(current);
                System.out.println(path);
                return path;
            }

            closedSet.add(current.coordinate);

            for (Coordinate neighbor : getNeighbors(current.coordinate, grid)) {
                if (closedSet.contains(neighbor))
                    continue;

                int tentativeG = current.gCost + 1;

                if (tentativeG < gScores.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    gScores.put(neighbor, tentativeG);
                    Node neighborNode = new Node(neighbor, tentativeG, heuristic(neighbor, end), current);
                    openSet.add(neighborNode);
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private int heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.getLatitude() - b.getLatitude()) + Math.abs(a.getLongitude() - b.getLongitude());
    }

    private List<Coordinate> getNeighbors(Coordinate coord, Map<String, Coordinate> grid) {
        List<Coordinate> neighbors = new ArrayList<>();
        int[][] directions = {
                { -1, 0 }, // up
                { 1, 0 }, // down
                { 0, -1 }, // left
                { 0, 1 } // right
        };

        int x = coord.getLatitude();
        int y = coord.getLongitude();

        for (int[] dir : directions) {
            String key = (x + dir[0]) + "," + (y + dir[1]);
            if (grid.containsKey(key)) {
                neighbors.add(grid.get(key));
            }
        }

        return neighbors;
    }

    private List<String> reconstructPath(Node node) {
        List<String> instructions = new LinkedList<>();
        Direction currentFacing = null;

        while (node.parent != null) {
            Coordinate from = node.parent.coordinate;
            Coordinate to = node.coordinate;

            int dx = to.getLatitude() - from.getLatitude();
            int dy = to.getLongitude() - from.getLongitude();

            Direction movementDir = getDirection(dx, dy);

            if (currentFacing == null) {
                // First step: assume the car starts facing in the direction of the first move
                currentFacing = movementDir;
                instructions.add(0, "go forward");
            } else {
                String turn = getTurnInstruction(currentFacing, movementDir);
                instructions.add(0, turn);
                currentFacing = movementDir;
            }

            node = node.parent;
        }

        return instructions;
    }

    private Direction getDirection(int dx, int dy) {
        if (dx == -1)
            return Direction.NORTH;
        if (dx == 1)
            return Direction.SOUTH;
        if (dy == -1)
            return Direction.WEST;
        if (dy == 1)
            return Direction.EAST;
        throw new IllegalArgumentException("Invalid movement delta: (" + dx + "," + dy + ")");
    }

    private String getTurnInstruction(Direction from, Direction to) {
        if (from == to)
            return "go forward";

        switch (from) {
            case NORTH:
                return to == Direction.EAST ? "turn right"
                        : to == Direction.WEST ? "turn left"
                                : "turn around";
            case SOUTH:
                return to == Direction.WEST ? "turn right"
                        : to == Direction.EAST ? "turn left"
                                : "turn around";
            case EAST:
                return to == Direction.SOUTH ? "turn right"
                        : to == Direction.NORTH ? "turn left"
                                : "turn around";
            case WEST:
                return to == Direction.NORTH ? "turn right"
                        : to == Direction.SOUTH ? "turn left"
                                : "turn around";
        }
        return "unknown";
    }

    private enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    private static class Node {
        Coordinate coordinate;
        int gCost;
        int hCost;
        Node parent;

        Node(Coordinate coordinate, int gCost, int hCost, Node parent) {
            this.coordinate = coordinate;
            this.gCost = gCost;
            this.hCost = hCost;
            this.parent = parent;
        }

        int fCost() {
            return gCost + hCost;
        }
    }
}
