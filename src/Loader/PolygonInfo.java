package Loader;

import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

/**
 * Décrit une classe permettant d'obtenir des informations sur les PolygonShape contenu dans le .shp
 * @author Théophile
 */
public class PolygonInfo {
    private final PolygonShape polygon;

    /**
     * @param polygon Polygone dont on souhaite obtenir des informations
     */
    public PolygonInfo(PolygonShape polygon) {
        this.polygon = polygon;
    }

    /**
     * Calcule et renvoie l'aire du polygone
     * @return Aire du polygone
     */
    public double getAire(){
        double somme = 0;
        int nbParts = polygon.getNumberOfParts();

        PointData pt0;
        PointData pt1 = null;
        
        for (int partIndex = 0; partIndex < nbParts; partIndex++) {
            PointData[] points = polygon.getPointsOfPart(partIndex);
            for (PointData point : points) {
                pt0 = pt1;
                pt1 = point;
                if(pt0 != null){
                    somme += ( pt1.getX()*pt0.getY() - pt0.getX()*pt1.getY() );
                }
            }
        }
        
        pt0 = pt1;
        pt1 = polygon.getPointsOfPart(0)[0];
        somme += ( pt1.getX()*pt0.getY() - pt0.getX()*pt1.getY() );
        
        return somme * 0.5;
    }
    
    /**
     * Calcule et retourne le centre de gravité/masse du polygone.
     * @return Centre de gravité du polygone
     */
    public PointData getCentreDeMasse(){
        // à optimiser
        double membre1X, membre1Y, membre2;
        double somme_X = 0, somme_Y = 0, somme_aire = 0;
        int nbParts = polygon.getNumberOfParts();

        PointData pt0;
        PointData pt1 = null;
        
        for (int partIndex = 0; partIndex < nbParts; partIndex++) {
            PointData[] points = polygon.getPointsOfPart(partIndex);
            for (PointData point : points) {
                pt0 = pt1;
                pt1 = point;
                if(pt0 != null){
                    membre1X = pt0.getX() + pt1.getX();
                    membre1Y = pt0.getY() + pt1.getY();
                    membre2 =  pt1.getX()*pt0.getY() - pt0.getX()*pt1.getY();
                    
                    somme_X += membre1X * membre2;
                    somme_Y += membre1Y * membre2;
                    somme_aire += membre2;
                }
            }
        }
        
        pt0 = pt1;
        pt1 = polygon.getPointsOfPart(0)[0];
        membre1X = pt0.getX() + pt1.getX();
        membre1Y = pt0.getY() + pt1.getY();
        membre2 = pt1.getX()*pt0.getY() - pt0.getX()*pt1.getY();
        somme_X += membre1X * membre2;
        somme_Y += membre1Y * membre2;
        somme_aire += membre2;
        
        double aire = 0.5 * somme_aire;
        double xG = somme_X / (6.0 * aire);
        double yG = somme_Y / (6.0 * aire);
        
        return new PointData(xG,yG);
    }

}
