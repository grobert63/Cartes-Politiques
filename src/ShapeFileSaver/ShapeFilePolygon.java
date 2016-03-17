package ShapeFileSaver;

/**
 * File : Saver.ShapeFilePolygon.java
 * Created by Guillaume Robert on 06/03/2016.
 * All Rights Reserved Guillaume Robert & Maxime Lemort & Julien Defiolles & Theophile Pumain
 */
class ShapeFilePolygon {
    private final ShapeFileBox box;
    private final int numberParts;
    private final int numberPoints;
    private final int[] Parts;
    private final ShapeFilePoint[] Points;

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
