<%-- 
    Document   : permission_deny
    Created on : Jul 26, 2014, 11:06:55 PM
    Author     : kunawutjar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Permission Deny</title>
                  <style type="text/css">
             .page1
            {
                <%-- background-color:#E0E0E0; --%>
                text-align:center;
                text-align:center;
                font-size:24px;
                color:#FF0000;
            }
/*            .head{
                width:100%;
            }*/
         </style>
    </head>
      <body class="head" >
         <center>
             <div style="padding-top: 25%;">
             <table border="0" width="100%" align="left" style="text-align:left;">
                <tr>
                    <td>
<!--        <h3 style="color:red;">ร้าน : <%= (String)request.getAttribute("storeid") %> ไม่มีสิทธิเข้าใช้  Store Calendar</h3>-->
                        <h3 align="center" style="color:red;">เมนูนี้เป็นเมนูสำหรับโครงการทดลอง </h3>
                        <h3 align="center" style="color:red;"> ร้านของท่านไม่ได้อยู่ในโครงการทดลอง จึงไม่สามารถเข้าใช้งานได้</h3>
                     </td>
                </tr>
            </table>
          </div>
        </center>
        </body>
</html>
