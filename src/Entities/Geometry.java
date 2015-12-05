/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Théophile
 */
public class Geometry {

    static public Polygon getMainPolygon(List<Polygon> polygons) {
        Polygon larger = null;
        int maxPoints = -1;
        for(Polygon p : polygons){
            int nbPoints = p.getPoints().size();
            if(nbPoints > maxPoints){
                maxPoints = nbPoints;
                larger = p;
            }
        }
        return larger;}

    static public Point getCentreDeMasse(Polygon polygon) {
        double somme_X = 0, somme_Y = 0, somme_aire = 0;
        Point pt0;
        Point pt1 = null;
        Point debut = null;
        Iterator<Double> polygonIterator = polygon.getPoints().iterator();
        while (polygonIterator.hasNext()) {
            pt0 = pt1;
            pt1 = new Point(polygonIterator.next(),polygonIterator.next());
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

        return new Point(xG,yG);
    }
    
    static private double calculSommeX(Point pt0,Point pt1) {
        return (pt0.x + pt1.x) * calculMembre2(pt0, pt1);
    }

    static private double calculSommeY(Point pt0,Point pt1) {
        return (pt0.y + pt1.y) * calculMembre2(pt0,pt1);
    }
    static private double calculMembre2(Point pt0, Point pt1) {
        return pt1.x * pt0.y - pt0.x * pt1.y;
    }

    static public double ratioCommonBoudaries(Polygon polygonA, Polygon polygonB) {
        List<Point> frontiereB = new ArrayList<>();
        int nbPointFrontiereCommune = 0;
        int nbPointBorderA = 0;
        
        Iterator<Double> iteratorB = polygonB.getPoints().iterator();
        while (iteratorB.hasNext()) {
            frontiereB.add(new Point(iteratorB.next(),iteratorB.next()));
        }
        
        Iterator<Double> iteratorA = polygonA.getPoints().iterator();
        while (iteratorA.hasNext()) {
            Point pt = new Point(iteratorA.next(),iteratorA.next());
            if(frontiereB.contains(pt)){
                nbPointFrontiereCommune++;
            }
            nbPointBorderA++;
        }
        
        return nbPointFrontiereCommune/(double)nbPointBorderA;
    }
    
}
