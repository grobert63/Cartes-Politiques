package LoggerUtils;

/**
 * File : ${PACKAGE_NAME}.${NAME}.java
 * Created by Guillaume Robert on 13/02/2016.
 * All Rights Reserved Guillaume Robert & Maxime Lemort & Julien Defiolles & Theophile Pumain
 */

/**
 * Allows to recognize major Operating Systems
 */
public class OSDetector {
    private static String OS = System.getProperty("os.name").toLowerCase();


    /**
     * @return true if OS is windows
     */
    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    /**
     * @return true if OS is Mac OS
     */
    public static boolean isMac() {
        return (OS.contains("mac"));
    }

    /**
     * @return true if OS is Unix based (inlcudes Linux)
     */
    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    }
}
