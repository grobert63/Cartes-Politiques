package LoggerUtils;

import java.util.logging.Logger;

/**
 * File : LoggerUtils.LoggerManager.java
 * Created by Guillaume Robert on 08/12/2015.
 */
public class LoggerManager {
    private static final LoggerManager ourInstance = new LoggerManager();

    private static Logger _logger;

    private LoggerManager() {
        _logger = Logger.getLogger(LoggerManager.class.getName());
        /*long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat df = new SimpleDateFormat("ddMMyyyy");
        String path = "%t/Cartes-Politiques" + df.format(currentDate) + "-%u.log";
        String path = "df.format(currentDate) + \"-%u.log";*/

        /*if (!(new File(path)).mkdirs())
        {
            _logger.log(Level.SEVERE, "Impossible de créer le repertoire de Log");
        }*/
        /*try {
            FileHandler handler = new FileHandler(path);
            handler.setFormatter(new LoggerUtils.LoggerFormatter());
            _logger.setUseParentHandlers(false);
            _logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static LoggerManager getInstance() {
        return ourInstance;
    }

    public Logger getLogger() {
        return _logger;
    }
}
