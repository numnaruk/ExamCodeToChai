/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.gosoft.webcalendar.member;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author pongpanboo
 */
public class Logout extends HttpServlet {
    final private Logger log = Logger.getLogger(this.getClass().getName());
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        
        try{
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession session;
            session = request.getSession(true);
            if (session != null) {

                log.info("User:"+request.getSession().getAttribute("Username")+", Action:Logout Complete");
              
                session.invalidate();
            }
            res.sendRedirect(req.getContextPath() + "/jsp/Login.jsp");
        }catch(Throwable t){
            log.error(t.toString(),t);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
        processRequest(request, response);
    }

}
