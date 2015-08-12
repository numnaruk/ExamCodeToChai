<%-- 
    Document   : add
    Created on : Jan 20, 2014, 6:25:50 PM
    Author     : pongpanboo
--%>

<%@page import="th.co.gosoft.webcalendar.utils.NullUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 1.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Calendar</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/style.css" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/jquery-ui.css" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/jquery-ui-timepicker-addon.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery1.7.1.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery-ui-timepicker-addon.js"></script>
    </head>
    <body>


        <form id="FormSave" method="post" action="<%=request.getContextPath()%>/CalendarDay" accept-charset="UTF-8" >

        <div style="height:450px">

        <div style="color:#D64A38;font-size:20px;padding:10px 0px;">เพิ่มกิจกรรม</div>
        <table>
            <tr>
                <td>วันที่</td>
                <td><input type="text" id="txtcal" name="txtcal" readonly="true"  class="txt" style="width:150px;" value="" /></td>
            </tr>
            <tr>
                <td>ชื่อกิจกรรม</td>
                <td><input type="text" id="txttitle" name="txttitle"  class="txt" style="width:300px;" value="" /></td>
            </tr>
            <tr>
                <td valign="top">รายละเอียด</td>
                <td><textarea  id="txtmessage" name="txtmessage" rows="3" class="txt area" style="width:300px; height:130px;"></textarea></td>
            </tr>
            <tr>
                <td valign="top">รหัสร้านที่มีสิทธิเข้าใช้งาน<br />คั่นด้วย ","</td>
                <td><textarea  id="txtuser" name="txtuser" rows="3" class="txt area" style="width:300px; height:130px;"></textarea></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" id="btnsave" value="บันทึก" class="btn" /></td>
            </tr>
        </table>
        </div>

        </form>

        <script type="text/javascript">

            var _res = '<%=NullUtils.cvStr(request.getAttribute("save_result"))%>';
            if(_res.length>0){
                //alert(_res);
                parent.$.fancybox.close();
            }


            $('#txtcal').datetimepicker({
                changeMonth: true,
                changeYear: true,
                buttonText: ' ',
                showButtonPanel: true,
                showOn: 'both',
                isBuddhist: true,
                dateFormat: "dd/mm/yy",
                buttonImage: '../assets/images/calendar.gif', buttonImageOnly: true,
                minDate: 0
            });


            $("#btnsave").click(function(){
                var _msg = "";
                $(".req_err").removeClass("req_err");
                $("#msg").html("");

                if ($("#txtcal").val()==''){
                    _msg +="- กรุณากรอกวันที่<br />";
                    $("#txtcal").addClass("req_err");
                }

                if ($("#txttitle").val()==''){
                    _msg +="- กรุณากรอกข้อความ<br />";
                    $("#txttitle").addClass("req_err");
                }


                if ($("#txtmessage").val()==''){
                    _msg +="- กรุณากรอกรายละเอียด<br />";
                    $("#txtmessage").addClass("req_err");
                }

                if ($("#txtuser").val()==''){
                    _msg +="- กรุณากรอกข้อความ<br />";
                    $("#txtuser").addClass("req_err");
                }
                
                if (_msg.length>0){
                    //$("#msg").html("<b>กรุณากรอกข้อมูล</b><br />" + _msg);
                    return false;
                }
                return true;
            });

        </script>
    </body>
</html>
