//package at.helios.sheduling;
//
//import java.text.SimpleDateFormat;
//
///**
// * Item für den SchedulerStack
// * @author
// * <PRE>
// *         ID    date       description
// *         mas   30.03.2009 Neuerstellung
// * </PRE>
// **/
//public class SchedulerStackItem
//{
//	private int _iTeamNumber;
//	private int _iDriverPointer;
//	private int _iMediPointer;
//	private ShiftNode _oShiftNode;
//	
//	/**
//	 * Konstruktor mit Parametern
//	 * @param iTeamNumber
//	 * @param iDriverPointer
//	 * @param iMediPointer
//	 * @param oShiftNode
//	 **/
//	public SchedulerStackItem(int iTeamNumber, int iDriverPointer, int iMediPointer,
//			ShiftNode oShiftNode) {
//		_iTeamNumber = iTeamNumber;
//		_iDriverPointer = iDriverPointer;
//		_iMediPointer = iMediPointer;
//		_oShiftNode = oShiftNode;
//	}
//	
//	/**
//	 * Wendet Item an
//	 **/
//	public void apply()
//	{
//		_oShiftNode.setSelectedTeam(_iTeamNumber, _iDriverPointer, _iMediPointer);	
//	}
//	
//	/**
//	 * Macht Item rückgängig
//	 **/
//	public void undo()
//	{
//		_oShiftNode.resetSelectedTeam(_iTeamNumber);
//	}
//	
//	/* (non-Javadoc)
//	 * @see java.lang.Object#toString()
//	 */
//	public String toString()
//	{
//		SimpleDateFormat oFormat = new SimpleDateFormat("dd.MM.yyyy");
//		return "[SchedulerStackItem] T: " + _iTeamNumber + " D: " + _iDriverPointer + " M: " + _iMediPointer + " " + oFormat.format(_oShiftNode.getDate().getTime()) + " " + _oShiftNode.getShift().name();
//	}
//
//}
