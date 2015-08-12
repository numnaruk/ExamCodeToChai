/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.gosoft.webcalendar.utils;

/**
 *
 * @author Pongpanboo
 */
public class Config {

    private static int PageSize = 20;

    /* Server Production */
    private static String poolName = "jdbc/storemessage";
    /* end server production */

    private static String Directory = "assets/tempExcel/" ;
    public static String getDatabasePoolname() {
        return poolName;
    }

    public static int getPageSize() {
        return PageSize;
    }

    public static String getUpload_Directory(){
        return Directory;
    }

}
