/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resolver.Tools;

/**
 *
 * @author Théophile
 */
public class WeightArray {
    private final double[][][] array;
    private final int nbRegions;

    public WeightArray(int nbRegions) {
        this.nbRegions = nbRegions;
        array = new double[nbRegions+1][nbRegions+1][6];
    }
    
    public int size(){
        return nbRegions;
    }
    
    public void setWeight(int indexA, int indexB, int direction, double weight){
        array[indexA][indexB][direction] = weight;
    }
    
    public double getWeight(int indexA, int indexB, int direction){
        return array[indexA][indexB][direction];
    }
    
    public int getMinWeightIndex(int indexA, int direction){
        int indexB = 1;
        double minWeight = -1;
        for(int idx=0; idx <= nbRegions; idx++){
            double curWeight = getWeight(indexA, idx, direction);
            if(minWeight == -1 || curWeight < minWeight){
                minWeight = curWeight;
                indexB = idx;
            }
        }
        return indexB;
    }
}
