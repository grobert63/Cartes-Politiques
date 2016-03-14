package LoggerUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.AccessControlException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File : LoggerUtils.LoggerManager.java
 * Created by Guillaume Robert on 08/12/2015.
 */

/**
 * Handles the logging for the whole app
 */
public class LoggerManager {
    private static final LoggerManager ourInstance = new LoggerManager();

    private static Logger _logger;

    private LoggerManager() {
        _logger = Logger.getLogger(LoggerManager.class.getName());
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat df = new SimpleDateFormat("ddMMyyyy");
        try {
            String path = System.getProperty("user.dir");
            String separator = System.getProperty("file.separator");
            String filename = df.format(currentDate) + "-%u.log";
            String appDirectory = separator + "Cartes-Politiques" + separator;
            if (OSDetector.isWindows()) {
                path = System.getProperty("java.io.tmpdir") + appDirectory;
            }
            if (OSDetector.isUnix()) {
                path = separator + "var" + separator + "log" + appDirectory;
            }
            if (OSDetector.isMac()) {
                path = System.getProperty("user.home") + appDirectory;
            }
            if (!Files.isDirectory(Paths.get(path))) {
                if (!(new File(path)).mkdirs()) {
                    _logger.log(Level.SEVERE, "Impossible de créer le repertoire de Log");
                }
            }
            path += filename;
            FileHandler handler = new FileHandler(path);
            handler.setFormatter(new LoggerUtils.LoggerFormatter());
            _logger.setUseParentHandlers(true);
            _logger.addHandler(handler);
        } catch (AccessControlException e) {
            System.out.println("Impossible de logger");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gives the log manager to use
     *
     * @return the log manager
     */
    public static LoggerManager getInstance() {
        return ourInstance;
    }

    /**
     * Gives the logger to use to log infos
     *
     * @return the logger
     */
    public Logger getLogger() {
        return _logger;
    }
}
