package twostartercode;

import javafx.scene.canvas.GraphicsContext;

public class PainterExecutable implements Runnable {
    private double reMax, reMin, imMax, imMin;
    private int canvasW, canvasH, convergenceSteps;;
    private CalculationDrawer calcDraw;
    private GraphicsContext ctx;
    public PainterExecutable(GraphicsContext ctx, double reMax, double reMin,double imMax, double imMin, int convergenceSteps, int CANVAS_HEIGHT, int CANVAS_WIDTH, CalculationDrawer calcDraw ){
        this.calcDraw = calcDraw;
        this.ctx = ctx;
        this.reMax = reMax;
        this.reMin = reMin;
        this.imMax = imMax;
        this.imMin = imMin;
        this.convergenceSteps = convergenceSteps;
        this.canvasH = CANVAS_HEIGHT;
        this.canvasW = CANVAS_WIDTH;
    }
    @Override
    public void run() {
        double precision = Math.max((reMax - reMin) / canvasW, (imMax - imMin) / canvasH);
        for (double c = reMin, xR = 0; xR < canvasW; c = c + precision, xR++) {
            for (double ci = imMin, yR = 0; yR < canvasH; ci = ci + precision, yR++) {
                try {
                    calcDraw.calculate(ci, c, convergenceSteps);
                    calcDraw.draw(ctx,xR, yR);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
