package GUI;

import Entities.Boundary;
import Entities.GeoMap;
import Entities.Region;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Décrit un canvas spécialisé dans l'affichage de polygones
 */
public class PolyCanvas extends CustomCanvas {
    private final BooleanProperty nomPays = new SimpleBooleanProperty();
    private GeoMap map;
    private double _ratio;

    /**
     * Canvas affichant la carte avec le nom de la région. Le ratio est toujours respecté
     *
     * @param map Structure Map contenant la carte à afficher
     */
    public PolyCanvas(GeoMap map) {
        super();
        this.map = map;
        setEvents();
    }

    @Override
    void setEvents() {
        super.setEvents();
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
        zoomProperty().addListener(evt -> draw());
        nomPaysProperty().addListener(evt -> draw());
    }

    public void changeMap(GeoMap map) {
        this.map = map;
        initialize();
        draw();
    }

    private boolean getNomPays() {
        return nomPays.get();
    }

    public BooleanProperty nomPaysProperty() {
        return nomPays;
    }

    public void draw() {
        _canvasWidth = getWidth();
        _canvasHeight = getHeight();
        GraphicsContext gc = super.getGraphicsContext2D();
        _ratio = resize();
        drawInitialize(gc);
        for (Boundary b : map.getSimpleBoundaries()) {
            drawPolygon(gc, b);
        }
        if (getNomPays())
            for (Region region : map.getRegions()) {
                gc.fillText(region.getName(), computeX(region.getCenter().x), computeY(region.getCenter().y));
            }
    }

    private void drawInitialize(GraphicsContext gc) {
        initializeGraphicsContext(gc);
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
    }

    private void drawPolygon(GraphicsContext gc, Boundary b) {
        int size = b.getPoints().size();
        double x[] = new double[size];
        double y[] = new double[size];
        for (int i = 0; i < size; i++) {
            y[i] = computeY(b.getPoints().get(i).y);
            x[i] = computeX(b.getPoints().get(i).x);
        }

        gc.strokePolyline(x, y, size);
    }


    private double computeX(double x) {
        return x * _ratio * getZoom() + getDecalageX() + (_canvasWidth / 2) * (1 - getZoom());
    }

    private double computeY(double y) {
        return -y * _ratio * getZoom() + getDecalageY() + (_canvasHeight / 2) * (getZoom() + 1);
    }

    private double resize() {
        double ratioX, ratioY;
        ratioX = _canvasWidth / map.getWidth();
        ratioY = _canvasHeight / map.getHeight();
        if (ratioX < 1 && ratioY < 1) {
            ratioX = map.getWidth() / _canvasWidth;
            ratioY = map.getHeight() / _canvasHeight;
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
