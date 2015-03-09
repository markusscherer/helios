/**
 * 
 */
package at.helios.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Durchmischt die Elemente einer Liste und führt einen Quicksort mit angegebener Tiefe aus
 * @author
 * <PRE>
 *         ID    date       description
 *         mas   15.03.2010 Neuerstellung
 * </PRE>
 */
public class FuzzyQuickSort<E extends Comparable<E>>
{
    private List<E> _coList = new ArrayList<E>();
    private int _iDepth = 0;
    private int _iMaxDepth;

    public FuzzyQuickSort(List<E> coList, int iMaxDepth)
    {
        _coList = coList;
        _iMaxDepth = iMaxDepth;
    }

    private int partition(int iStart, int iEnd, int iPivotIndex)
    {
        E pivotValue = _coList.get(iPivotIndex);
        Collections.swap(_coList, iPivotIndex, iEnd); //send pivot item to the back
        int index = iStart; //keep track of where the front ends
        for (int i = iStart; i < iEnd; i++) //check from the front to the back
        {
            //swap if the current value is less than the pivot
            if ((_coList.get(i)).compareTo(pivotValue) <= 0)
            {
                Collections.swap(_coList, i, index);
                index++;
            }
        }
        Collections.swap(_coList, iEnd, index); //put pivot item in the middle
        return index;
    }
    
    private void quicksort(int iStart, int iEnd)
    {
       _iDepth++;
       if (iEnd > iStart && _iDepth < _iMaxDepth)
       {
          int partitionPivotIndex = iStart;
          int newPivotIndex = partition(iStart, iEnd, partitionPivotIndex);
          quicksort(iStart, newPivotIndex-1);
          quicksort(newPivotIndex+1, iEnd);
       }
       _iDepth--;
    }
    
    public void sort()
    {
        Collections.shuffle(_coList);
        quicksort(0, _coList.size()-1);
    }
    
    public void sort(int iStart, int iEnd)
    {
        Collections.shuffle(_coList.subList(iStart, iEnd));
        quicksort(iStart, iEnd);
    }
}
