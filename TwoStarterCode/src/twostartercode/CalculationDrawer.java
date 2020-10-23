package twostartercode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicBoolean;

public class CalculationDrawer {
    private static double convergenceValue;
    private int convergenceSteps;
    private double c1;
    private double c2;
    private GraphicsContext ctx;
    private static final Object lock = new Object();
    private static AtomicBoolean calculationReady = new AtomicBoolean();

    public CalculationDrawer(GraphicsContext ctx){
        this.ctx = ctx;

    }
    public CalculationDrawer(){


    }

    public void setGraphicsContext(GraphicsContext ctx){
        this.ctx = ctx;
    }
    public void setConvergenceSteps(int convergenceSteps){
        this.convergenceSteps = convergenceSteps;
    }
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
    public void calculate(double ci, double c) throws InterruptedException{
        //System.out.println("ci " + ci + " c" + c );
        synchronized (lock){
            while(calculationReady.get() == true){
                lock.wait();
            }
            convergenceValue = checkConvergence(ci, c, convergenceSteps);
            double t1 = (double) convergenceValue / convergenceSteps;
            c1 = Math.min(255 * 2 * t1, 255);
            c2 = Math.max(255 * (2 * t1 - 1), 0);

            lock.notify();
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
            lock.notify();
            //System.out.println("consumed");
        }


    }

}
