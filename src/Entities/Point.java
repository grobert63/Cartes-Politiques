package Entities;

/**
 * @author Théophile
 */
public class Point {

    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && equals((Point) obj);

    }

    public boolean equals(Point other) {
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return (int) ((x * 4957.0 + y) * 13.0);
    }

}
