package Saver;

import GUI.HexPolygonContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * File : Saver.ShapeFileWriter.java
 * Created by Guillaume Robert on 17/02/2016.
 */
public class ShapeFileWriter {

    private ByteBuffer header;
    private ByteBuffer body;
    private int recordCount = 0;
    private int fileSize = 0;
    private ShapeFileBox limits;
    private int bodySize = 10000;
    private int bodySpaceLeft;
    private static final int INT = 4;
    private static final int DOUBLE = 8;
    private static final int DEFAULT_RESIZE_QUANTITY = 10000;

    public static final double NO_DATA = -1e38;
    public ShapeFileWriter(HexPolygonContainer hexPolygonContainer) {
        header = ByteBuffer.allocate(100);
        body = ByteBuffer.allocate(bodySize);
        bodySpaceLeft = bodySize;
        writeStaticHeaderInfos();
        fileSize = 50;
        int size = hexPolygonContainer.size();
        limits = new ShapeFileBox(NO_DATA,NO_DATA,NO_DATA,NO_DATA);
        for (int i = 0; i < size; i++)
        {
            double X[] = hexPolygonContainer.getDrawableHexCoordX(i,0);
            double[] minMaxX = minAndMaxValue(X);
            double Y[] = hexPolygonContainer.getDrawableHexCoordY(i,0);
            double[] minMaxY = minAndMaxValue(Y);
            ShapeFilePoint[] points = new ShapeFilePoint[X.length + 1];
            for (int j = 0; j < X.length; j++) {
                points[j] = new ShapeFilePoint(X[j],Y[j]);
            }
            points[points.length -1 ] = new ShapeFilePoint(X[0], Y[0]);
            ShapeFilePolygon polygon = new ShapeFilePolygon(new ShapeFileBox( minMaxX[0], minMaxY[0], minMaxX[1], minMaxY[1]), 1, points.length,new int[]{0},points);
            updateLimits(polygon.getBox());
            writePolygon(polygon);
        }
        writeDynamicHeaderInfos();
        writeToFile("test.shp");
    }

    private void writeStaticHeaderInfos()
    {
        header.order(ByteOrder.BIG_ENDIAN);
        header.putInt(0,9994);
        header.putInt(4,0);
        header.putInt(8,0);
        header.putInt(12,0);
        header.putInt(16,0);
        header.putInt(20,0);
        header.order(ByteOrder.LITTLE_ENDIAN);
        header.putInt(28,1000);
        header.putInt(32,5);
        header.putDouble(68,NO_DATA);//ZMin
        header.putDouble(76,NO_DATA);//ZMin
        header.putDouble(84,NO_DATA);//MMax
        header.putDouble(92,NO_DATA);//MMax
    }

    private void writeDynamicHeaderInfos()
    {
        header.order(ByteOrder.BIG_ENDIAN);
        header.putInt(24,fileSize);//File Length
        header.order(ByteOrder.LITTLE_ENDIAN);
        header.putDouble(36,limits.getxMin());//XMin
        header.putDouble(44,limits.getyMin());//YMin
        header.putDouble(52,limits.getxMax());//XMax
        header.putDouble(60,limits.getyMax());//YMax
    }

    private void writePolygon(ShapeFilePolygon polygon)
    {
        //length : 16 bits words = 2 bytes // int : 4 bytes => 2 words //double 8 byte => 4 words
        int length = 4;//header length
        length+= 16;//Box (4*4)
        length += 4;//numparts && numpints length (2*2)
        length += 2 * polygon.getNumberParts();//parts length
        length += 10 * polygon.getNumberPoints();//points list length (2 + 2*4)
        writeRecordHeader(length);
        if (bodySpaceLeft < ((3 + polygon.getNumberParts()) * INT + (DOUBLE * 4)))
        {
            resizeBodyBuffer(DEFAULT_RESIZE_QUANTITY);
        }
        body.order(ByteOrder.LITTLE_ENDIAN);
        bodySpaceLeft -= INT;
        body.putInt(5);
        writeBox(polygon.getBox());
        bodySpaceLeft -= INT;
        body.putInt(polygon.getNumberParts());
        bodySpaceLeft -= INT;
        body.putInt(polygon.getNumberPoints());
        for (int part : polygon.getParts()) {
            bodySpaceLeft -= INT;
            body.putInt(part);
        }
        for (ShapeFilePoint point : polygon.getPoints())
        {
            writePoint(point);
        }
        fileSize += length;
    }

    private void writePoint(ShapeFilePoint point)
    {
        //writeRecordHeader(4+2+8);
        if (bodySpaceLeft < (INT + (DOUBLE * 2)))
        {
            resizeBodyBuffer(DEFAULT_RESIZE_QUANTITY);
        }
        body.order(ByteOrder.LITTLE_ENDIAN);
        bodySpaceLeft -= INT;
        body.putInt(1);
        bodySpaceLeft -= DOUBLE;
        body.putDouble(point.getX());
        bodySpaceLeft -= DOUBLE;
        body.putDouble(point.getY());

    }

    private void writeBox(ShapeFileBox box)
    {
        if (bodySpaceLeft < DOUBLE * 4)
        {
            resizeBodyBuffer(DEFAULT_RESIZE_QUANTITY);
        }
        bodySpaceLeft -= DOUBLE;
        body.putDouble(box.getxMin());
        bodySpaceLeft -= DOUBLE;
        body.putDouble(box.getyMin());
        bodySpaceLeft -= DOUBLE;
        body.putDouble(box.getxMax());
        bodySpaceLeft -= DOUBLE;
        body.putDouble(box.getyMax());
    }

    private void writeRecordHeader(int contentLength)
    {
        if (bodySpaceLeft < INT * 2)
        {
            resizeBodyBuffer(DEFAULT_RESIZE_QUANTITY);
        }
        body.order(ByteOrder.BIG_ENDIAN);
        bodySpaceLeft -= INT;
        body.putInt(++recordCount);
        bodySpaceLeft -= INT;
        body.putInt(contentLength);
    }

    private int resizeBodyBuffer(int addSize)
    {
        ByteBuffer temp = ByteBuffer.allocate(bodySize + addSize);
        temp.put(body);
        body = temp;
        return bodySize + addSize;
    }

    private static double[] minAndMaxValue(double[] doubles) {
        double minMax[] = new double[2];
        minMax[0] = minMax[1] = doubles[0];
        for (double aDouble : doubles) {
            if (aDouble > minMax[1] || minMax[1] == NO_DATA) {
                minMax[1] = aDouble;
            }
            if (aDouble < minMax[0] || minMax[0] == NO_DATA) {
                minMax[0] = aDouble;
            }
        }
        return minMax;
    }

    private void updateLimits(ShapeFileBox newPolygonLimits)
    {
        if(limits.getxMin() > newPolygonLimits.getxMin() || limits.getxMin() == NO_DATA)
        {
            limits.setxMin(newPolygonLimits.getxMin());
        }
        if(limits.getxMax() < newPolygonLimits.getxMax() || limits.getxMax() == NO_DATA)
        {
            limits.setxMax(newPolygonLimits.getxMax());
        }
        if (limits.getyMin() > newPolygonLimits.getyMin() || limits.getyMin() == NO_DATA)
        {
            limits.setyMin(newPolygonLimits.getyMin());
        }
        if (limits.getyMax() < newPolygonLimits.getyMax() || limits.getyMax() == NO_DATA)
        {
            limits.setyMax(newPolygonLimits.getyMax());
        }
    }

    private void writeToFile(String filePath)
    {
        File f = new File(filePath);
        FileChannel channel;
        //header.flip();
        body.flip();
        try {
            channel = new FileOutputStream(f,false).getChannel();
            channel.write(header);
            channel.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
