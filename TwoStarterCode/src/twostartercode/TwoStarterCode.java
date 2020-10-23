/*
Original Code from:
http://www.hameister.org/JavaFX_MandelbrotSet.html

COMP 10183 Parallel Processing, Assignment Two, 2018
Take this sample program and convert it to a multi threaded structure.

 */
package twostartercode;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
 
public class TwoStarterCode extends Application {
    // Size of the canvas for the Mandelbrot set
    private static final int CANVAS_WIDTH = 700; 
    private static final int CANVAS_HEIGHT = 600;
    // Left and right border
    private static final int X_OFFSET = 25;
    // Top and Bottom border
    private static final int Y_OFFSET = 25;
    // Values for the Mandelbro set
    private static double MANDELBROT_RE_MIN = -2;  
    private static double MANDELBROT_RE_MAX = 1;       
    private static double MANDELBROT_IM_MIN = -1.2;    
    private static double MANDELBROT_IM_MAX = 1.2;      
 
    @Override
    public void start(Stage primaryStage) {
        Pane fractalRootPane = new Pane();
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        int numberOfThreads = 8;

        for(int i = 0; i < numberOfThreads; i++){
            Thread executionThread = new Thread (new FractionalGraphicExecution(canvas.getGraphicsContext2D(), 50, MANDELBROT_RE_MAX, MANDELBROT_RE_MIN, MANDELBROT_IM_MAX, MANDELBROT_IM_MIN, CANVAS_WIDTH, CANVAS_HEIGHT, numberOfThreads, i));
            executionThread.start();
            try {
                executionThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        fractalRootPane.getChildren().add(canvas);
 
        Scene scene = new Scene(fractalRootPane, CANVAS_WIDTH + 2 * X_OFFSET, CANVAS_HEIGHT + 2 * Y_OFFSET);
        scene.setFill(Color.BLACK);
        primaryStage.setTitle("Mandelbrot Set");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}