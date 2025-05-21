package team28.backend.service;

import team28.backend.model.Coordinate;
import team28.backend.model.Reader;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class PathfindingService {

    public List<String> findPath(Reader start, Reader end, List<Reader> allReaders) {
        Map<String, Reader> grid = new HashMap<>();
        for (Reader r : allReaders) {
            Coordinate c = r.getCoordinates();
            grid.put(c.getLatitude() + "," + c.getLongitude(), r);
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::fCost));
        Map<Reader, Integer> gScores = new HashMap<>();
        Set<Reader> closedSet = new HashSet<>();

        Node startNode = new Node(start, 0, heuristic(start.getCoordinates(), end.getCoordinates()), null);
        openSet.add(startNode);
        gScores.put(start, 0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.reader.equals(end)) {
                return reconstructPath(current);
            }

            closedSet.add(current.reader);

            for (Reader neighbor : getNeighbors(current.reader, grid)) {
                if (closedSet.contains(neighbor))
                    continue;

                int tentativeG = current.gCost + 1;

                if (tentativeG < gScores.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    gScores.put(neighbor, tentativeG);
                    Node neighborNode = new Node(neighbor, tentativeG,
                            heuristic(neighbor.getCoordinates(), end.getCoordinates()), current);
                    openSet.add(neighborNode);
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private int heuristic(Coordinate a, Coordinate b) {
        return Math.abs(a.getLatitude() - b.getLatitude()) + Math.abs(a.getLongitude() - b.getLongitude());
    }

    private List<Reader> getNeighbors(Reader reader, Map<String, Reader> grid) {
        List<Reader> neighbors = new ArrayList<>();
        int[][] directions = {
                { -1, 0 }, // up
                { 1, 0 }, // down
                { 0, -1 }, // left
                { 0, 1 } // right
        };

        Coordinate coord = reader.getCoordinates();
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
        List<String> directions = new LinkedList<>();

        while (node.parent != null) {
            Coordinate from = node.parent.reader.getCoordinates();
            Coordinate to = node.reader.getCoordinates();

            int dx = to.getLatitude() - from.getLatitude();
            int dy = to.getLongitude() - from.getLongitude();

            if (dx == -1)
                directions.add(0, "UP");
            else if (dx == 1)
                directions.add(0, "DOWN");
            else if (dy == -1)
                directions.add(0, "LEFT");
            else if (dy == 1)
                directions.add(0, "RIGHT");

            node = node.parent;
        }

        return directions;
    }

    private static class Node {
        Reader reader;
        int gCost;
        int hCost;
        Node parent;

        Node(Reader reader, int gCost, int hCost, Node parent) {
            this.reader = reader;
            this.gCost = gCost;
            this.hCost = hCost;
            this.parent = parent;
        }

        int fCost() {
            return gCost + hCost;
        }
    }
}
