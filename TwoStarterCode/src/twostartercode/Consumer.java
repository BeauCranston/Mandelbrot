package twostartercode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Consumer implements Runnable {
    private ConcurrentLinkedQueue<PaintCoordinate> queue;
    private GraphicsContext ctx;
    public Consumer(ConcurrentLinkedQueue<PaintCoordinate> queue, GraphicsContext ctx){
        this.queue = queue;
        this.ctx = ctx;
    }
    @Override
    public void run() {
        while(queue.peek() != null){
            PaintCoordinate pc = queue.poll();
            ctx.setFill(pc.getColor());
            ctx.fillRect(pc.getX(), pc.getY(), 1, 1);
        }
        System.out.println(queue.size());


    }
}
