/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.gosoft.webcalendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import th.co.gosoft.webcalendar.utils.Config;
import th.co.gosoft.store.message.java.lib.database.DatabaseAdapter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import th.co.gosoft.webcalendar.utils.Common;
import th.co.gosoft.webcalendar.utils.NullUtils;
import th.co.gosoft.webcalendar.utils.QueryDB;

/**
 *
 * @author pongpanboo
 */
public class CalendarDay extends HttpServlet {

    final private Logger log = Logger.getLogger(this.getClass().getName());
    final String poolName = Config.getDatabasePoolname();
    final int PageSize = Config.getPageSize();
    DatabaseAdapter adap = null;
    PreparedStatement pre = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    RequestDispatcher dp = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            String user_store = NullUtils.cvStr(request.getParameter("txtuser"));
            String chkSubmit = NullUtils.cvStr(request.getParameter("chkStore"));
            String useradmin = NullUtils.cvStr(request.getSession().getAttribute("Username"));
            out = response.getWriter();
            if (chkSubmit == null ? "CHK" == null : chkSubmit.equals("CHK")) {
                String missSID = "";
                missSID = chkStore(user_store);
                out.write(missSID);
            } else {
               if(useradmin == null ? "" != null : !useradmin.equals("")){
                 doSaveData(request,useradmin);
                 request.setAttribute("save_result", "ok");
                 out.write("1");
                }else{
                 out.write("0");
                }
//                dp = getServletContext().getRequestDispatcher("/MainCalendar");
//                dp.forward(request, response);
            }
        } catch (Throwable t) {
            DatabaseAdapter.closeResultSet(rs2);
            DatabaseAdapter.closeResultSet(rs);
            DatabaseAdapter.closePreparedStatement(pre);
            DatabaseAdapter.closeAll(adap);
            log.error(t.toString(), t);
        } finally {
            out.close();
        }
    }

    private String chkStore(String strStoreid) {
        int result = 0;
        String misSID = "";
        String strarr[] = strStoreid.split(",");
        try {
            for (int i = 0; i < strarr.length; i++) {
                String sql = "";
                try {
                    int chkint = Integer.parseInt(strarr[i]);
                } catch (Exception ex) {
                    misSID += "," + strarr[i];
                    continue;
                }
                String store_id = NullUtils.cv5Digit(strarr[i]);
                sql = QueryDB.getStorePermission() + "WHERE STORE_ID = '" + store_id + "'";
                adap = new DatabaseAdapter(poolName);
                pre = adap.getPreparedStatement(sql);
                rs = adap.executeQuery(pre);
                if (!rs.first()) {
                    misSID += "," + strarr[i];
                }
            }
        } catch (Throwable t) {
            misSID = ",Store Missing";
            log.error(t.toString(), t);
            adap.ROLLBACK();
            result = 0; // Error Save
        } finally {
            DatabaseAdapter.closeResultSet(rs);
            DatabaseAdapter.closePreparedStatement(pre);
            DatabaseAdapter.closeAll(adap);
        }
        if (misSID != "") {
            misSID = misSID.substring(1);
        }

        return misSID;
    }

    private void doSaveData(HttpServletRequest request,String username) {
        String cal = NullUtils.cvStr(request.getParameter("txtcal"));
        String title = NullUtils.cvStr(request.getParameter("txttitle"));
        String message = NullUtils.cvStr(request.getParameter("txtmessage"));
        String useradmin = username;
        int result = 0;
        try {
            adap = new DatabaseAdapter(poolName);
            String sql = "";
            sql = QueryDB.setEvent();
            pre = adap.getPreparedStatement(sql);
            pre.setString(1, Common.getDateCal2Base(cal));
            pre.setString(2, title);
            pre.setString(3, message);
            pre.setString(4,useradmin);
            pre.setString(5,"keyin");
            pre.setString(6,"");
            result = pre.executeUpdate();
            adap.COMMIT();

            if (result > 0) {
                int Event_id = 0;
                sql = "";
                sql = QueryDB.getMaxID();
                pre = adap.getPreparedStatement(sql);
                rs = adap.executeQuery(pre);
                while (rs.next()) {
                    Event_id = rs.getInt("MaxID");
                }

                String user_store = NullUtils.cvStr(request.getParameter("txtuser"));
                String nStorearr[] = user_store.split(",");
                ArrayList<String> diffStore = new ArrayList();
                for (int i = 0; i <= nStorearr.length - 1; i++) {
                    if (!diffStore.contains(NullUtils.cv5Digit(nStorearr[i]))) {
                        diffStore.add(NullUtils.cv5Digit(nStorearr[i]));
                    }
                }
                boolean res = false;
                sql = QueryDB.setStoreCalendar();
                pre = adap.getPreparedStatement(sql);
                for (int i = 0; i < diffStore.size(); i++) {
                    pre.setString(1, diffStore.get(i));
                    pre.setInt(2, Event_id);
//                    edit Last
               //    pre.addBatch();
                  result = pre.executeUpdate();
                 adap.COMMIT();
                }
 //               pre.executeBatch();
             //   adap.COMMIT();
                res = true;
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            DatabaseAdapter.closeResultSet(rs);
            DatabaseAdapter.closePreparedStatement(pre);
            DatabaseAdapter.closeAll(adap);
        }

    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();
            adap = new DatabaseAdapter(poolName);
            String act = NullUtils.cvStr(request.getParameter("act"));
            if (act.equals("del")) {
                String EID = NullUtils.cvStr(request.getParameter("EID"));
                if (EID.length() > 0) {
                    int _id = Integer.parseInt(EID);
                    int result = 0;
                    String sqlEvent = QueryDB.delEvent();
              //Dot      String sqlStoreEvent = QueryDB.delStoreCal();
                    try {

                        pre = adap.getPreparedStatement(sqlEvent);
                        pre.setInt(1, _id);
                        int res = pre.executeUpdate();
                        adap.COMMIT();

// Dot                       pre = adap.getPreparedStatement(sqlStoreEvent);
//                        pre.setInt(1, _id);
//                        res = pre.executeUpdate();
//                        adap.COMMIT();
//
                        result = 1;

                    } catch (Throwable t) {
                        log.error(t.toString(), t);
                        adap.ROLLBACK();
                    } finally {
                        DatabaseAdapter.closePreparedStatement(pre);
                        DatabaseAdapter.closeAll(adap);
                    }

                    if (result == 1) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                }

            } else {

                int d = NullUtils.cvInt(request.getParameter("d"));
                int m = NullUtils.cvInt(request.getParameter("m"));
                int y = NullUtils.cvInt(request.getParameter("y"));

                String store_id = NullUtils.cvStr(request.getParameter("store_id"));
                if (store_id.length() > 0) {
                    String arrEvent = "";
                    StringBuilder sb = new StringBuilder();
                    sb.append("<div style=\"position:relative; border:1px solid #EEE; \">");
                    sb.append("<div id=\"n_Date\" style=\"position:absolute;z-index:0;left:0px;top:0px;width:50px;height:50px;background-color:#ECADA4;color:#fff;font-size:20px;font-weight:bold;text-align:center;line-height:50px;\">" + d + "</div>");
                    sb.append(" <div style=\"position:absolute;right:0px;top:-15px;\"><a onClick=\"loadini()\" href=\"#\" style=\"text-decoration:underline;font-size:15px;\">ปิด[X]</a></div>");

                    sb.append("<div style=\"margin-left:65px;min-height:50px;_height:50px;\">");
                    sb.append("<table class=\"list\" cellpadding='0' cellspacing='0' border='0' width='100%'>");
                    String sql = QueryDB.getEventlist();
                    pre = adap.getPreparedStatement(sql);
                    pre.setString(1, store_id);
                    pre.setString(2, store_id);
                    pre.setString(3, String.valueOf(y));
                    pre.setString(4, String.valueOf(m));
                    pre.setString(5, String.valueOf(d));
                    rs = adap.executeQuery(pre);
                    if (rs.isBeforeFirst()) {
                        while (rs.next()) {
                            sb.append("<tr>");

                            if (arrEvent.length() > 0) {
                                arrEvent += ",";
                            }
                            arrEvent += NullUtils.cvStr(rs.getString("EVENT_ID")) + "";

                            if (store_id.equals("0000")) {
                                sb.append("<td style='width:20px;' valign=\"top\"; ><a href='" + NullUtils.cvStr(rs.getString("EVENT_ID")) + "' class='delete_list' title='ลบ' ><img src='assets/images/icon_delete.gif' /></a></td>");
                            }

                            sb.append("<td style='border-bottom:1px solid #ccc; '>");
                            sb.append("<b>" + rs.getString("ETITLE").replaceAll("\r\n|\n","<br/>") + "</b>");
                            sb.append("<br />");
                          //  sb.append("<textarea rows=\"3\" class=\"txt area\" style=\"width:300px; height:130px;\">" + rs.getString("EDATA")+ "</textarea>");
                         //*************Detail*********************************************
                          //  String x = rs.getString("EDATA").replaceAll("\r\n|\n", "<br />");
                          //   sb.append(x);
                          //   sb.append("<br />");
                          //************End Detail*****************************************
                            if (store_id.equals("0000")) {
                                String allStoreid = "";
                                sql = QueryDB.getStoreCalenEvent();
                                pre = adap.getPreparedStatement(sql);
                                pre.setString(1, NullUtils.cvStr(rs.getString("EVENT_ID")));
                                rs2 = adap.executeQuery(pre);         
                                while (rs2.next()) {
                                    allStoreid +=  " , " + rs2.getString("STORE_ID") ;
                                }
                                if (allStoreid == null ? "" != null : !allStoreid.equals("")) {
                                    allStoreid = allStoreid.substring(2);
                                }
                                sb.append("ร้านสาขา:" + allStoreid);
                                sb.append("</td>");
                                sb.append("</tr>");
                            }
                        }
                        sb.append("</table>");
                        sb.append("</div>");
                        sb.append("<div class='clear'></div>");
                        sb.append("</div>");
                        out.write(sb.toString());
                    } else {
                        out.write("Empty");
                    }

                    if (store_id == null ? "0000" != null : !store_id.equals("0000")) {
                        int result = 0;
                        try {
                            sql = "UPDATE WEBCALENDAR_STORECALENDAREVENT  "
                                    + " SET VIEW_COUNT=VIEW_COUNT+1 , "
                                    + " LAST_VIEW= NOW() "
                                    + " WHERE STORE_ID=? AND EVENT_ID IN (" + arrEvent + ")";


                            pre = adap.getPreparedStatement(sql);
                            pre.setString(1, store_id);
                            result = pre.executeUpdate();
                            adap.COMMIT();

                        } catch (Throwable t) {
                            log.error(t.toString(), t);
                            adap.ROLLBACK();
                        } finally {
                            sb.setLength(0);
                            DatabaseAdapter.closeResultSet(rs);
                            DatabaseAdapter.closePreparedStatement(pre);
                            DatabaseAdapter.closeAll(adap);
                        }
                    }
                }
            }
            //act=del&EID
        } catch (Throwable t) {
            log.error(t.toString(), t);
        } finally {
            out.close();
            DatabaseAdapter.closeResultSet(rs2);
            DatabaseAdapter.closeResultSet(rs);
            DatabaseAdapter.closePreparedStatement(pre);
            DatabaseAdapter.closeAll(adap);

        }
    }
}
