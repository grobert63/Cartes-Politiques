package DataManager;

import LoggerUtils.LoggerManager;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * File : Loader.Load.java
 * Created by Guillaume Robert on 08/12/2015.
 */
public class Load {

    public static List<File> loadMultiple(Stage stage) throws IOException {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir le fichier shapeFile");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Supported Files", "*.shp", "*.dbf"),
                new FileChooser.ExtensionFilter("Shapefile", "*.shp"),
                new FileChooser.ExtensionFilter("DataBase File", "*.dbf")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        if (list != null) {
            for (File file : list) {
                if (!file.exists())
                {
                    throw new IOException("impossible de lire le fichier : " + file.getName());
                }
            }
            String message = "Files : ";
            for (File file : list) {
                message += file.getName();
                if (list.size() - 1 > list.indexOf(file))
                {
                    message += ", ";
                }
            }
            message += " loaded";

            LoggerManager.getInstance().getLogger().log(Level.INFO,message);
        }

        return list;
    }

    public static File loadSingle(Window stage) throws IOException {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir le fichier shapeFile");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Supported Files", "*.shp", "*.dbf"),
                new FileChooser.ExtensionFilter("Shapefile", "*.shp"),
                new FileChooser.ExtensionFilter("DataBase File", "*.dbf")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(stage);
        if (file.exists())
        {
            LoggerManager.getInstance().getLogger().log(Level.INFO,"File : " + file.getName() + " loaded");
            return file;
        }
        throw new IOException("impossible de lire le fichier : " + file.getName());
    }

    public static File loadSingle(Window stage, FileChooser.ExtensionFilter extension,File directory) throws IOException {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir le fichier shapeFile");
        fileChooser.getExtensionFilters().addAll(extension);
        fileChooser.setInitialDirectory(directory);
        File file = fileChooser.showOpenDialog(stage);
        if (file.exists())
        {
            LoggerManager.getInstance().getLogger().log(Level.INFO,"File : " + file.getName() + " loaded");
            return file;
        }
        return null;
    }
}
