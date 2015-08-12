/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.gosoft.webcalendar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.mail.search.DateTerm;

/**
 *
 * @author pongpanboo
 */
public class Common {

    public static String DisplayEnable(String value) {
        if (value.equals("T")) {
            return "ใช้งาน";
        }
        return "ไม่ใช้งาน";
    }

    public static String PageLimit(int PageNo, int PageSize) {
        int intStart = 0;
        if (PageNo != 0) {
            intStart = (PageNo - 1) * PageSize;
        }
        return " LIMIT " + String.valueOf(intStart) + ", " + String.valueOf(PageSize);
    }

    public static String DisplayPaging(int Total, int PageSize, int CurrentPage) {
        return DisplayPaging(Total, PageSize, CurrentPage, "");
    }

    public static String DisplayPaging(int Total, int PageSize, int CurrentPage, String param) {
        int StartPage, EndPage;

        //int TotalPage = (int) (Math.ceil(Total / PageSize)) ;
        int TotalPage = Total / PageSize;
        if ((Total % PageSize) > 0) {
            TotalPage = TotalPage + 1;
        }

        StringBuilder sb = new StringBuilder();
        if (CurrentPage > TotalPage) {
            CurrentPage = TotalPage;
        }
        //StartPage = 1;
        //EndPage = TotalPage;
        if (CurrentPage - 10 > 1) {
            StartPage = CurrentPage - 10;
        } else {
            StartPage = 1;
        }

        if (StartPage + 10 > TotalPage) {
            EndPage = TotalPage;
        } else {
            EndPage = StartPage + 10;
        }

        if (CurrentPage > 1) {
            sb.append("<a href='?Page=1" + param + "' class='fix'>First</a>");
            sb.append("<a href='?Page=" + String.valueOf(CurrentPage - 1) + param + "'  class='fix'>Prev</a>");
        } else {
            sb.append("<a href='#' class='fix active'>First</a>");
            sb.append("<a href='#'  class='fix active'>Prev</a>");
        }

        for (int i = StartPage; i < EndPage + 1; i++) {
            if (i == CurrentPage) {
                sb.append("<a href='#'  class='active'>" + String.valueOf(i) + "</a>");
            } else {
                sb.append("<a href='?Page=" + String.valueOf(i) + param + "'>" + String.valueOf(i) + "</a>");
            }
        }


        if (CurrentPage < TotalPage) {
            sb.append("<a href='?Page=" + String.valueOf(CurrentPage + 1) + param + "' class='fix'>Next</a>");
            sb.append("<a href='?Page=" + TotalPage + param + "' class='fix'>Last</a>");
        } else {
            sb.append("<a href='#'  class='fix active'>Next</a>");
            sb.append("<a href='#' class='fix active'>Last</a>");
        }

        if (TotalPage < 2) {
            return "";
        } else {
            return "<div class='paging'>" + sb.toString() + "</div>";
        }

    }

    public static String getDateCal2Where(String value) {
        if (value.length() == 10) {
            return value.substring(6, 10) + "-" + value.substring(3, 5) + "-" + value.substring(0, 2);
        }
        return "";
    }

    public static String getDateCal2Base(String value) {
         String dateString="";
        try{
            String [] year = value.split("/");
            if(Integer.parseInt(year[2]) > 2500){
                int newyear =  Integer.parseInt(year[2])- 543;
                value = year[0]+"/"+year[1]+"/"+newyear ;
            }
            value += " 00:00" ;
            SimpleDateFormat sim1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            SimpleDateFormat sim2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateString= sim2.format(sim1.parse(value));
        }catch(ParseException p){
            return "";
        }
         return dateString;
    }

    public static String getDatenameFormat(){
        String name = "";
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
	String []date = sdf.format(new Date()).split("/");
        String []time = stf.format(cal.getTime()).split(":");
        name = date[2]+date[1]+date[0]+time[0]+time[1]+time[2];
        return name;
    }

    public static String getDateBase2Cal(String value) {
         String dateString="";
        try{
            SimpleDateFormat sim2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateString= sim2.format(sim1.parse(value));
        }catch(ParseException p){
            return "";
        }
         return dateString;
    }

    public static boolean isValidDate(String value){
         SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
         df.setLenient(false);
        try {
        Date date = df.parse(value);
        } catch (ParseException e) {
           // System.out.println("รูปแบบ Date ไม่ถูกต้อง " + value);
                return false;
        }
        return true;
    }
    public static String setDateBase2Cal(String value) {
        if (value.length() > 16) {
            return value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4) + " " + value.substring(11, 16);
        }
        return "";
    }

    public static String setDateBase2Cal_ver2(String value) {
        if (value.length() > 16) {
            return value.substring(8, 10) + "." + value.substring(5, 7) + "." + value.substring(0, 4) + ", " + value.substring(11, 16);
        }
        return "";
    }

    public static boolean isValidNumeric(String value){
        try{
            int chk = Integer.parseInt(value);
        }catch(Exception e){
//            System.out.println("ข้อมูลนี้ไม่ไช่ Interger " + value);
            return false;
        }
        return true;
    }
}
