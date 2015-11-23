package Loader;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Décrit une classe permettant d'obtenir des informations sur les PolygonShape contenu dans le .shp
 * @author Théophile
 */
public class PolygonInfo {
    private final List<Polygon> _polygons;

    /**
     * @param polygons Liste de Polygones dont on souhaite obtenir des informations
     */
    public PolygonInfo(List<Polygon> polygons) {
        this._polygons = polygons;
    }

    /**
     * Calcule et renvoie l'aire totale des polygones
     * @return Aire des polygones
     */
    public double getAire(){
        double somme = 0;

        for (Polygon polygon : _polygons) {
            somme += getAirePoly(polygon);
        }

        return somme;
    }

    private double getAirePoly(Polygon polygon) {
        double somme = 0;
        Point2D pt0;
        Point2D pt1 = null;
        Point2D debut = null;
        Iterator<Double> polygonIterator = polygon.getPoints().iterator();
        while (polygonIterator.hasNext()) {
            pt0 = pt1;
            pt1 = new Point2D(polygonIterator.next(), polygonIterator.next());
            if (pt0 != null) {
                somme += (pt1.getX() * pt0.getY() - pt0.getX() * pt1.getY());
            } else {
                debut = pt1;
            }
        }

        pt0 = pt1;
        pt1 = debut;
        if (pt1 != null) {
            somme += (pt1.getX() * pt0.getY() - pt0.getX() * pt1.getY());
        }
        return somme * 0.5;
    }
    
    /**
     * Calcule et retourne le centre de gravité/masse des polygones.
     * @return Centre de gravité des polygones
     */
    public PointData getCentreDeMasse(){
        // à optimiser
        List<Point2D> centres = _polygons.stream().map(this::getCentreDeMassePoly).collect(Collectors.toList());
        int i = 0;
        double div = getAirePoly(_polygons.get(i));
        Point2D centre = new Point2D(centres.get(i).getX() * getAirePoly(_polygons.get(i)),centres.get(i).getY() * getAirePoly(_polygons.get(i)));
        for (i = 1;i < _polygons.size();i++) {
            centre = new Point2D(centres.get(i).getX() * getAirePoly(_polygons.get(i)) + centre.getX(),centres.get(i).getY() * getAirePoly(_polygons.get(i)) + centre.getY());
            div += getAirePoly(_polygons.get(i));
        }
        return new PointData(centre.getX()/div,centre.getY()/div);
    }

    private Point2D getCentreDeMassePoly(Polygon polygon) {
        double somme_X = 0, somme_Y = 0, somme_aire = 0;
        Point2D pt0;
        Point2D pt1 = null;
        Point2D debut = null;
        Iterator<Double> polygonIterator = polygon.getPoints().iterator();
        while (polygonIterator.hasNext()) {
            pt0 = pt1;
            pt1 = new Point2D(polygonIterator.next(),polygonIterator.next());
            if(pt0 != null){
                somme_X += calculSommeX(pt0, pt1);
                somme_Y += calculSommeY(pt0, pt1);
                somme_aire += calculMembre2(pt0, pt1);
            } else {
                debut = pt1;
            }
        }
        pt0 = pt1;
        pt1 = debut;
        if (pt1 != null) {
            somme_X += calculSommeX(pt0, pt1);
            somme_Y += calculSommeY(pt0, pt1);
            somme_aire += calculMembre2(pt0, pt1);
        }

        double aire = 0.5 * somme_aire;
        double xG = somme_X / (6.0 * aire);
        double yG = somme_Y / (6.0 * aire);

        return new Point2D(xG,yG);
    }


    private double calculSommeX(Point2D pt0,Point2D pt1) {
        return (pt0.getX() + pt1.getX()) * calculMembre2(pt0, pt1);
    }

    private double calculSommeY(Point2D pt0,Point2D pt1) {
        return (pt0.getY() + pt1.getY()) * calculMembre2(pt0,pt1);
    }
    private  double calculMembre2(Point2D pt0, Point2D pt1) {
        return pt1.getX()*pt0.getY() - pt0.getX()*pt1.getY();
    }

}
