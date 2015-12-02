package Entities;

/**
 * Décrit une grille hexagonale contenant des régions
 * @author Théophile
 */
public class HexGrid {
    private final int width;
    private final int height;
    private Region[][] array;
    private int nbRegions = 0;

    /**
     * Construit la grille avec sa hauteur et sa largeur.
     * @param width Largeur de la grille ou nombre d'hexagones horizontaux
     * @param height Hauteur de la grille ou nombre d'hexagones verticaux
     */
    public HexGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.array = new Region[width][height];
    }

    /**
     * @return Largeur de la grille
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return Hauteur de la grille
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Récupère la région aux indexs indiqués dans la grille
     * @param x Index en X / Numéro de colonne 
     * @param y Index en Y / Numéro de ligne
     * @return Région aux index indiqués
     */
    public Region getRegion(int x, int y){
        return array[x][y];
    }
    
    /**
     * Ajoute une région dans la grille aux indexs indiqués
     * @param x Index en X / Numéro de colonne 
     * @param y Index en Y / Numéro de ligne
     * @param r Région à ajouter dans la grille
     */
    public void addRegion(int x, int y, Region r){
        array[x][y] = r;
        nbRegions++;
    }
    
    public int getNbRegions(){
        return nbRegions;
    }
}
