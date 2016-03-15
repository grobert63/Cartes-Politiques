package Entities;

import CustomException.InvalidDirectionException;

import java.util.ArrayList;

/**
 * Permet d'utiliser les directions lié aux arêtes d'un hexagone
 *
 * @author Théophile
 */
public class Direction {
    public static final int NORTH_EAST = 0;
    public static final int EAST = 1;
    public static final int SOUTH_EAST = 2;
    public static final int SOUTH_WEST = 3;
    public static final int WEST = 4;
    public static final int NORTH_WEST = 5;


    /**
     * (Fonction inutilisée)
     * Indique la direction opposée à une direction donnée
     *
     * @param direction Direction donnée
     * @return Direction opposé à la direction donnée
     * @throws InvalidDirectionException
     */
    public static int getOpposite(int direction) throws InvalidDirectionException {
        if (direction >= 0 && direction <= 5) {
            return (direction + 3) % 6;
        }
        throw new InvalidDirectionException();
    }

    /**
     * Indique la direction la plus proche d'un angle donné.
     * En sachant 0° correspond au NORD et 90° à l'EST
     *
     * @param angle Angle en degré
     * @return Direction la plus proche de l'angle
     */
    public static int getDirectionFromAngle(double angle) {
        angle = angle % 360;
        return (int) (angle / 60.0);
    }

    public static ArrayList<Integer> getAllDirection() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(NORTH_EAST);
        list.add(NORTH_WEST);
        list.add(WEST);
        list.add(SOUTH_EAST);
        list.add(SOUTH_WEST);
        list.add(EAST);
        return list;
    }
}
