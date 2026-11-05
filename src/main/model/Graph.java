package main.model;

import java.util.*;

public class Graph {
    private final Map<String, Node> nodes = new HashMap<>();
    private final Map<String, List<Edge>> adj = new HashMap<>();

    public void addNode(Node n) {
        nodes.put(n.getId(), n);
        adj.putIfAbsent(n.getId(), new ArrayList<>());
    }

    public void addEdge(String from, String to, double weight, boolean undirected) {
        if (!nodes.containsKey(from) || !nodes.containsKey(to)) return;
        adj.get(from).add(new Edge(from, to, weight));
        if (undirected) adj.get(to).add(new Edge(to, from, weight));
    }

    public Collection<Node> getNodes() { return nodes.values(); }
    public Node getNode(String id) { return nodes.get(id); }
    public List<Edge> neighbors(String id) { return adj.getOrDefault(id, Collections.emptyList()); }

    public Set<String> nodeIds() { return nodes.keySet(); }
    public Set<String> nodeNames() {
        Set<String> names = new HashSet<>();
        Set<Map.Entry<String, Node>> entries = nodes.entrySet();
        for (Map.Entry<String, Node> entry : entries) {
            names.add(entry.getValue().getName());
        }
        return names;
    }
    public Set<Node> getNodeSet() {
        Set<Map.Entry<String, Node>> entries = nodes.entrySet();
        Set<Node> nodes = new HashSet<>();
        for (Map.Entry<String, Node> entry : entries) {
            nodes.add(entry.getValue());
        }
        return nodes;
    }
}
