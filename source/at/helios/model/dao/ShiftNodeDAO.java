//package at.helios.model.dao;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//import at.helios.common.DBHelper;
//import at.helios.sheduling.ShiftNode;
//
///**
// * DAO für ShiftNodes
// * @author
// * <PRE>
// *         ID    date       description
// *         mas 02.02.2009 Neuerstellung
// * </PRE>
// **/
//public class ShiftNodeDAO
//{
//    private static final String SQL_INSERT_SHIFTNODE = "INSERT INTO tShiftNode(nDriverId, nMediId, nAdditionalId, nShiftTreeId, dShift, cShifttype, nTeamNumber) VALUES (?,?,?,?,?,?,?);";
//
//    /**
//     * Fügt ShiftNode in Datenbank ein
//     * @param oShiftNode      einzufügender ShiftNode
//     * @param iShiftTreeId    Id des zugehörigen ShiftTrees
//     **/
//    public void insert(ShiftNode oShiftNode, int iShiftTreeId)
//    {
//        Connection oConnection = DBHelper.getConnection();
//
//        try
//        {
//            for (int i = 0; i < oShiftNode.getTeamCount(); i++)
//            {
//                if (oShiftNode.getSelectedDriverIndex(i) != -1)
//                {
//                    if (oShiftNode.getSelectedDriverNode(i).getSelectedMediIndex() != -1)
//                    {
//                        PreparedStatement oStatement = oConnection.prepareStatement(SQL_INSERT_SHIFTNODE);
//                        oStatement.setInt(1, oShiftNode.getSelectedDriverNode(i).getDriver().getPersonId());
//                        oStatement.setInt(2, oShiftNode.getSelectedDriverNode(i).getSelectedMediNode()
//                            .getMedi().getPersonId());
//                        if (oShiftNode.getAdditional() == null)
//                        {
//                            oStatement.setNull(3, java.sql.Types.INTEGER);
//                        }
//                        else
//                        {
//                            oStatement.setInt(3, oShiftNode.getAdditional().getPersonId());
//                        }
//                        oStatement.setInt(4, iShiftTreeId);
//                        oStatement.setDate(5, new java.sql.Date((oShiftNode.getDate().getTime().getTime())));
//                        oStatement.setString(6, oShiftNode.getShift().toString());
//                        oStatement.setInt(7, i);
//
//                        oStatement.execute();
//                        oStatement.close();
//                    }
//                }
//            }
//            
//            oConnection.close();
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//
//    }
//}
