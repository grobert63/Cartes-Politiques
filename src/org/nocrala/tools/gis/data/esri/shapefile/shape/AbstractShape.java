package org.nocrala.tools.gis.data.esri.shapefile.shape;

import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;

import java.io.InputStream;

public abstract class AbstractShape {

  protected org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeHeader header;
  protected org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeType shapeType;

  public AbstractShape(final org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeHeader shapeHeader,
                       final org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeType shapeType, final InputStream is,
                       final ValidationPreferences rules) {
    this.header = shapeHeader;
    this.shapeType = shapeType;
  }

  // Getters

  public final org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeHeader getHeader() {
    return header;
  }

  public ShapeType getShapeType() {
    return shapeType;
  }

}
