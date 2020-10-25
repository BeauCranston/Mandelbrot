/*
Original Code from:
http://www.hameister.org/JavaFX_MandelbrotSet.html

COMP 10183 Parallel Processing, Assignment Two, 2018
Take this sample program and convert it to a multi threaded structure.

 */
package twostartercode;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.concurrent.ConcurrentLinkedQueue;

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
    private ConcurrentLinkedQueue<PaintCoordinate> queue = new ConcurrentLinkedQueue<>();
 
    @Override
    public void start(Stage primaryStage) {
        Pane fractalRootPane = new Pane();
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        int numberOfThreads = 2;
        for(int i = 0; i < numberOfThreads; i++){
            Thread prodThread = new Thread(new Producer(CANVAS_WIDTH, CANVAS_HEIGHT, MANDELBROT_RE_MAX, MANDELBROT_RE_MIN, MANDELBROT_IM_MAX, MANDELBROT_IM_MIN, numberOfThreads, i, queue));
            prodThread.start();

        }

        for(int i = 0; i < numberOfThreads; i++){

            Thread conThread = new Thread(new Consumer(queue, canvas.getGraphicsContext2D()));
            conThread.start();

        }


        //paintCoordinates(canvas.getGraphicsContext2D());
 
        fractalRootPane.getChildren().add(canvas);
 
        Scene scene = new Scene(fractalRootPane, CANVAS_WIDTH + 2 * X_OFFSET, CANVAS_HEIGHT + 2 * Y_OFFSET);
        scene.setFill(Color.BLACK);
        primaryStage.setTitle("Mandelbrot Set");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void paintCoordinates(GraphicsContext ctx){

        while(queue.peek() != null){
            PaintCoordinate pc = queue.poll();
            ctx.setFill(pc.getColor());
            ctx.fillRect(pc.getX(), pc.getY(), 1, 1);
        }
    }


    private void paintSet(GraphicsContext ctx, double reMin, double reMax, double imMin, double imMax, int fraction, int step, ConcurrentLinkedQueue<PaintCoordinate> queue) {
        double precision = Math.max((reMax - reMin) / CANVAS_WIDTH, (imMax - imMin) / CANVAS_HEIGHT);
        int convergenceSteps = 50;
        //get the interval of which each thread draws by
        int interval = (CANVAS_WIDTH/fraction);
        //System.out.println(interval);
        //get the start point of the interval
        int intervalStartPoint = step * interval;
        //System.out.println(intervalStartPoint);
        double xStartValue =  reMin + precision * interval * step; //get the reMin of the interval

        for (double c = xStartValue, xR = intervalStartPoint; xR < intervalStartPoint + interval; c = c + precision, xR++) {
            for (double ci = imMin, yR = 0; yR < CANVAS_HEIGHT; ci = ci + precision, yR++) {
                double convergenceValue = checkConvergence(ci, c, convergenceSteps);
                double t1 = (double) convergenceValue / convergenceSteps;
                double c1 = Math.min(255 * 2 * t1, 255);
                double c2 = Math.max(255 * (2 * t1 - 1), 0);
 
                if (convergenceValue != convergenceSteps) {
                    queue.add(new PaintCoordinate(xR,yR, Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0)));
                } else {
                    queue.add(new PaintCoordinate(xR,yR, Color.PURPLE)); // Convergence Color
                }

            }
        }
    }
 
    /**
     * Checks the convergence of a coordinate (c, ci) The convergence factor
     * determines the color of the point.
     */
    private int checkConvergence(double ci, double c, int convergenceSteps) {
        double z = 0;
        double zi = 0;
        for (int i = 0; i < convergenceSteps; i++) {
            double ziT = 2 * (z * zi);
            double zT = z * z - (zi * zi);
            z = zT + c;
            zi = ziT + ci;
 
            if (z * z + zi * zi >= 4.0) {
                return i;
            }
        }
        return convergenceSteps;
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}