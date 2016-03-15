package DataManager;

import LoggerUtils.LoggerManager;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.logging.Level;

/**
 * File : Loader.Load.java
 * Created by Guillaume Robert on 08/12/2015.
 */
public class Load {

    public static File loadSingle(Window stage, FileChooser.ExtensionFilter extension, File directory) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir le fichier");
        fileChooser.getExtensionFilters().addAll(extension);
        fileChooser.setInitialDirectory(directory);
        File file = fileChooser.showOpenDialog(stage);
        if (file.exists()) {
            LoggerManager.getInstance().getLogger().log(Level.INFO, "File : " + file.getName() + " loaded");
            return file;
        }
        return null;
    }
}
