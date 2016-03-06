package GUI;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;

/**
 * File : GUI.CustomCanvas.java
 * Created by Guillaume Robert on 06/03/2016.
 * All Rights Reserved Guillaume Robert & Maxime Lemort & Julien Defiolles & Theophile Pumain
 */
public abstract class CustomCanvas extends Canvas{
    private final DoubleProperty zoom = new SimpleDoubleProperty();
    protected final IntegerProperty decalageX = new SimpleIntegerProperty();
    protected final IntegerProperty decalageY = new SimpleIntegerProperty();
    protected double _canvasWidth;
    protected double _canvasHeight;
    protected double oldX;
    protected double oldY;

    public CustomCanvas(double width, double height) {
        super(width, height);
    }

    public CustomCanvas() {
        super();
    }

    protected void setMouseReleasedEvent() {
        setOnMouseReleased(event -> {
            oldX = 0;
            oldY = 0;
        });
    }

    protected void setMouseDraggedEvent() {
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

    public double getZoom() {
        return zoom.get();
    }

    public void setZoom(double zoom) {
        if(zoom>=0.5)
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

    public void initialize()
    {
        setZoom(1);
        setDecalageX(0);
        setDecalageY(0);
    }

    public abstract void draw();

    protected void setEvents() {
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
        decalageXProperty().addListener(evt -> draw());
        decalageYProperty().addListener(evt -> draw());
        zoomProperty().addListener(evt -> draw());
        setOnScroll(event -> setZoom(event.getDeltaY()/200+zoomProperty().getValue()));
        setMouseDraggedEvent();
        setMouseReleasedEvent();
    }
}
