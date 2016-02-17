package Saver;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * File : Saver.ShapeFileWriter.java
 * Created by Guillaume Robert on 17/02/2016.
 */
public class ShapeFileWriter {

    ByteBuffer header;

    public static final double NO_DATA = -1e38;
    public ShapeFileWriter() {
        header = ByteBuffer.allocate(100);
        header.order(ByteOrder.BIG_ENDIAN);
        header.putInt(0,9994);
        header.putInt(4,0);
        header.putInt(8,0);
        header.putInt(12,0);
        header.putInt(16,0);
        header.putInt(20,0);
        header.putInt(24,50);//File Length
        header.order(ByteOrder.LITTLE_ENDIAN);
        header.putInt(28,1000);
        header.putInt(32,5);
        header.putDouble(36,0);//XMin
        header.putDouble(44,0);//YMin
        header.putDouble(52,0);//XMax
        header.putDouble(60,0);//YMax
        header.putDouble(68,NO_DATA);//ZMin
        header.putDouble(76,NO_DATA);//ZMin
        header.putDouble(84,NO_DATA);//MMax
        header.putDouble(92,NO_DATA);//MMax
    }
}
