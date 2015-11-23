package GUI;

import Entities.Map;
import Entities.Region;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;
import javafx.scene.text.TextAlignment;

/**
 * Décrit un canvas spécialisé dans l'affichage de polygones
 */
public class PolyCanvas extends Canvas {
    private final double _canvasWidth;
    private final double _canvasHeight;
    private final Map _map;
    private final GraphicsContext _gc;

    /**
     * Canvas affichant la carte avec le nom de la région. Le ratio est toujours respecté
     * @param canvasWidth Largeur en pixels du canvas
     * @param canvasHeight Hauteur en pixels du canvas
     * @param map Structure Map contenant la carte à afficher
     */
    public PolyCanvas(double canvasWidth, double canvasHeight, Map map) {
        super(canvasWidth, canvasHeight);
        this._canvasWidth = canvasWidth;
        this._canvasHeight = canvasHeight;
        this._map = map;
        _gc = super.getGraphicsContext2D();
        double ratio = resize();
        boolean temp = false;
        _gc.setTextAlign(TextAlignment.CENTER);
        _gc.setTextBaseline(VPos.CENTER);
        for (Region region:map.getRegions()) {
            for (Polygon polygon : region.getBorders()) {
                int size = polygon.getPoints().size();
                double x[] = new double[size / 2], y[] = new double[size / 2];
                for (int i = 0; i < size; i++) {
                    if (temp) {
                        y[i / 2] = canvasHeight - polygon.getPoints().get(i) * ratio;
                    } else {
                        x[i / 2] = polygon.getPoints().get(i) * ratio;
                    }
                    temp = !temp;
                }
                _gc.strokePolygon(x,y,size/2);
            }

            _gc.fillText(region.getName(),region.getCenterX() * ratio, canvasHeight-region.getCenterY() * ratio);
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


}
