package GUI;

import Entities.Map;
import Entities.Region;
import javafx.beans.property.*;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.TextAlignment;

/**
 * Décrit un canvas spécialisé dans l'affichage de polygones
 */
public class PolyCanvas extends Canvas {
    private final Map _map;
    private final IntegerProperty decalageX = new SimpleIntegerProperty();
    private final IntegerProperty decalageY = new SimpleIntegerProperty();
    private final DoubleProperty zoom = new SimpleDoubleProperty();
    private final BooleanProperty nomPays = new SimpleBooleanProperty();
    private  double _canvasWidth;
    private  double _canvasHeight;

    /**
     * Canvas affichant la carte avec le nom de la région. Le ratio est toujours respecté
     *
     * @param map Structure Map contenant la carte à afficher
     */
    public PolyCanvas(Map map) {
        super();

        this._map = map;


        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
        decalageXProperty().addListener(evt -> draw());
        decalageYProperty().addListener(evt -> draw());
        nomPaysProperty().addListener(evt -> draw());
        zoomProperty().addListener(evt -> draw());
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

    public boolean getNomPays() {
        return nomPays.get();
    }

    public void setNomPays(boolean nomPays) {
        this.nomPays.set(nomPays);
    }

    public BooleanProperty nomPaysProperty() {
        return nomPays;
    }

    private void draw()
    {
        _canvasWidth = getWidth();
        _canvasHeight = getHeight();
        GraphicsContext gc = super.getGraphicsContext2D();
        double ratio = resize();
        boolean temp = false;

        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,getWidth(),getHeight());
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        for (Region region:_map.getRegions()) {
            for (Polygon polygon : region.getBoundaries()) {
                int size = polygon.getPoints().size();
                double x[] = new double[size / 2], y[] = new double[size / 2];
                for (int i = 0; i < size; i++) {
                    if (temp) {
                        y[i / 2] =(_canvasHeight- polygon.getPoints().get(i) + getDecalageY())*ratio*getZoom();
                    } else {
                        x[i / 2] =( polygon.getPoints().get(i)  + getDecalageX())*ratio*getZoom();
                    }
                    temp = !temp;
                }
                gc.strokePolygon(x,y,size/2);
            }
            if(getNomPays())
                gc.fillText(region.getName(),(region.getCenter().x + getDecalageX())*getZoom()*ratio, ((_canvasHeight- region.getCenter().y  + getDecalageY()))*getZoom()*ratio);
        }
    }
    private double resize() {
        double ratioX, ratioY;
        ratioX = _canvasWidth / _map.getWidth();
        ratioY = _canvasHeight / _map.getHeight();
        if (ratioX < 1 && ratioY < 1)
        {
            ratioX = _map.getWidth() / _canvasWidth;
            ratioY = _map.getHeight() / _canvasHeight;
        }
        return ratioX < ratioY ? ratioX : ratioY;
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

}
