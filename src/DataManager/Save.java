package DataManager;

import GUI.HexPolygonContainer;
import LoggerUtils.LoggerManager;
import ShapeFileSaver.ShapeFileWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * File : DataManager.Save.java
 * Created by Guillaume Robert on 08/12/2015.
 */
public class Save {

    public static void saveToImage(Window stage, Image imageToSave) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir où enregistrer le fichier");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Supported Files *.png *.jpg *.jpeg *.gif", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            String type;
            switch (getExtension(file).toLowerCase()) {
                case "jpeg":
                case "jpg":
                    type = "jpg";
                    break;
                case "gif":
                    type = "gif";
                    break;
                default:
                    type = "png";
                    break;
            }
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(imageToSave, null), type, file);
                LoggerManager.getInstance().getLogger().log(Level.INFO, "Image saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Unable to save image : " + e.getMessage());
            }
        }
    }

    public static void saveToShp(Window stage, HexPolygonContainer polygonContainer) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir où enregistrer le fichier");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Supported Files *.shp","*.shp"),
                new FileChooser.ExtensionFilter("ShapeFile", "*.shp")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            new ShapeFileWriter(file,polygonContainer);
        }
    }

    private static String getExtension(File file) {
        String name = file.getName();
        if (name.lastIndexOf(".") > 0) {
            return name.substring(name.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
}
