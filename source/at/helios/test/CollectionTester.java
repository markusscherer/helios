package at.helios.test;

import java.util.ArrayList;
import java.util.List;

public class CollectionTester
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        List<String> coStrings = new ArrayList<String>();
        
        coStrings.add("Eins");
        coStrings.add("Zwei");
        coStrings.add("Vier");
        
        for(String oString : coStrings) System.out.println(oString);
        System.out.println("_._._._._");
        
        String oTest = coStrings.get(2);
        
        oTest = "Drei";
        for(String oString : coStrings) System.out.println(oString);
        System.out.println("_._._._._");
        
        coStrings.set(2, "Drei");
        for(String oString : coStrings) System.out.println(oString);
    }

}
