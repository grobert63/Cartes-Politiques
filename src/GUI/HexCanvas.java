package GUI;

import Entities.HexGrid;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Décrit un canvas spécialisé dans l'affichage d'une grille hexagonale
 * @author Théophile
 */
public class HexCanvas extends Canvas{
    private final IntegerProperty decalageX = new SimpleIntegerProperty();
    private final IntegerProperty decalageY = new SimpleIntegerProperty();
    private final DoubleProperty zoom = new SimpleDoubleProperty();
    private double oldX;
    private double oldY;

    private final HexPolygonContainer hexContainer;

    /**
     * @param width  Largeur en pixel
     * @param height Hauteur en pixel
     * @param grid   Grille hexagonale contenant les régions à afficher
     */
    public HexCanvas(double width, double height, HexGrid grid) {
        super(width, height);

        hexContainer = new HexPolygonContainer(grid, 1, width, height);
        setEvents();
    }

    private void setMouseReleasedEvent() {
        setOnMouseReleased(event -> {
            oldX = 0;
            oldY = 0;
        });
    }

    private void setMouseDraggedEvent() {
        setOnMouseDragged(event -> {
            int x =(int) (event.getX());
            int y =(int) (event.getY());
            if(oldX != 0) {
                decalageXProperty().setValue(getDecalageX() + x - oldX );
                decalageYProperty().setValue(getDecalageY() + y - oldY );
            }
            oldX = x;
            oldY = y;
        });
    }

    private void setEvents() {
        widthProperty().addListener(evt -> {
            hexContainer.notifyCanvasWidthChange(widthProperty().getValue());
            draw();
        });

        heightProperty().addListener(evt -> {
            hexContainer.notifyCanvasHeightChange(heightProperty().getValue());
            draw();
        });

        decalageXProperty().addListener(evt -> draw());
        decalageYProperty().addListener(evt -> draw());

        zoomProperty().addListener(evt -> {
            hexContainer.notifyZoomChange(zoomProperty().getValue());
            draw();
        });

        setOnScroll(event -> setZoom(event.getDeltaY()/200+zoomProperty().getValue()));
        setMouseDraggedEvent();
        setMouseReleasedEvent();
    }

    public void changeGrid(HexGrid grid) {
        hexContainer.notifyHexGridChange(grid);
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
        GraphicsContext gc = super.getGraphicsContext2D();
        int rgb = 0;
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,getWidth(),getHeight());

        int nbHexagones = hexContainer.size();

        for(int i=0; i<nbHexagones ; i++){
            gc.setFill(Color.rgb(255, rgb, rgb));
            gc.fillPolygon(hexContainer.getDrawableHexCoordX(i, getDecalageX()), hexContainer.getDrawableHexCoordY(i, getDecalageY()), 6);
            gc.setFill(Color.BLACK);
            gc.fillText(hexContainer.getRegion(i).getName(),hexContainer.getTextPositionX(i, getDecalageX()), hexContainer.getTextPositionY(i, getDecalageY()));

            rgb = (rgb + 8) % 224;
        }
    }
}
