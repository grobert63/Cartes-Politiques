package LoggerUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * File : LoggerUtils.LoggerFormatter.java
 * Created by Guillaume Robert on 08/12/2015.
 */
class LoggerFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        StringBuilder builder = new StringBuilder("[");
        builder.append(record.getLevel().getName());
        builder.append("] ");
        builder.append(dateFormat.format(new Date(record.getMillis())));
        builder.append(": ");
        if (!record.getMessage().equals(""))
        {
            try {
                builder.append(record.getResourceBundle().getString(record.getMessage()));
            } catch (Exception e) {
                builder.append(record.getMessage());
            }
        }
        builder.append("\r\n");
        if (record.getThrown() != null)
        {
            builder.append(record.getThrown().getLocalizedMessage());
            builder.append("\r\n");
        }
        return builder.toString();
    }
}
