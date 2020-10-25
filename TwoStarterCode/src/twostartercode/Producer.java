package twostartercode;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Producer implements Runnable {
    private double reMax, reMin, imMax, imMin;
    private int convergenceSteps, canvasWidth, canvasHeight, fraction, step;
    private ConcurrentLinkedQueue<PaintCoordinate> queue;
    public Producer(int canvasWidth, int canvasHeight, double reMax, double reMin, double imMax, double imMin, int fraction, int step, ConcurrentLinkedQueue<PaintCoordinate> queue){
        this.reMax = reMax;
        this.reMin = reMin;
        this.imMax = imMax;
        this.imMin = imMin;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.fraction = fraction;
        this.step = step;
        this.queue = queue;
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
    @Override
    public void run() {
        double precision = Math.max((reMax - reMin) / canvasWidth, (imMax - imMin) / canvasHeight);
        int convergenceSteps = 50;
        //get the interval of which each thread draws by
        int interval = (canvasWidth/fraction);
        //System.out.println(interval);
        //get the start point of the interval
        int intervalStartPoint = step * interval;
        //System.out.println(intervalStartPoint);
        double xStartValue =  reMin + precision * interval * step; //get the reMin of the interval

        for (double c = xStartValue, xR = intervalStartPoint; xR < intervalStartPoint + interval; c = c + precision, xR++) {
            for (double ci = imMin, yR = 0; yR < canvasHeight; ci = ci + precision, yR++) {
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
        //System.out.println(queue.size());
    }
}
