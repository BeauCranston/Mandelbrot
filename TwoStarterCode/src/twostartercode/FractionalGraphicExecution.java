package twostartercode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FractionalGraphicExecution implements Runnable{
    /**
     * a class that handles drawing out a portion of a graphic. The fraction of the graphic that is drawn by the runnable is dependent on how many threads are executed.
     * If 10 threads are executed the class will draw 1/10th of the graphic for each thread running
     */
    private double reMax, reMin, imMax, imMin;
    private int convergenceSteps, canvasWidth, canvasHeight, fraction, step;
    private GraphicsContext ctx;
    private CalculationDrawer cd = new CalculationDrawer();
    public FractionalGraphicExecution(GraphicsContext ctx, int convergenceSteps, double reMax, double reMin, double imMax, double imMin, int canvasWidth, int canvasHeight, int fraction, int step){
        this.convergenceSteps = convergenceSteps;
        this.reMax = reMax;
        this.reMin = reMin;
        this.imMax = imMax;
        this.imMin = imMin;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        //the context of the graphic
        this.ctx = ctx;
        //fraction in which to divide the work by
        this.fraction = fraction;
        //the current step that the thread is on
        this.step = step;
    }

    
    @Override
    public void run() {
        //set variables on calculation drawer
        cd.setGraphicsContext(ctx);
        cd.setConvergenceSteps(this.convergenceSteps);
        System.out.println("hit run");
        System.out.println("thread " + step + " is running");
        //get the precision
        double precision = Math.max((reMax - reMin) / canvasWidth, (imMax - imMin) / canvasHeight);
        //get the interval of which each thread draws by
        int interval = (canvasWidth/fraction);
        //System.out.println(interval);
        //get the start point of the interval
        int intervalStartPoint = step * interval;
        //System.out.println(intervalStartPoint);
        double xStartValue =  reMin + precision * interval * step; //get the reMin of the interval
        System.out.println(xStartValue);
        //for each pixel in the interval, calculate the colour and then paint at the specific position
        for (double c = xStartValue, xR = intervalStartPoint; xR < intervalStartPoint + interval; c = c + precision, xR++) {
            for (double ci = imMin, yR = 0; yR < canvasHeight; ci = ci + precision, yR++) {
                try {
                    cd.calculate(ci, c);
                    cd.paint(xR, yR);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }

    }
}

