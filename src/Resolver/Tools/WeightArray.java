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
    private double[][][] array;
    private int nbRegions;

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
}
