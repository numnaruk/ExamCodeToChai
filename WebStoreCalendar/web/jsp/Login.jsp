<%-- 
    Document   : Login
    Created on : Jul 23, 2014, 10:05:59 AM
    Author     : kunawutjar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>เข้าสู่ระบบ</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/style.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery1.7.1.js"></script>
    </head>
    <body>
  <div id="wapper">
            <div id="header"></div>
            <div id="menu"></div>
            <div id="container">
                <div id="login">
                    <label class="lbluser" for="txtuser">ชื่อผู้ใช้งาน<span class="req">*</span></label>
                    <input type="text" id="txtuser" name="txtuser" class="txt txtuser" onkeypress="return runEnter(event)"  />
                    <label class="lblpass" for="txtpass">รหัสผ่าน<span class="req">*</span></label>
                    <input type="password" id="txtpass" name="txtpass" class="txt txtpass" onkeypress="return runEnter(event)"  />
                    <button type="button" id="btnlogin"  name="btnlogin" class="btn"> เข้าสู่ระบบ</button>
                    <div id="msg" class="req_msg"></div>
                </div>
            </div>
            <div id="footer">Copyright 2010 Gosoft Thailand Co.,Ltd. All rights reserved.</div>
        </div>
        <script type="text/javascript">

            $("#txtuser").focus();
            function runEnter(e) {
                if (e.keyCode == 13) {
                    $("#btnlogin").click();
                    return false;
                }
            }

            $("#btnlogin").click(function(){
                var _msg = "";
                 $(".req_err").removeClass("req_err");
                 $("#msg").html("");

                if ($("#txtuser").val()==''){
                    _msg +="- กรุณากรอกชื่อผู้ใช้งาน<br />";
                    $("#txtuser").addClass("req_err");
                }
                if ($("#txtpass").val()==''){
                    _msg +="- กรุณากรอกรหัสผ่าน<br />";
                    $("#txtpass").addClass("req_err");
                }

                if ($("#txtuser").val().length>0 && $("#txtpass").val().length>0){
                    var d = new Date();

                    var _url = "<%=request.getContextPath()%>/CheckLogin?u=" + $("#txtuser").val() + "&p=" + $("#txtpass").val() + "&t="+d.getTime();
                    var result = $.ajax({
                        url:_url,
                        async: false
                    }).responseText;
        
                    if (parseInt(result)>0){
                     window.location = "<%=request.getContextPath() %>/MainCalendar.do";
                    }else{
                        _msg +="- "+result+"<br />";
                        $("#txtuser").addClass("req_err");
                        $("#txtpass").addClass("req_err");
                    }

                }

                if (_msg.length>0){
                    $("#msg").html(_msg);
                    return false;
                }
            });
        </script>
    </body>
</html>
