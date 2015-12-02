/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Théophile
 */
public class Point{

    static Point Zero = new Point(0.0,0.0);
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
        return !(this.x != other.x || this.y != other.y);
    }

    @Override
    public int hashCode() {
        return (int)(x * 43797 + y);
    }

    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }
}
