package LoggerUtils;

/**
 * File : LoggerUtils.AppletUtils.java
 * Created by Guillaume Robert on 14/03/2016.
 * All Rights Reserved Guillaume Robert and Maxime Lanouziere
 */
class AppletUtils {
    public static boolean insideApplet()
    {
        return null != System.getSecurityManager();
    }
}
