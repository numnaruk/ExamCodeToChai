/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.gosoft.webcalendar;


import java.io.IOException;
import java.io.PrintWriter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import th.co.gosoft.store.message.java.lib.database.DatabaseAdapter;
import th.co.gosoft.webcalendar.utils.Config;
import th.co.gosoft.webcalendar.utils.NullUtils;
import th.co.gosoft.webcalendar.utils.QueryDB;

/**
 *
 * @author kunawutjar
 */
public class DirectLimeSurvay extends HttpServlet {
   
    final private Logger log = Logger.getLogger(this.getClass().getName());
    final String poolName = Config.getDatabasePoolname();
    final int PageSize = Config.getPageSize();
    DatabaseAdapter adap = null;
    PreparedStatement pre = null;
    ResultSet rs = null;
    RequestDispatcher dp = null;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String storeid = NullUtils.cvStr(request.getParameter("storeid"));
        RequestDispatcher dp = null;
        try {
           if(storeid == null ? "" != null : !storeid.equals("")){
              if(ChkStore(storeid)){
              HttpSession session = request.getSession(false);
                  if(session!=null) {
                    session.invalidate();
                   }
              storeid = NullUtils.cv5Digit(storeid);
              response.sendRedirect("http://survey.cpall.co.th/LimeSurvey2/index.php/438249/lang-th");
               }else{
              request.setAttribute("storeid",storeid );
              String []root =   request.getRequestURL().toString().split("/D");
              response.sendRedirect(root[0] + "/jsp/permission_deny.jsp");
             }
            }
             dp.forward(request, response);
        }catch(Throwable t){
            log.error(t.toString(), t);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean ChkStore(String storeid) {
        int result = 0;
        try{
            String sql = "";
            storeid = NullUtils.cv5Digit(storeid);
            sql = QueryDB.getStorePermission() + "WHERE STORE_ID = ?";
            adap = new DatabaseAdapter(poolName);
            pre = adap.getPreparedStatement(sql);
            pre.setString(1, storeid);
            rs = adap.executeQuery(pre);
            while(rs.next()){
             result += 1;
            }
        }catch(Throwable t){
            log.error(t.toString(), t);
            adap.ROLLBACK();
            result = 0; // Error Save
        }finally{
            DatabaseAdapter.closeResultSet(rs);
            DatabaseAdapter.closePreparedStatement(pre);
            DatabaseAdapter.closeAll(adap);
        }

        if(storeid == null ? "" != null : !storeid.equals("")){
           if(result > 0){
               return true;
            }else{
               return false;
            }
        }
        return false;
    }
}
