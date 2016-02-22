package Entities;

/**
 *
 * @author Théophile
 */
public class Point{

    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return (int)((x * 4957.0 + y)*13.0);
    }

    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }
}
