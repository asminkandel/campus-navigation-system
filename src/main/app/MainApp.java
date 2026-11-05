package main.app;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.algorithm.Dijkstra;
import main.io.DataLoader;
import main.model.Graph;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.model.Node;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainApp extends Application {
    private Graph graph;
    private MapCanvas canvas;
    private ImageView background;



    @Override
    public void start(Stage stage) throws Exception {
        // load data
        String base = Paths.get("").toAbsolutePath().toString();
        graph = DataLoader.loadGraph("data/nodes.csv", "data/edges.csv");

        Pane root = new Pane();
        loadBackgroundImage();
        root.getChildren().add(background);
        canvas = new MapCanvas(graph); // custom Pane to draw nodes/edges

        TextField fromField = new TextField();
        fromField.setPromptText("From node name (e.g. Kent Library)");
        TextField toField = new TextField();
        toField.setPromptText("To node name (e.g. Grauel)");
        final TextField[] activeField = {null};
// Track active field
        fromField.focusedProperty().addListener((obs, oldV, newV) -> {
            if (newV) activeField[0] = fromField;
        });

        toField.focusedProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                activeField[0] = (toField);
            }
        });
        Set<Node> nodes = graph.getNodeSet();
        for (Circle c : canvas.circles) {
            c.setOnMouseClicked(e -> {
                if (activeField[0] != null) {
                    for (Node node : nodes) {
                        if (c.getCenterX() == node.getX() && c.getCenterY() == node.getY()) {
                            activeField[0].setText(node.getName());
//                            c.setFill(Color.DARKBLUE);
                            break;
                        }
                    }
                }
            });
        }
        Label fromLabel = new Label("From:");
        Label toLabel = new Label("To:");
        Button findBtn = new Button("Find Path");
        Label status = new Label();
        status.setWrapText(true);
        status.setPadding(new Insets(10));
        status.setMaxHeight(Double.MAX_VALUE);
        status.setPrefWidth(300);
        status.setLayoutX(500);
        status.setLayoutY(500);
        status.setBackground(new Background(
                new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
        ));
        status.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                        new CornerRadii(5), new BorderWidths(2))
        ));

        root.getChildren().addAll(fromLabel, fromField, toLabel, toField, findBtn);
        fromLabel.setLayoutX(10);
        fromLabel.setLayoutY(10);
        fromField.setLayoutX(50);
        fromField.setLayoutY(5);
        toLabel.setLayoutX(220);
        toLabel.setLayoutY(10);
        toField.setLayoutX(250);
        toField.setLayoutY(5);
        findBtn.setLayoutX(420);
        findBtn.setLayoutY(5);
        canvas.setLayoutY(40);
        status.setLayoutX(20);
        status.setLayoutY(50);
        root.getChildren().add(canvas);
        root.getChildren().add(status);

        findBtn.setOnAction(e -> {
            String s = fromField.getText().trim();
            String t = toField.getText().trim();

            String s_id = new String();
            String t_id = new String();
            for (Node node: nodes) {
                if (node.getName().equals(s)) s_id = node.getId();
                if (node.getName().equals(t)) t_id = node.getId();
            }
            if (!graph.nodeIds().contains(s_id) || !graph.nodeIds().contains(t_id)) {
                status.setText("Invalid building");
                return;
            }
            List<String> path = Dijkstra.shortestPath(graph, s_id, t_id);
            List<String> pathName = new ArrayList<>();

            for (int i = 0; i < path.size(); i++) {
                Node a = graph.getNode(path.get(i));
                for (Node node: nodes) {
                    if (node.getId().equals(a.getId())) {
                        pathName.add(node.getName());
                        break;
                    }
                }
//                Node b = graph.getNode(path.get(i+1));
//                javafx.scene.shape.Line line = new javafx.scene.shape.Line(a.getX(), a.getY(), b.getX(), b.getY());
//                line.setStrokeWidth(4);
//                line.setStroke(Color.ORANGE);
//                getChildren().add(line);
            }
            if (path.isEmpty()) status.setText("No path found.");
            else {
                double distance = Dijkstra.calculateDistance(t_id);
                double time = (Dijkstra.calculateDistance(t_id)/300);
                String timeString = String.format("%.2f", time);
                status.setText("Shortest Path: " + String.join(" -> ", pathName) + "\n\n" + "Total distance: " + Dijkstra.calculateDistance(t_id) + " ft"
                + "\nEstimated walking time: " + timeString + " min");
                canvas.highlightPath(path);
            }
            for (Circle c : canvas.circles) {
                c.setOnMouseClicked(e1 -> {
                    if (activeField[0] != null) {
                        for (Node node : nodes) {
                            if (c.getCenterX() == node.getX() && c.getCenterY() == node.getY()) {
                                activeField[0].setText(node.getName());
                                break;
                            }
                        }
                    }
                });
            }
        });

        Scene scene = new Scene(root, 900, 650);

//        scene.setOnMouseClicked(e -> {
//            double x = e.getSceneX();
//            double y = e.getSceneY();
//            System.out.println("Scene coordinates: (" + x + ", " + y + ")");
//        });

        stage.setTitle("Campus Navigation");
        stage.setScene(scene);
        stage.show();


    }
    private void loadBackgroundImage() {
        // Load the image from resources
        Image img = new Image(getClass().getResource("/background.png").toString());

        background = new ImageView(img);
        background.setFitWidth(900);    // Match window or map size
        background.setFitHeight(650);
        background.setPreserveRatio(false);

        // Add image as the first layer
    }



    public static void main(String[] args) { launch(args); }
}
