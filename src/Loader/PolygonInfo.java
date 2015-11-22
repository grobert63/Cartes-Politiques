package Loader;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;

import java.util.Iterator;

/**
 * Décrit une classe permettant d'obtenir des informations sur les PolygonShape contenu dans le .shp
 * @author Théophile
 */
public class PolygonInfo {
    private final Polygon polygon;

    /**
     * @param polygon Polygone dont on souhaite obtenir des informations
     */
    public PolygonInfo(Polygon polygon) {
        this.polygon = polygon;
    }

    /**
     * Calcule et renvoie l'aire du polygone
     * @return Aire du polygone
     */
    public double getAire(){
        double somme = 0;

        Point2D pt0;
        Point2D pt1 = null;
        Point2D debut = null;
        Iterator<Double> polygonIterator = polygon.getPoints().iterator();
        while (polygonIterator.hasNext()) {
            pt0 = pt1;
            pt1 = new Point2D(polygonIterator.next(),polygonIterator.next());
            if(pt0 != null){
                somme += ( pt1.getX()*pt0.getY() - pt0.getX()*pt1.getY() );
            }
            else
            {
                debut = pt1;
            }
        }
        
        pt0 = pt1;
        pt1 = debut;
        if (pt1 != null) {
            somme += ( pt1.getX()*pt0.getY() - pt0.getX()*pt1.getY() );
        }

        return somme * 0.5;
    }
    
    /**
     * Calcule et retourne le centre de gravité/masse du polygone.
     * @return Centre de gravité du polygone
     */
    public PointData getCentreDeMasse(){
        // à optimiser
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
        
        return new PointData(xG,yG);
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
