package main.app;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import main.model.Edge;
import main.model.Graph;
import main.model.Node;

import java.util.ArrayList;
import java.util.List;

public class MapCanvas extends Pane {
    private final Graph graph;
    public ArrayList<Circle> circles = new ArrayList<>();

    public MapCanvas(Graph graph) {
        this.graph = graph;
        setPrefSize(900, 650);

        drawGraph();
    }

    private void drawGraph() {
        getChildren().clear();
        // Draw edges
        for (String id : graph.nodeIds()) {
            for (Edge e : graph.neighbors(id)) {
                // to avoid duplicate undirected drawing, draw only when from<to
                if (e.getFrom().compareTo(e.getTo()) < 0) {
                    Node a = graph.getNode(e.getFrom());
                    Node b = graph.getNode(e.getTo());
                    Line line = new Line(a.getX(), a.getY(), b.getX(), b.getY());
                    line.setStrokeWidth(2);
                    line.setStroke(Color.LIGHTGRAY);
                    getChildren().add(line);
                }
            }
        }
        // Draw nodes
        for (Node n : graph.getNodes()) {
//            circles.clear();
            Circle c = new Circle(n.getX(), n.getY(), 8, Color.DARKRED);
            circles.add(c);
            Label l = new Label(n.getName());
            if (n.getName().equals("UC") || n.getName().equals("DPS") || n.getName().equals("Towers") || n.getName().equals("Laferla") ||
                    n.getName().equals("Kent Library") || n.getName().equals("Grauel") || n.getName().equals("Carnahan Hall")) {
                l.setLayoutX(n.getX() - 10);
                l.setLayoutY(n.getY() - 20);
            } else if (n.getName().equals("Crisp Hall")) {
                l.setLayoutX(n.getX() - 10);
                l.setLayoutY(n.getY() + 10);
            }else {
                l.setLayoutX(n.getX() - 50);
                l.setLayoutY(n.getY() - 20);
            }
            l.setFont(new Font(8));
            l.setBackground(new Background(
                    new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)
            ));
            c.setUserData(n.getId());
            Tooltip t = new Tooltip(n.getId() + ": " + n.getName());
            Tooltip.install(c, t);
            getChildren().add(c);
            getChildren().add(l);
        }
    }

    public void highlightPath(List<String> path) {
        drawGraph(); // reset
        // draw path lines thicker and colored
        for (int i = 0; i < path.size()-1; i++) {
            Node a = graph.getNode(path.get(i));
            Node b = graph.getNode(path.get(i+1));
            javafx.scene.shape.Line line = new javafx.scene.shape.Line(a.getX(), a.getY(), b.getX(), b.getY());
            line.setStrokeWidth(4);
            line.setStroke(Color.ORANGE);
            getChildren().add(line);
        }
        // highlight nodes
        for (String id : path) {
            Node n = graph.getNode(id);
            for (Circle circle : circles) {
                if (circle.getCenterX() == n.getX() && circle.getCenterY() == n.getY()) {
                    circle.setFill(Color.ORANGE);
                    circle.setRadius(10);
                }
            }
        }
    }
}
