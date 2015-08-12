package th.co.gosoft.webcalendar.member;


import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
import th.co.gosoft.webcalendar.utils.NullUtils;
import java.sql.PreparedStatement;
import th.co.gosoft.store.message.java.lib.database.DatabaseAdapter;
import java.sql.ResultSet;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import th.co.gosoft.webcalendar.authen.ldap.LDAPConnector;
import th.co.gosoft.webcalendar.authen.ldap.LDAPDataObject;
import th.co.gosoft.webcalendar.utils.Config;
import th.co.gosoft.webcalendar.utils.QueryDB;

/**
 *
 * @author pongpanboo
 */
public class Login extends HttpServlet {
    final private Logger log = Logger.getLogger(this.getClass().getName());

    final String poolName = Config.getDatabasePoolname();
    DatabaseAdapter adap = null;
    PreparedStatement pre = null;
    ResultSet rs = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String user = NullUtils.cvStr(request.getParameter("u"));
            String pass = NullUtils.cvStr(request.getParameter("p"));
            if (user == null || pass == null) {
                out.write("ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง");
            }
            
            String sql = QueryDB.getUserLogin();
            adap = new DatabaseAdapter(poolName);
            pre = adap.getPreparedStatement(sql);
            pre.setString(1, user);
            rs = adap.executeQuery(pre);

            int group_id = 1;
            String user_login = "";
            String rules ="";
            String last_login ="";
            while (rs.next()) {

                user_login = NullUtils.cvStr(rs.getString("USER"));
                last_login = NullUtils.cvStr(rs.getString("LAST_LOGIN"));
            }

              if(user_login == null ? "" != null : !user_login.equals("")){
               LDAPDataObject lData = null;
               LDAPConnector ldapConnect = new LDAPConnector();
                lData = ldapConnect.LdapConnection(user, pass);
                if (lData != null) {
                    sql = QueryDB.getUpdateLastLogin();
                    DatabaseAdapter.closePreparedStatement(pre);
                    pre = adap.getPreparedStatement(sql);
                    pre.setString(1, user);
                    pre.executeUpdate();
                    adap.COMMIT();

                    log.info("User:"+user+", Action:Login Complete");

                    HttpSession session;
                    session = request.getSession(true);
                    session.setMaxInactiveInterval(5*30);
                    session.setAttribute("Username", user);
                    session.setAttribute("Name", lData.getName());
                    session.setAttribute("nameth",lData.getNameThai());
                    session.setAttribute("last_login", last_login);
                    out.write(String.valueOf(group_id));
             
                } else {
                    out.write("ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง");
                }
            } else {
                out.write("ชื่อผู้ใช้งานนี้ไม่มีในระบบ กรุณาติดต่อผู้ดูแลระบบ");
            }

        } catch (Throwable t) {
           log.error(t.toString(), t);
            out.write("ไม่สามารถเชื่อมต่อระบบได้ กรุณาติดต่อผู้ดูแลระบบ");
        } finally {
            DatabaseAdapter.closeResultSet(rs);
            DatabaseAdapter.closePreparedStatement(pre);
            DatabaseAdapter.closeAll(adap);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
        processRequest(request, response);
    }
}
