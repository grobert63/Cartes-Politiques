package DataManager;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * File : DataManager.Converter.java
 * Created by Guillaume Robert on 16/12/2015.
 */
public class Converter {

    public static Image CanvasToImage(Canvas canvas) {
        WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, image);
        return image;
    }
}
