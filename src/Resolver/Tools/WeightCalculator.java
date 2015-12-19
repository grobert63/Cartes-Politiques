/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resolver.Tools;

import Entities.Direction;
import Entities.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Théophile
 */
public class WeightCalculator {
    /*
    private final List<Region> regions;
    private final Map<Region, Map<Region, Double>> voisins = new HashMap<>();

    public WeightCalculator(List<Region> regions) {
        this.regions = regions;
    }
        
    private void calculerVoisins(){
        double ratio;
        double ratioTotal;
        
        int nbRegions = regions.size();
        Region a,b;
        Map<Region,Double> frontieres;
        for(int i=0;i<nbRegions;i++){
            a = regions.get(i);
            frontieres = new HashMap<>();
            voisins.put(a, frontieres);
            
            ratioTotal = 0;
            for(int j=0;j<nbRegions;j++){
                if(i!=j){
                    b =  regions.get(j);
                    ratio = a.pourcentageDeFrontiereCommune(b);
                    frontieres.put(b,ratio);
                    ratioTotal += ratio;
                }
            }
            frontieres.put(null,1-ratioTotal);
        }
    }
        
    public WeightArray getWeights() throws Exception{
        Region rA, rB;
        
        calculerVoisins();
        WeightArray weights = new WeightArray(regions.size());
        for(int col=0; col<=weights.size();col++){
            for(int row=0; row<=weights.size();row++){
                if(row != col){
                    if(row == weights.size()){
                        rA = null;
                    }
                    else{
                        rA = regions.get(row);
                    }
                    
                    if(col == weights.size()){
                        rB = null;
                    }
                    else{
                        rB = regions.get(col);
                    }
                    
                    for(int dir=0; dir<6;dir++){
                        weights.setWeight(row, col, dir, calculateWeight(rA,rB,dir));
                    }
                }
            }
        }
        return weights;
    }

    // rA et rB peuvent être 'null'
    private double calculateWeight(Region rA, Region rB, int direction) throws Exception{
        double distance = rA.getDistanceTo(rB);
        double angle = Direction.getDifferenceAngleDirection(rA.getAngleTo(rB),direction);
        double frontiereCommune = voisins.get(rA).get(rB);
        
        
        throw new UnsupportedOperationException("Pas fini !!!!!!!!!!!!!!!!");
    }
    */
}
