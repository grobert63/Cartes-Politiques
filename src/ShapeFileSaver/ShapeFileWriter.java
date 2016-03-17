package ShapeFileSaver;

import Entities.Point;
import GUI.HexPolygonContainer;
import LoggerUtils.LoggerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;

/**
 * File : Saver.ShapeFileWriter.java
 * Created by Guillaume Robert on 17/02/2016.
 */
public class ShapeFileWriter {

    private static final double NO_DATA = -1e38;
    private static final int INT = 4;
    private static final int DOUBLE = 8;
    private static final int DEFAULT_RESIZE_QUANTITY = 10000;
    private final ByteBuffer header;
    private final ShapeFileBox limits;
    private ByteBuffer body;
    private int recordCount = 0;
    private int fileSize = 0;
    private int bodySize = 10000;
    private int bodySpaceLeft;

    public ShapeFileWriter(File file, HexPolygonContainer hexPolygonContainer) {
        header = ByteBuffer.allocate(100);
        body = ByteBuffer.allocate(bodySize);
        bodySpaceLeft = bodySize;
        writeStaticHeaderInfos();
        fileSize = 50;
        limits = new ShapeFileBox(NO_DATA, NO_DATA, NO_DATA, NO_DATA);
        for (Point point : hexPolygonContainer.getHexNormalizeCoordCenter()) {
            ShapeFilePoint[] points = new ShapeFilePoint[7];
            final double[] hexEdgesX = {0.5, 0.5, 0.0, -0.5, -0.5, 0.0, 0.5};
            final double[] hexEdgesY = {0.25, -0.25, -0.5, -0.25, 0.25, 0.5, 0.25};
            for (int j = 0; j < 7; ++j) {
                points[j] = new ShapeFilePoint(point.x + hexEdgesX[j], -point.y + hexEdgesY[j]);
            }
            ShapeFilePolygon polygon = new ShapeFilePolygon(new ShapeFileBox(-0.5 * point.x, -0.5 * point.y, 0.5 * point.x, 0.5 * point.y), new int[]{0}, points);
            updateLimits(polygon.getBox());
            writePolygon(polygon);
        }
        writeDynamicHeaderInfos();
        writeToFile(file);
    }

    private void writeStaticHeaderInfos() {
        header.order(ByteOrder.BIG_ENDIAN);
        header.putInt(0, 9994);
        header.putInt(4, 0);
        header.putInt(8, 0);
        header.putInt(12, 0);
        header.putInt(16, 0);
        header.putInt(20, 0);
        header.order(ByteOrder.LITTLE_ENDIAN);
        header.putInt(28, 1000);
        header.putInt(32, 5);
        header.putDouble(68, NO_DATA);//ZMin
        header.putDouble(76, NO_DATA);//ZMin
        header.putDouble(84, NO_DATA);//MMax
        header.putDouble(92, NO_DATA);//MMax
    }

    private void writeDynamicHeaderInfos() {
        header.order(ByteOrder.BIG_ENDIAN);
        header.putInt(24, fileSize);//File Length
        header.order(ByteOrder.LITTLE_ENDIAN);
        header.putDouble(36, limits.getxMin());//XMin
        header.putDouble(44, limits.getyMin());//YMin
        header.putDouble(52, limits.getxMax());//XMax
        header.putDouble(60, limits.getyMax());//YMax
    }

    private void writePolygon(ShapeFilePolygon polygon) {
        //length : 16 bits words = 2 bytes // int : 4 bytes => 2 words //double 8 byte => 4 words
        int length = 4;//header length
        length += 16;//Box (4*4)
        length += 4;//numparts && numpints length (2*2)
        length += 2 * (polygon.getNumberParts() - 1);//parts length
        length += 8 * polygon.getNumberPoints();//points list length (2 + 2*4)
        writeRecordHeader(length);
        if (bodySpaceLeft < ((3 + polygon.getNumberParts()) * INT + (DOUBLE * 4))) {
            resizeBodyBuffer();
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
        for (ShapeFilePoint point : polygon.getPoints()) {
            writePoint(point);
        }
        fileSize += length;
    }

    private void writePoint(ShapeFilePoint point) {
        if (bodySpaceLeft < ((DOUBLE * 2))) {
            resizeBodyBuffer();
        }
        body.order(ByteOrder.LITTLE_ENDIAN);
        bodySpaceLeft -= DOUBLE;
        body.putDouble(point.getX());
        bodySpaceLeft -= DOUBLE;
        body.putDouble(point.getY());

    }

    private void writeBox(ShapeFileBox box) {
        if (bodySpaceLeft < DOUBLE * 4) {
            resizeBodyBuffer();
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

    private void writeRecordHeader(int contentLength) {
        if (bodySpaceLeft < INT * 2) {
            resizeBodyBuffer();
        }
        body.order(ByteOrder.BIG_ENDIAN);
        bodySpaceLeft -= INT;
        body.putInt(++recordCount);
        bodySpaceLeft -= INT;
        body.putInt(contentLength);
    }

    private void resizeBodyBuffer() {
        ByteBuffer temp = ByteBuffer.allocate(bodySize + ShapeFileWriter.DEFAULT_RESIZE_QUANTITY);
        temp.put(body);
        body = temp;
        bodySize = bodySize + DEFAULT_RESIZE_QUANTITY;
        bodySpaceLeft += DEFAULT_RESIZE_QUANTITY;
    }

    private void updateLimits(ShapeFileBox newPolygonLimits) {
        if (limits.getxMin() > newPolygonLimits.getxMin() || limits.getxMin() == NO_DATA) {
            limits.setxMin(newPolygonLimits.getxMin());
        }
        if (limits.getxMax() < newPolygonLimits.getxMax() || limits.getxMax() == NO_DATA) {
            limits.setxMax(newPolygonLimits.getxMax());
        }
        if (limits.getyMin() > newPolygonLimits.getyMin() || limits.getyMin() == NO_DATA) {
            limits.setyMin(newPolygonLimits.getyMin());
        }
        if (limits.getyMax() < newPolygonLimits.getyMax() || limits.getyMax() == NO_DATA) {
            limits.setyMax(newPolygonLimits.getyMax());
        }
    }

    private void writeToFile(File file) {
        try {
            FileOutputStream outputStream =
                    new FileOutputStream(file);
            outputStream.write(header.array());
            outputStream.write(body.array(), 0, bodySize - bodySpaceLeft);
            outputStream.close();
            LoggerManager.getInstance().getLogger().log(Level.INFO,"ShapeFile saved to : "+ file.getAbsolutePath() + " with a size of " + (header.array().length + bodySize - bodySpaceLeft) + " bytes");
        } catch (IOException e) {
            LoggerManager.getInstance().getLogger().log(Level.SEVERE, "Error while saving Shapefile to disk : " + e.getMessage());
        }
    }
}
