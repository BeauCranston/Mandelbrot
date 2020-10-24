package twostartercode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is used to do make sure that the calculate and the draw happens in the correct order so that there is no
 * conflicts with the calculations. I also made sure that i did not cause dead lock by using a private static final lock object to bounce between processes at the appropriate times.
 *
 * How it works is that the calculation will execute, notify the paitnt method that the calculation is ready, and then the paint method will paint with the calculated values.
 */
public class CalculationDrawer {
    private static double convergenceValue;
    private int convergenceSteps;
    private double c1;
    private double c2;
    private GraphicsContext ctx;
    private static final Object lock = new Object();
    private static final
    AtomicBoolean calculationReady = new AtomicBoolean();

    //default constructor
    public CalculationDrawer(){


    }

    //set the graphics context for the painting
    public void setGraphicsContext(GraphicsContext ctx){
        this.ctx = ctx;
    }
    //set the convergence steps
    public void setConvergenceSteps(int convergenceSteps){
        this.convergenceSteps = convergenceSteps;
    }
    //check what step the convergence is at and return its value
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

    /**
     * calculates the convergence value which then uses the value to get the colour of the coordinate.
     * The method will only run if the paint method is not in the process of placing the point onto the canvas.
     *
     * If the method is running it will wake up the paint method using a shared lock object
     * @param ci convergence imaginary
     * @param c convergence real
     * @throws InterruptedException signals threads to wake up when thrown
     */
    public void calculate(double ci, double c) throws InterruptedException{
        //System.out.println("ci " + ci + " c" + c );
        //this is done outside of the synchronized block for more concurrency
        int tempConvergence = checkConvergence(ci, c, convergenceSteps);
        double t1 = (double) tempConvergence / convergenceSteps;
        synchronized (lock){
            //if the atomic boolean is true then relinquish control of the lock object to the paint method
            while(calculationReady.get() == true){
                lock.wait();
            }
            //set convergence value inside synchronized block since it is shared among the paint and calculate methods
            convergenceValue = tempConvergence;
            //this also needs to be called in the synchronized block to keep the t1 value in sync with the convergence value

            c1 = Math.min(255 * 2 * t1, 255);
            c2 = Math.max(255 * (2 * t1 - 1), 0);

            lock.notifyAll();
            calculationReady.set(true);
            //System.out.println("produced");
        }

    }

    public void paint(double xR, double yR) throws InterruptedException{

        synchronized (lock){
            while(calculationReady.get() == false){
                lock.wait();
            }
            //System.out.println("running paint");
            //System.out.println("convergence value " + convergenceValue);
            if (convergenceValue != convergenceSteps) {
                ctx.setFill(Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0));
            } else {
                ctx.setFill(Color.PURPLE); // Convergence Color
            }
            ctx.fillRect(xR, yR, 1, 1);

            calculationReady.set(false);
            lock.notifyAll();
            //System.out.println("consumed");
        }


    }

}
