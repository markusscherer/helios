///**
// * 
// */
//package at.helios.sheduling;
//
//import java.util.Stack;
//
//
///**
// * Stack für die Einteilungs-Vorgänge
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   29.03.2009 Erstkommentierung
// * </PRE>
// **/
//public class SchedulerStack
//{
//	Stack<SchedulerStackItem> _coItems = new Stack<SchedulerStackItem>();
//	int _iBlock = 0;
//	
//	/**
//	 * Legt Item auf Stack und führt es aus
//	 * @param oItem    
//	 **/
//	public void push(SchedulerStackItem oItem)
//	{
//	    oItem.apply();
//		_coItems.push(oItem);
//	}
//	
//	/**
//	 * Nimmt Item vom Stack und macht es ungültig
//	 **/
//	public void pop()
//	{
//		_coItems.peek().undo();
//		_coItems.pop();
//	}
//
//}
