/**
 * 
 */
package at.helios.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * TODO BESCHREIBUNG_HIER_EINFUEGEN
 * @author
 * <PRE>
 *         ID    date       description
 *         markus.scherer 15.03.2010 Neuerstellung
 * </PRE>
 */
public class FuzzyQuickSortTest
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        List<Integer> coInts = new ArrayList<Integer>();
        FuzzyQuickSort<Integer> oFuzzyQuickSort = new FuzzyQuickSort<Integer>(coInts,40);
        coInts.add( 1   );
        coInts.add( 2   );
        coInts.add( 3   );
        coInts.add( 4   );
        coInts.add( 5   );
        coInts.add( 6   );
        coInts.add( 7   );
        coInts.add( 8   );
        coInts.add( 9   );
        coInts.add( 1   );
        coInts.add( 2   );
        coInts.add( 3   );
        coInts.add( 4   );
        coInts.add( 5   );
        coInts.add( 6   );
        coInts.add( 7   );
        coInts.add( 8   );
        coInts.add( 9   );
        coInts.add( 1   );
        coInts.add( 2   );
        coInts.add( 3   );
        coInts.add( 4   );
        coInts.add( 5   );
        coInts.add( 6   );
        coInts.add( 7   );
        coInts.add( 8   );
        coInts.add( 9   );
        
        Collections.shuffle(coInts);
        oFuzzyQuickSort.sort(5,coInts.size()-1);
        
        Integer[] aiInts = {2,3,4,5,6,7};
        
        Collections.max(Arrays.asList(aiInts));
        
        for (int i = 0; i < 10; i++)
        {
            for (Integer I : coInts)
            {
                if (I >= (10 - i))
                    System.out.print("# ");
                else
                    System.out.print("  ");
            }
            System.out.println();
        }

        for (Integer I : coInts)
            System.out.print(I + " ");
        System.out.println();
    }
}
