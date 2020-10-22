package twostartercode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CalculationDrawer {
    private boolean consumeWaiting = true;
    private int convergenceValue = 0;
    private double c1 = 0;
    private double c2 = 0;
    private int convergenceSteps;
    public void calculate(double ci, double c, int convergenceSteps) throws InterruptedException{
        while(true) {

            synchronized (this) {
                this.convergenceSteps = convergenceSteps;
                while (!consumeWaiting) {
                    System.out.println("calculate waiting");
                    wait();
                }
                System.out.println("calculate executing");
                this.convergenceValue = checkConvergence(ci, c, convergenceSteps);
                double t1 = (double) convergenceValue / convergenceSteps;
                this.c1 = Math.min(255 * 2 * t1, 255);
                this.c2 = Math.max(255 * (2 * t1 - 1), 0);
                this.consumeWaiting = false;
                notify();
            }
        }

    }

    public void draw(GraphicsContext ctx, double xR, double yR) throws InterruptedException{
        while(true){
            synchronized (this){
                while(consumeWaiting){
                    System.out.println("draw waiting");
                    wait();
                }
                System.out.println("draw executing");
                if (convergenceValue != convergenceSteps) {
                    ctx.setFill(Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0));
                } else {
                    ctx.setFill(Color.PURPLE); // Convergence Color
                }
                ctx.fillRect(xR, yR, 1, 1);

                this.consumeWaiting = true;
                notify();
            }
        }
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
}
