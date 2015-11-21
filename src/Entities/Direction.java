package Entities;

/**
 * Permet d'utiliser les directions lié aux arêtes d'un hexagone
 * @author Théophile
 */
public class Direction{
    public static final int NORTH_WEST = 0;
    public static final int WEST = 1;
    public static final int SOUTH_WEST = 2;
    public static final int SOUTH_EAST = 3;
    public static final int EAST = 4;
    public static final int NORTH_EAST = 5;
    
    /**
     * Indique l'angle lié à la direction donné
     * @param direction Un des membres de HexDirection.
     * @return Angle de cette direction en degré
     * @throws Exception 
     */
    public static double getAngle(int direction) throws Exception{
        if(direction >= 0 && direction <= 5){
            return direction * 60.0 + 30.0;
        }
        throw new Exception("Une direction doit être comprise entre 0 et 5");
    }
    
    /**
     * Indique la direction opposée à une direction donnée
     * @param direction Direction donnée
     * @return Direction opposé à la direction donnée
     * @throws Exception 
     */
    public static int getOpposite(int direction) throws Exception{
        if(direction >= 0 && direction <= 5){
            return (direction + 3) % 6;
        }
        throw new Exception("Une direction doit être comprise entre 0 et 5");
    }
}
