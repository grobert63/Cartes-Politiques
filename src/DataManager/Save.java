package DataManager;

import LoggerUtils.LoggerManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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

    public static boolean saveToImage(Window stage, Image imageToSave) {
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
                return true;
            } catch (IOException e) {
                LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Unable to save image : " + e.getMessage());
                return false;
            }
        }
        return false;
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
