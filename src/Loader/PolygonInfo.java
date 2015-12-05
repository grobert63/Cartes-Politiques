package Loader;

import Entities.Point;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Décrit une classe permettant d'obtenir des informations sur les PolygonShape contenu dans le .shp
 * @author Théophile
 */
public class PolygonInfo {
    //private final List<Polygon> _polygons;

    /**
     * @param polygons Liste de Polygones dont on souhaite obtenir des informations
     */
    /*
    public PolygonInfo(List<Polygon> polygons) {
        this._polygons = polygons;
    }
    */

    /**
     * Calcule et renvoie l'aire totale des polygones
     * @return Aire des polygones
     */
    /*
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
    */
    /**
     * Calcule et retourne le centre de gravité/masse des polygones.
     * @return Centre de gravité des polygones
     */
    /*
    public Point getCentreDeMasse(){
        // à optimiser
        List<Point2D> centres = _polygons.stream().map(this::getCentreDeMassePoly).collect(Collectors.toList());
        int i = 0;
        double div = getAirePoly(_polygons.get(i));
        Point2D centre = new Point2D(centres.get(i).getX() * getAirePoly(_polygons.get(i)),centres.get(i).getY() * getAirePoly(_polygons.get(i)));
        for (i = 1;i < _polygons.size();i++) {
            centre = new Point2D(centres.get(i).getX() * getAirePoly(_polygons.get(i)) + centre.getX(),centres.get(i).getY() * getAirePoly(_polygons.get(i)) + centre.getY());
            div += getAirePoly(_polygons.get(i));
        }
        return new Point(centre.getX()/div,centre.getY()/div);
    }
    */

    /**
     * Calcule et retourne le centre de masse principal d'une région en excluant les polygones trop éloignés
     * @param acceptedPercent Facteur d'éloignement accepté en pourcentage total de la carte
     * @param mapHeight Hauteur de la carte
     * @param mapWidth Largeur de la carte
     * @return Centre de gravité principal des polygones
     */
    /*
    public Point2D getCentreDeMassePrincipal(double acceptedPercent, double mapHeight, double mapWidth) {
        List<Map<Integer, Point2D>>  listCentres = new ArrayList<>();
        listCentres.add(new HashMap<>());
        Polygon previous = null;
        int i = 0, j = 0;
        for (Polygon polygon : _polygons) {
            if (previous != null) {
                if (isSameArea(previous, polygon, acceptedPercent, mapHeight, mapWidth)) {
                    listCentres.get(j).put(i,getCentreDeMassePoly(polygon));
                }
                else {
                    listCentres.add(new HashMap<>());
                    listCentres.get(j+1).put(i, getCentreDeMassePoly(polygon));
                    j++;
                }
            }
            else {
                listCentres.get(j).put(i,getCentreDeMassePoly(polygon));
            }
            i++;
            previous = polygon;
        }
        double aire = 0,aireTemp = 0;
        Map<Integer,Point2D> centres = new HashMap<>();
        for(Map<Integer,Point2D> liste : listCentres) {
            for (Integer key : liste.keySet()) {
                aireTemp += getAirePoly(_polygons.get(key));
            }
            if (aireTemp > aire)
            {
                aire = aireTemp;
                centres = liste;
            }
            aireTemp = 0;
        }
        double div = 0;
        Point2D centre = null;
        for (int key : centres.keySet()) {
            if (centre == null) {
                centre = new Point2D(centres.get(key).getX() * getAirePoly(_polygons.get(key)),centres.get(key).getY() * getAirePoly(_polygons.get(key)));
            }
            else {
                centre = new Point2D(centres.get(key).getX() * getAirePoly(_polygons.get(key)) + centre.getX(),centres.get(key).getY() * getAirePoly(_polygons.get(key)) + centre.getY());
            }
            div += getAirePoly(_polygons.get(key));
        }
        if (centre != null) {
            return new Point2D(centre.getX()/div,centre.getY()/div);
        }
        return new Point2D(0,0);
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

    private static boolean isSameArea(Polygon polygonA, Polygon polygonB, double acceptedPercent, double mapHeight, double mapWidth) {
        double temp = 0;
        for (int i = 0; i < polygonA.getPoints().size(); i = i+2) {
            for (int j = 0; j < polygonB.getPoints().size(); j = j+2) {
                if (temp == 0 || temp > distanceBetweenPoints(new Point2D(polygonA.getPoints().get(i),polygonA.getPoints().get(i+1)), new Point2D(polygonB.getPoints().get(j),polygonB.getPoints().get(j+1)))) {
                    temp = distanceBetweenPoints(new Point2D(polygonA.getPoints().get(i),polygonA.getPoints().get(i+1)), new Point2D(polygonB.getPoints().get(j),polygonB.getPoints().get(j+1)));
                }
            }
        }
        return temp < (mapHeight * acceptedPercent / 100) && temp < (mapWidth * acceptedPercent / 100);
    }

    private static double distanceBetweenPoints(Point2D pointA, Point2D pointB) {
        return Math.sqrt((Math.pow(pointB.getX() - pointA.getX(),2) + Math.pow(pointB.getY() - pointA.getY(),2)));
    }
    
    public Polygon getMainPolygon(){
        Polygon larger = null;
        int maxPoints = -1;
        for(Polygon p : _polygons){
            int nbPoints = p.getPoints().size();
            if(nbPoints > maxPoints){
                maxPoints = nbPoints;
                larger = p;
            }
        }
        return larger;
    }
        
    public static double ratioDeFrontierePartagee(Polygon borderA, Polygon borderB){
        List<Point> frontiereB = new ArrayList<>();
        int nbPointFrontiereCommune = 0;
        int nbPointBorderA = 0;
        
        Iterator<Double> iteratorB = borderB.getPoints().iterator();
        while (iteratorB.hasNext()) {
            frontiereB.add(new Point(iteratorB.next(),iteratorB.next()));
        }
        
        Iterator<Double> iteratorA = borderA.getPoints().iterator();
        while (iteratorA.hasNext()) {
            Point pt = new Point(iteratorA.next(),iteratorA.next());
            if(frontiereB.contains(pt)){
                nbPointFrontiereCommune++;
            }
            nbPointBorderA++;
        }
        
        return nbPointFrontiereCommune/(double)nbPointBorderA;
    }
    */
}
