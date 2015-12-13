package LoggerUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * File : LoggerUtils.LoggerManager.java
 * Created by Guillaume Robert on 08/12/2015.
 */
public class LoggerManager {
    private static LoggerManager ourInstance = new LoggerManager();

    private static Logger _logger;

    public static LoggerManager getInstance() {
        return ourInstance;
    }

    private LoggerManager() {
        _logger = Logger.getLogger(LoggerManager.class.getName());
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat df = new SimpleDateFormat("ddMMyyyy");
        String path = "%t/Cartes-Politiques" + df.format(currentDate) + "%u.log";

        try {
            FileHandler handler = new FileHandler(path);
            handler.setFormatter(new LoggerUtils.LoggerFormatter());
            _logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Logger getLogger() {
        return _logger;
    }
}
