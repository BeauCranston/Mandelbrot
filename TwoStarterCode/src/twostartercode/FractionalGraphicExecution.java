package twostartercode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FractionalGraphicExecution implements Runnable{

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
        this.ctx = ctx;
        this.fraction = fraction;
        this.step = step;
    }

    
    @Override
    public void run() {
        cd.setGraphicsContext(ctx);
        cd.setConvergenceSteps(this.convergenceSteps);
        System.out.println("hit run");
        System.out.println("thread " + step + " is running");
        double precision = Math.max((reMax - reMin) / canvasWidth, (imMax - imMin) / canvasHeight);
        int interval = (canvasWidth/fraction);
        System.out.println(interval);
        int ixR = step * interval;
        System.out.println(ixR);
        double xStartValue =  reMin + precision * interval * step;
        for (double c = xStartValue, xR = ixR; xR < ixR + interval; c = c + precision, xR++) {
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

