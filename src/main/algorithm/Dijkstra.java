package main.algorithm;

import main.model.Edge;
import main.model.Graph;

import java.util.*;

public class Dijkstra {
    private static Map<String, Double> dist;
    private static Map<String, String> prev;
    public static List<String> shortestPath(Graph g, String source, String target) {
        dist = new HashMap<>();
        prev = new HashMap<>();
        for (String id : g.nodeIds()) {
            dist.put(id, Double.POSITIVE_INFINITY);
        }
        dist.put(source, 0.0);
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(source);

        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (u.equals(target)) break;
            double du = dist.get(u);
            for (Edge e : g.neighbors(u)) {
                String v = e.getTo();
                double alt = du + e.getWeight();
                if (alt < dist.getOrDefault(v, Double.POSITIVE_INFINITY)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.remove(v); // simple decrease-key simulation
                    pq.add(v);
                }
            }
        }
        // reconstruct path
        if (!prev.containsKey(target) && !source.equals(target)) return Collections.emptyList();
        LinkedList<String> path = new LinkedList<>();

        String cur = target;

        path.addFirst(cur);
        while (prev.containsKey(cur)) {
            cur = prev.get(cur);
            path.addFirst(cur);
        }

        if (!path.getFirst().equals(source)) return Collections.emptyList();
        return path;
    }

    public static double calculateDistance(String target) {
        return dist.get(target);
    }
}
