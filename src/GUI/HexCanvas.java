package GUI;

import Entities.HexGrid;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * Décrit un canvas spécialisé dans l'affichage d'une grille hexagonale
 * @author Théophile
 */
public class HexCanvas extends CustomCanvas {
    private final double[] hexCoordRelativeX = {0.5, 0.5, 0.0, -0.5, -0.5, 0.0};
    private final double[] hexCoordRelativeY = {0.25, -0.25, -0.5, -0.25, 0.25, 0.5};
    private HexGrid grid;
    /**
     * @param width  Largeur en pixel
     * @param height Hauteur en pixel
     * @param grid   Grille hexagonale contenant les régions à afficher
     */
    public HexCanvas(double width, double height, HexGrid grid) {
        super(width, height);

        this.grid = grid;
        setEvents();
    }

    public void changeGrid(HexGrid grid) {
        this.grid = grid;
        initialize();
        draw();

    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    public void draw()
    {
        drawInitialize();
        GraphicsContext gc = super.getGraphicsContext2D();
        int rgb = 0;
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,getWidth(),getHeight());
        for(int row = 0; row < grid.getHeight(); row++)
        {
            rgb = drawPolygon(gc, rgb, row);
        }
    }

    private void drawInitialize() {
        if(getHeight()/(grid.getHeight()*0.75 + 0.25) > getWidth()/(grid.getWidth())){
            this._canvasHeight = getHeight() / ((3.0/4.0)*grid.getHeight() + 0.25)*getZoom();
            this._canvasWidth = (Math.sqrt(3.0))/2.0 * _canvasHeight;
        }
        else{
            this._canvasWidth = getWidth() / (grid.getWidth() + 0.5)*getZoom();
            this._canvasHeight = _canvasWidth * 2.0/(Math.sqrt(3.0));
        }
    }

    private int drawPolygon(GraphicsContext gc, int rgb, int row) {
        for(int col = 0; col < grid.getWidth(); col++)
        {
            if(grid.getRegion(col, row) != null){
                gc.setFill(Color.rgb(255, rgb, rgb));
                gc.fillPolygon(getHexCoordAbsoluteX(row,col), getHexCoordAbsoluteY(row), 6);
                gc.setFill(Color.BLACK);
                gc.fillText(grid.getRegion(col, row).getName(),getTextPositionX(row, col), getTextPositionY(row));
                rgb = (rgb + 8) % 256;
            }
        }
        return rgb;
    }

    private double[] getHexCoordAbsoluteX(int row, int col){
        double decalage = 0.0;
        if(row % 2 == 0) decalage = 0.5;
        
        double[] hexCoordAbsoluteX = new double[6];
        
        for(int i = 0; i<6; i++){
            hexCoordAbsoluteX[i] = (hexCoordRelativeX[i] + col + 0.5 + decalage) * _canvasWidth +getDecalageX() +(getWidth() / 2)*(1- getZoom());
        }
        return hexCoordAbsoluteX;
    }
    
    private double[] getHexCoordAbsoluteY(int row){
        
        double[] hexCoordAbsoluteY = new double[6];
        
        for(int i = 0; i<6; i++){
            hexCoordAbsoluteY[i] = (hexCoordRelativeY[i] + 0.75*row + 0.5) * _canvasHeight +getDecalageY() +(getHeight() / 2)*(1- getZoom());
        }
        return hexCoordAbsoluteY;
    }
    
    private double getTextPositionX(int row, int col){
        double decalage = 0.0;
        if(row % 2 == 0) decalage = 0.5;
        
        return (col + decalage) * _canvasWidth +getDecalageX() +(getWidth() / 2)*(1- getZoom());
    }
    
    private double getTextPositionY(int row){
        return (row * 0.75 + 0.5) * _canvasHeight +getDecalageY()+(getHeight() / 2)*(1- getZoom());
    }


}
