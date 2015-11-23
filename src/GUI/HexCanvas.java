package GUI;

import Entities.HexGrid;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * Décrit un canvas spécialisé dans l'affichage d'une grille hexagonale
 * @author Théophile
 */
public class HexCanvas extends Canvas{
    private final double hexWidth;
    private final double hexHeight;
        
    private final double[] hexCoordRelativeX = {0.5,0.5,0.0,-0.5,-0.5,0.0};
    private final double[] hexCoordRelativeY = {0.25,-0.25,-0.5,-0.25,0.25,0.5};
    
    /**
     * @param width Largeur en pixel
     * @param height Hauteur en pixel
     * @param grid Grille hexagonale contenant les régions à afficher
     */
    public HexCanvas(double width, double height, HexGrid grid){
        super(width, height);
        
        if(height/(grid.getHeight()*0.75 + 0.25) > width/(grid.getWidth())){
            this.hexHeight = height / ((3.0/4.0)*grid.getHeight() + 0.25);
            this.hexWidth = (Math.sqrt(3.0))/2.0 * hexHeight;
        }
        else{
            this.hexWidth = width / (grid.getWidth() + 0.5);
            this.hexHeight = hexWidth * 2.0/(Math.sqrt(3.0));
        }

        GraphicsContext gc = super.getGraphicsContext2D();
        int rgb = 0;
        
        for(int row = 0; row < grid.getHeight(); row++)
        {
            for(int col = 0; col < grid.getWidth(); col++)
            {
                if(grid.getRegion(col, row) != null){
                    gc.setFill(Color.rgb(255, rgb, rgb));
                    gc.fillPolygon(getHexCoordAbsoluteX(row,col), getHexCoordAbsoluteY(row), 6);
                    
                    gc.setFill(Color.BLACK);
                    gc.fillText(grid.getRegion(col, row).getName(),getTextPositionX(row, col), getTextPositionY(row));

                    //gc.strokePolygon(getHexCoordAbsoluteX(row,col), getHexCoordAbsoluteY(row), 6);
                    rgb = (rgb + 8) % 256;
                }
            }
        }
    }
    
    private double[] getHexCoordAbsoluteX(int row, int col){
        double decalage = 0.0;
        if(row % 2 == 0) decalage = 0.5;
        
        double[] hexCoordAbsoluteX = new double[6];
        
        for(int i = 0; i<6; i++){
            hexCoordAbsoluteX[i] = (hexCoordRelativeX[i] + col + 0.5 + decalage) * hexWidth;
        }
        return hexCoordAbsoluteX;
    }
    
    private double[] getHexCoordAbsoluteY(int row){
        
        double[] hexCoordAbsoluteY = new double[6];
        
        for(int i = 0; i<6; i++){
            hexCoordAbsoluteY[i] = (hexCoordRelativeY[i] + 0.75*row + 0.5) * hexHeight;
        }
        return hexCoordAbsoluteY;
    }
    
    private double getTextPositionX(int row, int col){
        double decalage = 0.0;
        if(row % 2 == 0) decalage = 0.5;
        
        return (col + decalage) * hexWidth;
    }
    
    private double getTextPositionY(int row){
        return (row * 0.75 + 0.5) * hexHeight;
    }
    
    /*
    // Constructeur test (outdated)
    public HexCanvas(double width, double height, int nbHexVertical, int nbHexHorizontal){
        super(width, height);
        this.hexHeight = height / ((3.0/4.0)*nbHexVertical + 0.25);
        this.hexWidth = (Math.sqrt(3.0))/2.0 * hexHeight;

        GraphicsContext gc = super.getGraphicsContext2D();
        int rgb = 0;
        
        for(int row = 0; row < nbHexVertical; row++)
        {
            for(int col = 0; col < nbHexHorizontal; col++)
            {
                gc.setFill(Color.rgb(255, rgb, rgb));
                gc.fillPolygon(getHexCoordAbsoluteX(row,col), getHexCoordAbsoluteY(row), 6);
                
                gc.setFill(Color.BLACK);
                gc.fillText("("+row+","+col+")",getTextPositionX(row, col), getTextPositionY(row));
                
                rgb = (rgb + 4) % 256;

                //gc.strokePolygon(getHexCoordAbsoluteX(row,col), getHexCoordAbsoluteY(row), 6);
            }
        }
    }
    */
}
