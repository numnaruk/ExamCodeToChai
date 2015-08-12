/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.gosoft.webcalendar.utils;

/**
 *
 * @author kunawutjar
 */
public class QueryDB {
   public static String sql ;
    public static String getUserLogin(){
        //  String sql = "SELECT U.*, P.Name, P.RULES FROM USER_EVENT U";
//        sql = "SELECT U.*, P.Name, P.RULES FROM WEBCALENDAR_USER U INNER JOIN"
//              + " USER_PERMISSION P ON U.GROUP_ID=P.GROUP_ID WHERE U.USER=?";
       sql = "SELECT * FROM WEBCALENDAR_USER  WHERE USER=?";
        return sql;
    }
    public static String getUpdateLastLogin(){
        //  String sql = "SELECT U.*, P.Name, P.RULES FROM USER_EVENT U";
        sql = "UPDATE WEBCALENDAR_USER SET LAST_LOGIN=NOW() WHERE  USER=?";
        return sql;
    }
    public static String getStorePermission(){
        sql = "SELECT STORE_ID FROM WEBCALENDAR_STOREPERMISSION ";
        return sql;
    }
    public static String getStoreEvent(){
        sql =  "SELECT COUNT(DISTINCT E.EVENT_ID) val ,YEAR(E.EDATETIME) y,MONTH(E.EDATETIME) m,DAY(E.EDATETIME) d "
                + " FROM `WEBCALENDAR_EVENT`E LEFT JOIN WEBCALENDAR_STORECALENDAREVENT S ON E.EVENT_ID=S.EVENT_ID "
                + " WHERE E.ENABLE='T' AND (?='0000' OR S.STORE_ID=?) AND (YEAR(E.EDATETIME)=? AND MONTH(E.EDATETIME)=?)  "
                + "GROUP BY YEAR(E.EDATETIME),MONTH(E.EDATETIME),DAY(E.EDATETIME) "
                + "ORDER BY y,m,d";
        return sql;
    }
    public static String getMaxID(){
       sql = "SELECT MAX(EVENT_ID) MaxID FROM WEBCALENDAR_EVENT  ";
       return sql;
    }
    public static String getEventlist(){
      sql =  "SELECT DISTINCT E.* FROM `WEBCALENDAR_EVENT`E LEFT JOIN WEBCALENDAR_STORECALENDAREVENT S "
            + "ON E.EVENT_ID=S.EVENT_ID WHERE  E.ENABLE='T' AND (?='0000' OR S.STORE_ID=?) "
            + "AND YEAR(E.EDATETIME)=? AND MONTH(E.EDATETIME)=?  AND DAY(E.EDATETIME)=? ";
       return sql;
    }
    public static String getStoreCalenEvent(){
      sql = "SELECT STORE_ID FROM WEBCALENDAR_STORECALENDAREVENT WHERE EVENT_ID = ?";
       return sql;
    }
    public static String setEvent(){
      sql =  "INSERT INTO WEBCALENDAR_EVENT  "
                    + " (EDATETIME, ETITLE, EDATA, ENABLE,CREATEBY,ETYPE,EFILENAME)"
                    + " VALUES (?,?,?,'T',?,?,?)";
       return sql;
    }
    public static String setStoreCalendar(){
       sql = "INSERT INTO WEBCALENDAR_STORECALENDAREVENT  "
            + " (STORE_ID, EVENT_ID, DESCRIPTION, VIEW_COUNT, LAST_VIEW, ENABLE)"
            + " VALUES (?,?,'',0,NOW(),'T')";
        return sql;
    }
    public static String delEvent(){
        //sql = "DELETE FROM WEBCALENDAR_EVENT WHERE EVENT_ID=?";
          sql = "UPDATE WEBCALENDAR_EVENT SET ENABLE='F' WHERE EVENT_ID=?";
        return sql;
    }
    public static String delStoreCal(){
        sql = "DELETE FROM WEBCALENDAR_STORECALENDAREVENT WHERE EVENT_ID=?";
        return sql;
    }

}
