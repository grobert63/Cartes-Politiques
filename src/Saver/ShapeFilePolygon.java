package Saver;

/**
 * File : Saver.ShapeFilePolygon.java
 * Created by Guillaume Robert on 06/03/2016.
 * All Rights Reserved Guillaume Robert & Maxime Lemort & Julien Defiolles & Theophile Pumain
 */
public class ShapeFilePolygon {
    private ShapeFileBox box;
    private int numberParts;
    private int numberPoints;
    private int Parts[];
    private ShapeFilePoint[] Points;

    public ShapeFilePolygon(ShapeFileBox box, int numberParts, int numberPoints, int[] parts, ShapeFilePoint[] points) {
        this.box = box;
        this.numberParts = numberParts;
        this.numberPoints = numberPoints;
        Parts = parts;
        Points = points;
    }

    public ShapeFilePolygon(ShapeFileBox box, int[] parts, ShapeFilePoint[] points) {
        this.box = box;
        numberParts = parts.length;
        numberPoints = points.length;
        Parts = parts;
        Points = points;
    }

    public ShapeFileBox getBox() {
        return box;
    }

    public int getNumberParts() {
        return numberParts;
    }

    public int getNumberPoints() {
        return numberPoints;
    }

    public int[] getParts() {
        return Parts;
    }

    public ShapeFilePoint[] getPoints() {
        return Points;
    }
}
