package twostartercode;

import javafx.scene.paint.Color;

public class PaintCoordinate {
    private final double x;
    private final double y;
    private final Color color;

    public PaintCoordinate(double x, double y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }

    public Color getColor(){
        return this.color;
    }

}
