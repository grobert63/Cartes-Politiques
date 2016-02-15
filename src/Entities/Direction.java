package Entities;

import CustomException.InvalidDirectionException;

/**
 * Permet d'utiliser les directions lié aux arêtes d'un hexagone
 * @author Théophile
 */
public class Direction{
    public static final int NORTH_EAST = 0;
    public static final int EAST = 1;
    public static final int SOUTH_EAST = 2;
    public static final int SOUTH_WEST = 3;
    public static final int WEST = 4;
    public static final int NORTH_WEST = 5;
    
    /**
     * (Fonction inutilisée)
     * Indique l'angle lié à la direction donné
     * @param direction Un des membres de HexDirection.
     * @return Angle de cette direction en degré
     * @throws InvalidDirectionException
     */
    public static double getAngle(int direction) throws Exception{
        if(direction >= 0 && direction <= 5){
            return direction * 60.0 + 30.0;
        }
        throw new InvalidDirectionException("Une direction doit être comprise entre 0 et 5");
    }
    
    /**
     * (Fonction inutilisée)
     * Indique la direction opposée à une direction donnée
     * @param direction Direction donnée
     * @return Direction opposé à la direction donnée
     * @throws InvalidDirectionException
     */
    public static int getOpposite(int direction) throws Exception{
        if(direction >= 0 && direction <= 5){
            return (direction + 3) % 6;
        }
        throw new InvalidDirectionException("Une direction doit être comprise entre 0 et 5");
    }
    
    /**
     * Indique la direction la plus proche d'un angle donné.
     * En sachant 0° correspond au NORD et 90° à l'EST
     * @param angle Angle en degré
     * @return Direction la plus proche de l'angle
     */
    public static int getDirectionFromAngle(double angle){
        angle = angle % 360;
        return (int)(angle / 60.0);
    }
    
    public static double getDifferenceAngleDirection(double angle, int direction) throws Exception{
        return Math.abs(angle - getAngle(direction));
    }
}
