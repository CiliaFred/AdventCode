
package paris.cilia.game.days;

import lombok.Getter;
import lombok.Setter;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day15 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 15;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        int[][] map = new int[inputs.get(0).length()][inputs.size()];
        for (int y = 0; y < inputs.size(); y++) {
            for (int x = 0; x < inputs.get(y).length(); x++) {
                map[x][y] = Character.getNumericValue(inputs.get(y).charAt(x));
            }
        }
        Graph graph = initGraph(map);

        calculateShortestPathFromSource(graph);
        return getLastNode(graph, 1).getDistance();
    }

    protected Integer processSecondStar() {
        int[][] map = new int[inputs.get(0).length() * 5][inputs.size() * 5];

        for (int y = 0; y < inputs.size() * 5; y++) {
            for (int x = 0; x < inputs.get(y % inputs.size()).length() * 5; x++) {
                int jy = y % inputs.size();
                int jx = x % inputs.get(y % 5).length();
                int value = (Character.getNumericValue(inputs.get(jy).charAt(jx)) + ((x / inputs.get(y % inputs.size()).length()) + (y / inputs.size())));
                if( value >= 10) {
                    value++;
                }
                map[y][x] = value % 10;
            }
        }

        Graph graph = initGraph(map);

        calculateShortestPathFromSource(graph);
        return getLastNode(graph, 5).getDistance();
    }

    private void generateNodeNeighbors(int[][] map, Map<String, Node> nodes, int x, int y) {
        Node parent = nodes.get(generateNodeName(x, y));
        if (x > 0) {
            if (nodes.containsKey(generateNodeName(x - 1, y))) {
                Node neighbor = nodes.get(generateNodeName(x - 1, y));
                parent.addDestination(neighbor, map[x - 1][y]);
            } else {
                Node neighbor = new Node(generateNodeName(x - 1, y));
                parent.addDestination(neighbor, map[x - 1][y]);
            }
        }
        if (x < map.length - 1) {
            if (nodes.containsKey(generateNodeName(x + 1, y))) {
                Node neighbor = nodes.get(generateNodeName(x + 1, y));
                parent.addDestination(neighbor, map[x + 1][y]);
            } else {
                Node neighbor = new Node(generateNodeName(x + 1, y));
                parent.addDestination(neighbor, map[x + 1][y]);
            }
        }
        if (y > 0) {
            if (nodes.containsKey(generateNodeName(x, y - 1))) {
                Node neighbor = nodes.get(generateNodeName(x, y - 1));
                parent.addDestination(neighbor, map[x][y - 1]);
            } else {
                Node neighbor = new Node(generateNodeName(x, y - 1));
                parent.addDestination(neighbor, map[x][y - 1]);
            }
        }
        if (y < map[0].length - 1) {
            if (nodes.containsKey(generateNodeName(x, y + 1))) {
                Node neighbor = nodes.get(generateNodeName(x, y + 1));
                parent.addDestination(neighbor, map[x][y + 1]);
            } else {
                Node neighbor = new Node(generateNodeName(x, y + 1));
                parent.addDestination(neighbor, map[x][y + 1]);
            }
        }
    }

    private Node getLastNode(Graph graph, int multiplier) {
        for (Node node : graph.getNodes()) {
            if (node.getName().equalsIgnoreCase(generateNodeName((inputs.get(0).length() * multiplier) - 1,
                    (inputs.size() * multiplier) - 1))) {
                return node;
            }
        }
        return null;
    }

    private String generateNodeName(int x, int y) {
        return String.join("-", String.valueOf(x), String.valueOf(y));
    }

    private Graph initGraph(int[][] map) {
        Graph graph = new Graph();
        Map<String, Node> nodes = new HashMap<>();
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                Node node = new Node(generateNodeName(x, y));
                nodes.put(node.getName(), node);
            }
        }
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                generateNodeNeighbors(map, nodes, x, y);
            }
        }
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                if (x == 0 && y == 0) {
                    graph.setFirstNode(nodes.get(generateNodeName(x, y)));
                }
                graph.addNode(nodes.get(generateNodeName(x, y)));
            }
        }
        return graph;
    }

    private static Graph calculateShortestPathFromSource(Graph graph) {
        graph.getFirstNode().setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(graph.getFirstNode());

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node, Integer> adjacencyPair :
                    currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Node evaluationNode,
                                                 Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    @Getter
    @Setter
    private class Graph {

        private Node firstNode;

        private Set<Node> nodes = new HashSet<>();

        public void addNode(Node nodeA) {
            nodes.add(nodeA);
        }

    }

    @Getter
    @Setter
    private class Node {

        private String name;

        private List<Node> shortestPath = new LinkedList<>();

        private Integer distance = Integer.MAX_VALUE;

        Map<Node, Integer> adjacentNodes = new HashMap<>();

        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }

        public Node(String name) {
            this.name = name;
        }
    }

}
