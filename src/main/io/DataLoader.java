package main.io;

import main.model.Graph;
import main.model.Node;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DataLoader {

    public static Graph loadGraph(String nodesPath, String edgesPath) throws IOException {
        Graph g = new Graph();
        // load nodes
        List<String> nodeLines = Files.readAllLines(Paths.get(nodesPath));
        for (int i = 1; i < nodeLines.size(); i++) {
            String line = nodeLines.get(i).trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            String id = parts[0].trim();
            String name = parts[1].trim();
            double x = Double.parseDouble(parts[2].trim());
            double y = Double.parseDouble(parts[3].trim());
            g.addNode(new Node(id, name, x, y));
        }
        // load edges
        List<String> edgeLines = Files.readAllLines(Paths.get(edgesPath));
        for (int i = 1; i < edgeLines.size(); i++) {
            String line = edgeLines.get(i).trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            String from = parts[0].trim();
            String to = parts[1].trim();
            double w = Double.parseDouble(parts[2].trim());
            g.addEdge(from, to, w, true); // undirected graph
        }
        return g;
    }
}
