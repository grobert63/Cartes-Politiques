package Debug;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Théophile
 */
public class TimeDebug {
    private static Map<Integer,Long> starts = new HashMap<>();
    private static Map<Integer,Long> times = new HashMap<>();
    private static Map<Integer,String> labels = new HashMap<>();
    
    public static void timeStart(int index){
        starts.put(index, System.nanoTime());
    }
    
    public static void timeStop(int index){
        long timeToAdd = (System.nanoTime() - starts.get(index));
        long previousTime = 0;
        
        if(times.containsKey(index)){
            previousTime = times.get(index);
        }
        times.put(index, previousTime+timeToAdd);
    }
    
    public static void setTimeLabel(int index, String label){
        labels.put(index, label);
    }
    
    public static void displayPourcentage(int index, int refIndex){
        displayHeader(index);
        System.out.println("\t> "+((int)((times.get(index)/(double)times.get(refIndex))*10000))/100.0 + " %");
    }
    
    public static void displayTime(int index){
        displayHeader(index);
        System.out.println("\t> "+(times.get(index)/ 1000000000.0)+" sec");
    }
    
    private static void displayHeader(int index){
        System.out.print("[TimeIndex : " + index + "] ");
        if(labels.containsKey(index)){
            System.out.print(labels.get(index));
        }
        System.out.println();
    }
}
