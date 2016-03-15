package GUI;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * File : GUI.CustomCanvas.java
 * Created by Guillaume Robert on 06/03/2016.
 * All Rights Reserved Guillaume Robert & Maxime Lemort & Julien Defiolles & Theophile Pumain
 */
abstract class CustomCanvas extends Canvas {
    private final IntegerProperty decalageX = new SimpleIntegerProperty();
    private final IntegerProperty decalageY = new SimpleIntegerProperty();
    private final DoubleProperty zoom = new SimpleDoubleProperty();
    double _canvasWidth;
    double _canvasHeight;
    private double oldX;
    private double oldY;

    CustomCanvas(double width, double height) {
        super(width, height);
    }

    CustomCanvas() {
        super();
    }

    private void setMouseReleasedEvent() {
        setOnMouseReleased(event -> {
            oldX = 0;
            oldY = 0;
        });
    }

    private void setMouseDraggedEvent() {
        setOnMouseDragged(event -> {
            int x = (int) (event.getX());
            int y = (int) (event.getY());
            if (oldX != 0) {
                decalageXProperty().setValue(getDecalageX() + x - oldX);
                decalageYProperty().setValue(getDecalageY() + y - oldY);
            }
            oldX = x;
            oldY = y;
        });
    }

    double getZoom() {
        return zoom.get();
    }

    private void setZoom(double zoom) {
        if (zoom >= 0.5)
            this.zoom.set(zoom);
    }

    public DoubleProperty zoomProperty() {
        return zoom;
    }

    int getDecalageX() {
        return decalageX.get();
    }

    private void setDecalageX() {
        this.decalageX.set(0);
    }

    private IntegerProperty decalageXProperty() {
        return decalageX;
    }

    int getDecalageY() {
        return decalageY.get();
    }

    private void setDecalageY() {
        this.decalageY.set(0);
    }

    private IntegerProperty decalageYProperty() {
        return decalageY;
    }

    void initialize() {
        setZoom(1);
        setDecalageX();
        setDecalageY();
    }

    protected abstract void draw();

    void setEvents() {
        decalageXProperty().addListener(evt -> draw());
        decalageYProperty().addListener(evt -> draw());
        setOnScroll(event -> setZoom(event.getDeltaY() / 200 + zoomProperty().getValue()));
        setMouseDraggedEvent();
        setMouseReleasedEvent();
    }

    void initializeGraphicsContext(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }
}
