/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.gosoft.webcalendar.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import org.apache.log4j.Logger;
import th.co.gosoft.store.message.java.lib.database.DatabaseAdapter;

/**
 *
 * @author kunawutjar
 */
public class DatabaseUtils {
    final private Logger log = Logger.getLogger(this.getClass().getName());
    final String poolName = Config.getDatabasePoolname();
    final int PageSize = Config.getPageSize();
    DatabaseAdapter adap = null;
    PreparedStatement pre = null;
    ResultSet rs = null;

  
   public void DBclose(){
      DatabaseAdapter.closeResultSet(rs);
      DatabaseAdapter.closePreparedStatement(pre);
      DatabaseAdapter.closeAll(adap);
   }
}
