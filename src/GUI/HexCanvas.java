package GUI;

import Entities.HexGrid;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
/**
 * Décrit un canvas spécialisé dans l'affichage d'une grille hexagonale
 * @author Théophile
 */
public class HexCanvas extends Canvas{
    private final double[] hexCoordRelativeX = {0.5, 0.5, 0.0, -0.5, -0.5, 0.0};
    private final double[] hexCoordRelativeY = {0.25, -0.25, -0.5, -0.25, 0.25, 0.5};
    private final IntegerProperty decalageX = new SimpleIntegerProperty();
    private final IntegerProperty decalageY = new SimpleIntegerProperty();
    private final DoubleProperty zoom = new SimpleDoubleProperty();
    private double hexWidth;
    private double hexHeight;
    private double oldX;
    private double oldY;
    /**
     * @param width  Largeur en pixel
     * @param height Hauteur en pixel
     * @param grid   Grille hexagonale contenant les régions à afficher
     */
    public HexCanvas(double width, double height, HexGrid grid) {
        super(width, height);


        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
        decalageXProperty().addListener(evt -> draw());
        decalageYProperty().addListener(evt -> draw());
        zoomProperty().addListener(evt -> draw());

        setOnScroll(event -> zoomProperty().setValue(event.getDeltaY()/200+zoomProperty().getValue()));

        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x =(int) (event.getX()*getZoom());
                int y =(int) (event.getY()*getZoom());
                if(oldX != 0) {
                    decalageXProperty().setValue(getDecalageX() + x - oldX);
                    decalageYProperty().setValue(getDecalageY() + y - oldY);
                }
                oldX = x;
                oldY = y;

            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                oldX = 0;
                oldY = 0;
            }
        });
    }

    public double getZoom() {
        return zoom.get();
    }

    public void setZoom(double zoom) {
        this.zoom.set(zoom);
    }

    public DoubleProperty zoomProperty() {
        return zoom;
    }

    public int getDecalageX() {
        return decalageX.get();
    }

    public void setDecalageX(int decalageX) {
        this.decalageX.set(decalageX);
    }

    public IntegerProperty decalageXProperty() {
        return decalageX;
    }

    public int getDecalageY() {
        return decalageY.get();
    }

    public void setDecalageY(int decalageY) {
        this.decalageY.set(decalageY);
    }

    public IntegerProperty decalageYProperty() {
        return decalageY;
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
        if(getHeight()/(Main.grid.getHeight()*0.75 + 0.25) > getWidth()/(Main.grid.getWidth())){
            this.hexHeight = getHeight() / ((3.0/4.0)*Main.grid.getHeight() + 0.25)*getZoom();
            this.hexWidth = (Math.sqrt(3.0))/2.0 * hexHeight;
        }
        else{
            this.hexWidth = getWidth() / (Main.grid.getWidth() + 0.5)*getZoom();
            this.hexHeight = hexWidth * 2.0/(Math.sqrt(3.0));
        }

        GraphicsContext gc = super.getGraphicsContext2D();
        int rgb = 0;
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,getWidth(),getHeight());
        for(int row = 0; row < Main.grid.getHeight(); row++)
        {
            for(int col = 0; col < Main.grid.getWidth(); col++)
            {
                if(Main.grid.getRegion(col, row) != null){
                    gc.setFill(Color.rgb(255, rgb, rgb));
                    gc.fillPolygon(getHexCoordAbsoluteX(row,col), getHexCoordAbsoluteY(row), 6);

                    gc.setFill(Color.BLACK);
                    gc.fillText(Main.grid.getRegion(col, row).getName(),getTextPositionX(row, col), getTextPositionY(row));

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
            hexCoordAbsoluteX[i] = (hexCoordRelativeX[i] + col + 0.5 + decalage) * hexWidth+getDecalageX();
        }
        return hexCoordAbsoluteX;
    }
    
    private double[] getHexCoordAbsoluteY(int row){
        
        double[] hexCoordAbsoluteY = new double[6];
        
        for(int i = 0; i<6; i++){
            hexCoordAbsoluteY[i] = (hexCoordRelativeY[i] + 0.75*row + 0.5) * hexHeight+getDecalageY();
        }
        return hexCoordAbsoluteY;
    }
    
    private double getTextPositionX(int row, int col){
        double decalage = 0.0;
        if(row % 2 == 0) decalage = 0.5;
        
        return (col + decalage) * hexWidth+getDecalageX();
    }
    
    private double getTextPositionY(int row){
        return (row * 0.75 + 0.5) * hexHeight+getDecalageY();
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
