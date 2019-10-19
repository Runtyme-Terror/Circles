package circles;

import java.util.stream.Stream;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Spinner;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

/**
 * A lab exercise to extend Java 8 lambdas and streams.
 * @author David Gagliardi
 */
public class Circles extends Application {
    
    public static final int ROWS = 5;
    public static final int COLS = 5;
    public static final int CELL_SIZE = 150;
    
    @Override
    public void start(Stage primaryStage) {
        root = new VBox();  
        controls = new HBox(10.0);
        
        root.setAlignment(Pos.CENTER);
        controls.setAlignment(Pos.CENTER);
        
        canvas = new Pane();
    
        cellSlider = new Slider(50,150,100);
        cellSlider.setPrefWidth(100);
        cellSlider.setShowTickLabels(true);
        cellSlider.setShowTickMarks(true);
        cellSlider.setBlockIncrement(1);
        
        rowSpinner = new Spinner<>(1,5,4);
        rowSpinner.setPrefWidth(75.0);
        
        colSpinner = new Spinner<>(1,5,5);
        colSpinner.setPrefWidth(75.0);
        
        xSpinner = new Spinner<>(-3,3,0);
        xSpinner.setPrefWidth(75.0);
        
        ySpinner = new Spinner<>(-3,3,0);
        ySpinner.setPrefWidth(75.0);        
        


        canvas.setPrefSize(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        
        initialCanvas();
        
        controls.getChildren().addAll(new Node[]{labelMaker("Rows", (Node)rowSpinner), labelMaker("Columns", 
                (Node)colSpinner), labelMaker("Cell Size", (Node)new HBox(new Node[]{cellSlider})), 
                labelMaker("X Scale", (Node)xSpinner), labelMaker("Y Scale", (Node)ySpinner)});
        
        root.getChildren().addAll(canvas, controls);
        
        rowSpinner.valueProperty().addListener((ev) -> { canvas.getChildren().clear(); 
           addAllRowsToCanvas(makeAllRows());});
        colSpinner.valueProperty().addListener((ev) -> { canvas.getChildren().clear(); 
           addAllRowsToCanvas(makeAllRows());});
        xSpinner.valueProperty().addListener((ev) -> { canvas.getChildren().clear(); 
           addAllRowsToCanvas(makeAllRows());});
        ySpinner.valueProperty().addListener((ev) -> { canvas.getChildren().clear(); 
           addAllRowsToCanvas(makeAllRows());});
        cellSlider.valueProperty().addListener((ev) -> { canvas.getChildren().clear(); 
           addAllRowsToCanvas(makeAllRows());});
        
        
        primaryStage.setTitle("Java 8 Lab Exercise");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        makeRow().forEach(x -> System.out.println(x));
        makeAllRows().forEach(r -> r.forEach(x -> System.out.println(x)));
    }
    
    private VBox labelMaker(String label, Node node) {
        VBox lNode = new VBox(10.0, new Node[]{new Label(label), node});
        lNode.setAlignment(Pos.CENTER);
        return lNode;
    }
    
    private void addToCanvas(Circle circle) {
        circle.setFill(new Color(Math.random(), Math.random(), Math.random(), 1.0));
        
        double fromX = -1*(col * cellSlider.getValue()) - 50;
        double fromY = -1*(row * cellSlider.getValue()) - 50;
        
        double toX = ((col + 1) * cellSlider.getValue()) - 50;
        double toY = ((row + 1) * cellSlider.getValue()) - 50;
        
        circle.setCenterX(fromX);
        circle.setCenterY(fromY);

        canvas.getChildren().add(circle);
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(500)) ;
        tt.setNode(circle);
        tt.setByX(toX - fromX);
        tt.setByY(toY - fromY);
        tt.play();
        
        ScaleTransition st = new ScaleTransition(Duration.millis(1000*(1+Math.random())));
        st.setNode(circle);
        st.setByX(xSpinner.getValue());
        st.setByY(ySpinner.getValue());
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
    }
    
    /**
     * This method adds the handler to the button that gives
     * this application its behavior.
     */
    private void initialCanvas() {
        Circle circle = new Circle();
        circle.setRadius(25.0f);
        
        canvas.getChildren().clear();
        addAllRowsToCanvas(makeAllRows());
    }
    
    public Stream<Circle> makeRow() {
        Stream<Circle> stream = Stream.generate(() ->
                new Circle(cellSlider.getValue() / 4)).limit(colSpinner.getValue());
        return stream;        
    }
    
    public void addRowToCanvas(Stream<Circle> s) {
        col = 0;
        
        s.forEach( c -> {addToCanvas(c); col++;});
    }
    
    public Stream<Stream<Circle>> makeAllRows() {
        Stream<Stream<Circle>> stream = Stream.generate(() -> makeRow()).limit(rowSpinner.getValue());
        
        return stream;
    }
    
    public void addAllRowsToCanvas(Stream<Stream<Circle>> ss) {
        row = 0;
        
        ss.forEach(r -> {addRowToCanvas(r); row++;});
    }
    
    private HBox controls;
    private VBox root;
    private Pane canvas;
    private Button starter;
    private Spinner<Integer> rowSpinner;
    private Spinner<Integer> colSpinner;
    private Spinner<Integer> xSpinner;
    private Spinner<Integer> ySpinner;
    private Slider cellSlider;
    private int row;
    private int col;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}