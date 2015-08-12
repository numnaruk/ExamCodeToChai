/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.gosoft.webcalendar.utils;
import java.util.ArrayList;
/**
 *
 * @author Pongpanboo
 */
public class NullUtils {

    public static String cvStr(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    public static String cvStr(Object value, String default_val) {
        if (value == null) {
            return default_val;
        }
        return value.toString();
    }

    public static int cvInt(Object value) {
        try {
            return Integer.valueOf((String) value);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static int cvInt(Object value, int default_val) {
        try {
            return Integer.valueOf((String) value);
        } catch (Exception ex) {
            return default_val;
        }
    }

    public static float cvFloat(Object value) {
        try {
            if (value == null) {
                return 0;
            }
            return Float.parseFloat((String) value);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static float cvFloat(Object value, float default_val) {
        try {
            if (value == null) {
                return default_val;
            }
            return Float.parseFloat((String) value);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static String cv5Digit(String storeId){
       if(storeId.length() < 5){
         storeId = "0" + storeId;
         storeId = cv5Digit(storeId);
       }
      return storeId;
    }

    public static String[] cvStoreID(String storeId){
        String nStorearr [] = storeId.split(",");
           ArrayList<String> diffStore = new ArrayList();
            for(int i=0;i <= nStorearr.length - 1;i++){
           if(!diffStore.contains(NullUtils.cv5Digit(nStorearr[i]))){
               diffStore.add(NullUtils.cv5Digit(nStorearr[i]));
          }
        }
       return (String[]) diffStore.toArray() ;
    }

}
