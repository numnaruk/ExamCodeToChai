<%--
    Document   : Login
    Created on : Jul 23, 2014, 10:05:59 AM
    Author     : kunawutjar
--%>

<%@page import="th.co.gosoft.webcalendar.utils.NullUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Import File Data</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/style.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery1.7.1.js"></script>
    </head>
    <body>
        <div id="wapper">
        <div id="header"></div>
        <div id="container">
        <div  id="addJobImport" style="color:#D64A38;font-size:20px;">นำเข้าข้อมูลจาก Excel</div>             
        <div style="position:relative;left:250px; width:450px;height:250px;border-bottom:#ccc 1px solid;">
            <div style="padding-bottom:10px;padding-top: 10px;">
               <label>  หมายเหตุ : การนำเข้าข้อมูล รองรับเฉพาะไฟล์ Excel ประเภท .xls เท่านั้น   </label><br/><br/>
                <form id="FormSave" method="post" action= "<%=request.getContextPath()%>/ImportEventByExcel"  enctype="multipart/form-data" accept-charset="UTF-8" >
                    <input type="file" id="fileImport" name="fileImport" accept="image/jpg" class="txt" style="width: 325px"  />
                    <input type="submit" id="btnImport" value="Import" class="btn" />
                </form>
            </div>
                <div id ="loading" style="font-size:20px;"></div>
                <div id ="result" style="display:block;" >
                        <label style="font-size:15px;"><br/><span id="event"></span><br/></label>

<!--                    <label style="font-size:20px;"><br/>จำนวนแผนงานทั้งหมด <span id="event"></span> รายการ<br/></label>-->
<!--                    <label style="font-size:15px;" ><br/>Import สำเร็จ     <span id="compevent" style="color:#ff0000;"></span> รายการ<br/></label>-->
<!--                    <label style="font-size:15px;" > <br/>Import ไม่สำเร็จ  <span id="misscomp" style="color:#ff0000;"></span> รายการ </label>-->
                </div>
          </div>
               <div style="margin-top:20px;margin-left:400px;">
                <input type="button" id="btnback" value="กลับหน้าแผนงาน" class="btn"/>
               </div>
        </div>
       <div id="footer">Copyright 2010 Gosoft Thailand Co.,Ltd. All rights reserved.</div>
    </div>   

        <script type="text/javascript">
//            var rowdata = '<%=NullUtils.cvStr(request.getAttribute("countdata"))%>';
            var missdata = '<%=NullUtils.cvStr(request.getAttribute("missdata"))%>';
            var missfile = '<%=NullUtils.cvStr(request.getAttribute("missfile"))%>';
            var retvalue = '<%=NullUtils.cvStr(request.getAttribute("retvalue"))%>';
            var loginper = '<%=NullUtils.cvStr(request.getAttribute("timeper"))%>';
//            if(rowdata != ""){
//                var sum = parseInt(rowdata) + parseInt(missdata) ;
//                $("#event").text(sum);
//                $("#compevent").text(rowdata);
//                $("#misscomp").text(missdata);
//                $("#result").slideDown();
//            }
            if(loginper == "F"){
              alert("อยู่ในระบบนานเกิน 30 นาทีไม่อนุญาตให้เพิ่มข้อมูล กรุณา Login ใหม่อีกครั้ง");
              var _url = "<%=request.getContextPath()%>/Logout.do";
              window.location = _url;
            }
            if(missfile == "T"){
                $("#loading").html("กรุณาเลือกไฟล์ให้ถูกต้อง");
            }
            if(retvalue == "MIH"){
                $("#loading").html(missdata);
            }else if(retvalue == "MIC"){
                $("#event").text(missdata);
            }else if(retvalue == "REIn"){
                $("#loading").html(missdata);
            }
            $("#btnImport").click(function(){
                loadini();
                $("#loading").html("กำลัง Import ข้อมูล .......");
            });
            $("#btnback").click(function(){
                window.location = "<%=request.getContextPath() %>/MainCalendar.do";
            });
            function loadini(){
                $("#result").slideUp();
            }

        </script>
    </body>
</html>
