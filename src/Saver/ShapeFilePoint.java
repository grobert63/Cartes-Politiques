package Saver;

/**
 * File : Saver.ShapeFilePoint.java
 * Created by Guillaume Robert on 06/03/2016.
 * All Rights Reserved Guillaume Robert & Maxime Lemort & Julien Defiolles & Theophile Pumain
 */
public class ShapeFilePoint {
    private double x;
    private double y;

    public ShapeFilePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
