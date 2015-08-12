/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.gosoft.webcalendar;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import th.co.gosoft.webcalendar.utils.Config;
import th.co.gosoft.store.message.java.lib.database.DatabaseAdapter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.ejb.Local;
import javax.servlet.RequestDispatcher;
import org.apache.log4j.PropertyConfigurator;
import th.co.gosoft.webcalendar.utils.NullUtils;
import th.co.gosoft.webcalendar.utils.QueryDB;

/**
 *
 * @author Pongpanboo
 */
public class CalendarController extends HttpServlet {

    final private Logger log = Logger.getLogger(this.getClass().getName());
    final String poolName = Config.getDatabasePoolname();
    final int PageSize = Config.getPageSize();
    DatabaseAdapter adap = null;
    PreparedStatement pre = null;
    ResultSet rs = null;
    RequestDispatcher dp = null;

    String[] arr = {"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์"};
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> datas = new ArrayList<String>();

 
    int ty ;
    int tm ;
    int td ;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
       Calendar cal_now = Calendar.getInstance(Locale.US);
       ty = cal_now.get(cal_now.YEAR);
       tm = cal_now.get(cal_now.MONTH)+1;
       td = cal_now.get(cal_now.DAY_OF_MONTH);
        if (ty>2500){
            ty = ty-543;
        }
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    private String getSMS(int d){
        int idx=0;
        for (String s : names){
            if (s.equals(String.valueOf(d))){
                return "<a href='"+d+"' class='sms'>"+datas.get(idx)+"</a>";
            }
            idx +=1;
        }
        return "";
    }

    @Override
    public void init() throws ServletException {
        initial();
        super.init();
    }

    private void initial()
    {
            String filepath = getServletContext().getRealPath("/WEB-INF/log4j.properties");
            PropertyConfigurator.configure(filepath);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            out = response.getWriter();

            ArrayList<String> fixStores = new ArrayList<String>();
            String sql = "";
            sql = QueryDB.getStorePermission();
            adap = new DatabaseAdapter(poolName);
            pre = adap.getPreparedStatement(sql);
            rs = adap.executeQuery(pre);
            while(rs.next()){
               fixStores.add(rs.getString("STORE_ID"));
            }
              fixStores.add("0000");
            int d = NullUtils.cvInt(request.getParameter("d"));
            int m = NullUtils.cvInt(request.getParameter("m")) - 1;
            int y = NullUtils.cvInt(request.getParameter("y"));

            names = new ArrayList<String>();
            datas = new ArrayList<String>();

            String store_id = NullUtils.cvStr(request.getParameter("store_id"));
        //    store_id = NullUtils.cv5Digit(store_id);

//            log.info("Access by Store "+store_id);

            if (store_id.length()>0){
                if (fixStores.contains(store_id)){

                    try {
                        sql = QueryDB.getStoreEvent() ;
                        pre = adap.getPreparedStatement(sql);
                        pre.setString(1,store_id);
                        pre.setString(2,store_id);
                        pre.setString(3,String.valueOf(y));
                        pre.setString(4, String.valueOf(m+1));
                        rs = adap.executeQuery(pre);
                        while (rs.next()) {
                            if (rs.getInt("d")>0) {
                                names.add(NullUtils.cvStr(rs.getString("d")));
                                datas.add(NullUtils.cvStr(rs.getString("val")));
                            }
                        }
                    } catch (Throwable t) {
                        log.error(t.toString(),t);

                    } finally {
                        DatabaseAdapter.closeResultSet(rs);
                        DatabaseAdapter.closePreparedStatement(pre);
                        DatabaseAdapter.closeAll(adap);
                    }

                }

            }

            StringBuilder sb = new StringBuilder();
            sb.append("<table class='tb_calendar' cellpadding='0' cellspacing='0' border='0'>");
            sb.append("<tr>");
            sb.append("<th class='sun'>อาทิตย์</th>");
            sb.append("<th>จันทร์</th>");
            sb.append("<th>อังคาร</th>");
            sb.append("<th>พุธ</th>");
            sb.append("<th>พฤหัสบดี</th>");
            sb.append("<th>ศุกร์</th>");
            sb.append("<th>เสาร์</th>");
            sb.append("</tr>");

            //=======================================    This month
            Calendar cur_cal = new GregorianCalendar(y, m, d);
           // int cur_last_day = cur_cal.get(Calendar.DAY_OF_WEEK);
            int cur_last_day = cur_cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            //=======================================    Month prev
            int prev_y = 0;
            int prev_m = 0;
            if (m==0){
                prev_m=12;
                prev_y=y-1;
            }else{
                prev_m=m-1;
                prev_y=y;
            }
            Calendar prev_cal_month = new GregorianCalendar(prev_y, prev_m, 1);
            int pre_last_day = prev_cal_month.getActualMaximum(Calendar.DAY_OF_MONTH);

            //=======================================    Start First Week
            Calendar cal_start = new GregorianCalendar(y, m, 1);
            int s_w = cal_start.get(Calendar.DAY_OF_WEEK);
            sb.append("<tr>");
            int day  =1;
            boolean chkfirst = true;
            for (int i = 1; i < s_w; i++) {
                if (chkfirst){
                    sb.append("<td class='first'><div class='box'><span class='d d_pre'>" + (pre_last_day-(s_w-i)+1) +  "</span></div></td>");
                    chkfirst=false;
                }else{
                    sb.append("<td><div class='box'><span class='d d_pre'>" + (pre_last_day-(s_w-i)+1) +  "</span></div></td>");
                }
            }
            if (chkfirst){
                sb.append("<td class='first'><div class='box"+chkNowDate(y,m,day)+"'><span class='d'>"+day+"</span>"+getSMS(day)+"</div></td>");
                chkfirst=false;
            }else{
                sb.append("<td><div class='box"+chkNowDate(y,m,day)+"'><span class='d'>"+day+"</span>"+getSMS(day)+"</div></td>");
            }
            for (int i = s_w + 1; i < 8; i++) {
                day +=1;
                if (chkfirst){
                    sb.append("<td class='first'><div class='box"+chkNowDate(y,m,day)+"'><span class='d'>"+day+"</span>"+getSMS(day)+"</div></td>");
                    chkfirst=false;
                }else{
                    sb.append("<td><div class='box"+chkNowDate(y,m,day)+"'><span class='d'>"+day+"</span>"+getSMS(day)+"</div></td>");
                }
            }
            sb.append("</tr>");

            //=======================================    ALL
            int t_d=0;
            sb.append("<tr>");
            for (int i= day+1; i<cur_last_day+1;i++){
                t_d +=1;
                if (t_d>7){
                    sb.append("</tr>");
                    sb.append("<tr>");
                    t_d=1;
                }
                if (t_d==1){
                    sb.append("<td class='first'><div class='box"+chkNowDate(y,m,i)+"'><span class='d'>"+i+"</span>"+getSMS(i)+"</div></td>");
                }else{
                    sb.append("<td><div class='box"+chkNowDate(y,m,i)+"'><span class='d'>"+i+"</span>"+getSMS(i)+"</div></td>");
                }
            }

            int t_id=1;
            for (int i= t_d+1; i<8;i++){
                sb.append("<td><div class='box'><span class='d d_pre'>"+t_id+"</span></div></td>");
                t_id +=1;
            }
            sb.append("</tr>");
            sb.append("</table>");

            out.write(sb.toString());
        } catch (Throwable t){
            log.error(t.toString(), t);
        } finally {
            out.close();
        }
    }

    private String chkNowDate(int y, int m, int d){
        m +=1;
        if (ty==y && tm==m && td==d){
            return " today";
        }
        return "";
    }


}
