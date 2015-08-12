<%-- 
    Document   : calendar
    Created on : Jan 17, 2014, 3:54:04 PM
    Author     : Pongpanboo
--%>
<%@page import="th.co.gosoft.webcalendar.utils.NullUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<html>
    <head >
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="x-ua-compatible" content="IE=8" >
        <title>Calendar | Demo</title>
	<script type="text/javascript"> var mn=3;
            var username = "<%= (String)request.getAttribute("storeid") %>";
            if(username != '0000'){
                var url = "<%=request.getContextPath()%>/assets/css/style.css",
                head = document.getElementsByTagName('head')[0];
                link = document.createElement('link');
                link.type = "text/css";
                link.rel = "stylesheet"
                link.href = url;
                head.appendChild(link);
            }else{
                var url = "<%=request.getContextPath()%>/assets/css/style_admin.css",
                head = document.getElementsByTagName('head')[0];
                link = document.createElement('link');

                link.type = "text/css";
                link.rel = "stylesheet"
                link.href = url;
                head.appendChild(link);
            }
        </script>

<!--        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/style.css" />-->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/superfish.css" media="screen">
<!--	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/jquery.fancybox.css?v=2.1.3" media="screen" />-->
    </head>
    <body style="height:100%;">
        <div style="position:relative;">
        <div id="send" style="position:absolute;">
            <h2 align="right" class="useradmin" style="display:none; padding-top:10px;"><span id="th_uname"></span>&nbsp;|&nbsp;<a id="bn_logout" style="text-decoration: underline;" href="#">Logout</a></h2>
            <h2 id="web_name" style="display:none;text-align: center;padding-top:20px;">Store Calendar</h2>
            <div id="calendar">
             <div class="cal_option">
                <div style="float:left;">
                    <a href="#" id="bt-now" class="btn" style="line-height:20px;">เดือนปัจจุบัน</a>
                    <a href="#" id="bt-prev"  class="btn"><</a>
                    <a href="#" id="bt-next" class="btn">></a>
                </div>
                <a href="#" class="display_admin" id="btImp" style="float:right;width:80px;height:30px;line-height:30px;display:none;margin-left:10px;background-color:#5B9846;color:#fff ;text-align: center;border-radius: 7px;">Import File</a>
                <a href="#" class="display_admin" id="btadd" style="float:right;width:80px;height:30px;line-height:30px;display:none;margin-left:10px;background-color:#D64A38;color:#fff ;text-align: center;border-radius: 7px;">เพิ่มกิจกรรม</a>
                <span style="float:right;">&nbsp;<span id="dp_month"></span> <span id="dp_year"></span>&nbsp;</span>
                &nbsp; &nbsp; &nbsp; &nbsp;
                <div class="clear"></div>
                <div style="display:none;">
                    day <input type="text" id="txtday" style="width:20px" value="11" />
                    month <input type="text" id="txtmonth" style="width:20px" value="1" />
                    year <input type="text" id="txtyear" style="width:40px" value="2013" />
                </div>
            </div>
            <div id="dp_list" style="display:none;padding:15px; background-color: #eee;line-height:22px;margin:10px 0px;">
            </div>
            <div id="htmlContainer"></div>
          </div>
        </div>
    <div style="-webkit-opacity: 0.5;-moz-opacity:0.5;opacity:0.5;filter:alpha(opacity=50);" class="popup_form" id="addJob"> </div>
    <div id="panel_form" style="position: absolute; top: 70px; left: 250px;_left:200px;">

    <div  class="msgform" id="addJobform">
      <div style=" font-size:18px;font-weight:bold;text-align:right; padding-right:5px;"> <a id="bn_exit" href="#" style="text-decoration:underline">Close</a> </div>
      <form id="FormSave" method="post" action="" accept-charset="UTF-8" >
        <div style="height:450px">
        <div style="color:#D64A38;font-size:20px;padding:10px 0px;">เพิ่มกิจกรรม</div>
        <table>
            <tr>
                <td>วันที่</td>
                <td><input type="text" id="txtcal" name="txtcal" readonly="true"  class="txt" style="width:150px;" value="" /></td>
            </tr>
            <tr>
                <td valign="top">ชื่อกิจกรรม</td>
<!--                <td><textarea  id="txttitle" name="txttitle" rows="3" class="txtarea" style="width:300px; height:130px;"></textarea></td>-->
                    <td><input type="text" id="txttitle" name="txttitle" autocomplete="off"  class="txt" style="width:300px;height:150px" value="" /></td>
            </tr>
<!--Dot            <tr>
                <td valign="top">รายละเอียด</td>
                <td><textarea  id="txtmessage" name="txtmessage" rows="3" class="txt area" style="width:300px; height:130px;"></textarea></td>
            </tr>-->
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
        </div>
     </div>
  </div>
<!--    </div>-->
	<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery1.7.1.js"></script>
<!--        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/style.css" />-->
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/jquery-ui.css" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/css/jquery-ui-timepicker-addon.css" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery-ui-timepicker-addon.js"></script>

        <script type="text/javascript">
            var _res = '<%=NullUtils.cvStr(request.getAttribute("save_result"))%>';
            var username = "<%= (String)request.getAttribute("storeid") %>";
            if(username != '0000'){
                var url = "<%=request.getContextPath()%>/assets/css/style.css",
                head = document.getElementsByTagName('head')[0];
                link = document.createElement('link');
                link.type = "text/css";
                link.rel = "stylesheet"
                link.href = url;
                head.appendChild(link);
            }else{
                var url = "<%=request.getContextPath()%>/assets/css/style_admin.css",
                head = document.getElementsByTagName('head')[0];
                link = document.createElement('link');
                link.type = "text/css";
                link.rel = "stylesheet"
                link.href = url;
                head.appendChild(link);
            }
            if(_res.length>0){
              //  alert(_res);
             //   parent.$.fancybox.close();
            }

            $('#bn_exit').click(function(){
                loadini();
            });
            $('#bn_exit_Imp').click(function(){
                loadini();
            });
            $('#txtcal').datepicker({
                changeMonth: true,
                changeYear: true,
                buttonText: ' ',
                showButtonPanel: true,
                showOn: 'both',
                isBuddhist: true,
                dateFormat: "dd/mm/yy",
                buttonImage: '<%=request.getContextPath()%>/assets/images/calendar.gif', buttonImageOnly: true,
                minDate: 0
            });


            $("#btnsave").click(function(){
                var _msg = "";
                $(".req_err").removeClass("req_err");500
                $("#msg").html("");

                if ($("#txtcal").val()==''){
                    _msg +="- กรุณากรอกวันที่<br />";
                    $("#txtcal").addClass("req_err");
                }

                if ($("#txttitle").val()==''){
                    _msg +="- กรุณากรอกข้อความ<br />";
                    $("#txttitle").addClass("req_err");
                }


//Dot                if ($("#txtmessage").val()==''){
//                    _msg +="- กรุณากรอกรายละเอียด<br />";
//                    $("#txtmessage").addClass("req_err");
//                }

                if ($("#txtuser").val()==''){
                    _msg +="- กรุณากรอกข้อความ<br />";
                    $("#txtuser").addClass("req_err");
                }

                if (_msg.length>0){
                   // $("#msg").html("<b>กรุณากรอกข้อมูล</b><br />" + _msg);
                    return false;                
                }else{
  //    ###################################
               var _txtcal = $("#txtcal").val();
               var _txttitle = $("#txttitle").val();
//Dot          var _txtMsg = $("#txtmessage").val();
               var _txtMsg ="";
               var _txtStore = $("#txtuser").val();
               var _url = "<%=request.getContextPath()%>/CalendarDay";
                           var result = $.ajax({
                                type:"POST",
                                data:"chkStore=CHK&txtuser=" + encodeURIComponent( _txtStore),
                                url: _url,
                                async: false
                            }).responseText;
                           if(result == "" ){
                                var result2 = $.ajax({
                                    type:"POST",
                                    data:"txtcal="+ encodeURIComponent( _txtcal ) +"&txttitle=" + encodeURIComponent( _txttitle )  + "&txtmessage=" + encodeURIComponent( _txtMsg )  + "&txtuser=" + encodeURIComponent( _txtStore),
                                    url: _url,
                                    async: false
                                }).responseText;
                            //  return true;
        //                    if (parseInt(result)>0){
        //                      alert("Succed");
        //                     }
                                if(parseInt(result2)>0){
                                    $("#txtcal").val("");
                                    $("#txttitle").val("");
                                    $("#txtmessage").val("");
                                    $("#txtuser").val("");
                                    loadini();
                                    processCal();
                                }else{
                                 alert("อยู่ในระบบนานเกิน 30 นาทีไม่อนุญาตให้เพิ่มข้อมูล กรุณา Login ใหม่อีกครั้ง");
                                  var _url = "<%=request.getContextPath()%>/Logout.do";
                                  window.location = _url;
                                }
                            }else{
                                   alert("ข้อมูลร้านไม่ถูกต้อง : " + result);
                            }
//                      }else{
//                       alert("อยู่ในระบบนานเกิน 30 นาทีไม่อนุญาตให้เพิ่มข้อมูล กรุณา Login ใหม่อีกครั้ง");
//                               var _url = "<%=request.getContextPath()%>/Logout.do";
//                               window.location = _url;
//                         }
                }
 //    ###################################
                return false;
            });
   //******************** Import From Excel *******************************
             $("#btnImport").click(function(){
              var _url = "<%=request.getContextPath()%>/CalendarDay";
              var _filepath = $("#fileImport").val();
              alert(_filepath);
               if ($("#fileImport").val()==''){
                   $("#fileImport").addClass("req_err");
                   return false;
                }
              var _url = "<%=request.getContextPath()%>/ImportEventByExcel";
              var result = $.ajax({
                    type:"POST",
                    data:"filepath=" + encodeURIComponent(_filepath),
                    url: _url,
                    async: false
                 }).responseText;
             });

 //  ********************** End Import From Excel  *************************

        </script>


        <script type="text/javascript">
            var part = "<%=request.getContextPath()%>/CalendarController";
            var part_sms = "<%=request.getContextPath()%>/CalendarDay";
            var part_sms_del = "<%=request.getContextPath()%>/CalendarDay";

            var part_sms_edit = "<%=request.getContextPath()%>/ProcessSMS";
            var today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth()+1;
            var yyyy = today.getFullYear();
            var store_id = "<%= (String)request.getAttribute("storeid") %>";
            var myMonth = new Array("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม");



        function getURLParameter(name) {
            return decodeURI(
                (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]
            );
        }
         if (store_id =='0000'){
             $("#web_name").show();
             $(".useradmin").show();
             $(".display_admin").show();
             $("#th_uname").html("<%= (String)request.getAttribute("nameth") %>");
         }
        $("#addJob").click(function(){
            $("#addJob").fadeOut('2000');
            $("#addJobform").fadeOut('2000');
            $("#addJobImport").fadeOut('2000');
        });
        
	$("#btadd").click(function(){
               / //processCal();
                loadini();
                 $("#send").css('z-index','-1');
                 $("#addJob").fadeIn('2000');
                 $("#addJobform").fadeIn('2000');

            });
        $("#btImp").click(function(){
               / //processCal();
//                loadini();
//                 $("#send").css('z-index','-1');
//                 $("#addJob").fadeIn('2000');
//                 $("#addJobImport").fadeIn('2000');
               var _url = "<%=request.getContextPath()%>/jsp/importEvent.jsp";
               document.location = _url;
            });
            function loadini(){
                 $("#dp_list").slideUp();
                 $("#addJobform").fadeOut('2000');
               //  $("#addJobImport").fadeOut('2000');
                 $("#addJob").fadeOut('2000');
            }

            function processCal(){
                var dtemp = new Date();
                var _url = part+"?d="+dd+"&m="+mm+"&y="+yyyy+"&dd="+dtemp.getTime()+"&store_id="+store_id;
                var result = $.ajax({
                    url:_url,
                    async: false
                }).responseText;
                $("#dp_month").text(myMonth[mm-1]);
                $("#dp_year").text(yyyy);

                $("#htmlContainer").html(result);

                $(".sms").click(function(){
                    var dsms = $(this).attr("href");
                    var reclick = $(this).attr("href");
                    var dateUrl=dsms.split("/");
                    if(dateUrl.length>1)
                     {
                       dsms=dateUrl[dateUrl.length-1];
                     }
                    var dd = new Date();
                    var _url_sms = part_sms+"?d="+dsms+"&m="+mm+"&y="+yyyy+"&dd="+dd.getTime()+"&store_id="+store_id;
                    var res_sms = $.ajax({
                        url: _url_sms,
                        async: false
                    }).responseText;
                    if(res_sms == "Empty"){
                        alert("ข้อมูลถูกแก้ไขโดย Admin");
                         loadini();
                         processCal();
                    }else{
                    $("#dp_list").html(res_sms)
                    $("#dp_list").slideDown();
                    }
                    $(".edit_list").click(function() {
                        var _url_edit = ($(this).attr("href"));
                        var _url_edit = part_sms_edit+$(this).attr("href");
                        window.location.href = _url_edit;
                        return false;
                    });
                    $(".delete_list").click(function() {
                        var msg_del = 'ต้องการลบกิจกรรมนี้ใช่หรือไม่?';
                        if ( confirm(msg_del) ) {
                           var dsms_del = $(this).attr("href");
                            var dateUrl=dsms_del.split("/");
                            if(dateUrl.length>1)
                             {
                               dsms_del=dateUrl[dateUrl.length-1];
                             }
                            var _url_del = part_sms_del+"?act=del&EID="+ dsms_del;
                            var res_del = $.ajax({
                                url:_url_del,
                                async: false
                            }).responseText;
                            if(res_del=='1'){
                                processCal();
                                $("#dp_list").html("");
                                $("#dp_list").hide();
                               $("a[href='" + reclick + "']").click();
                             }else{
                                alert("ไม่สามารถลบข้อมูลได้");
                            }
                        }
                        return false;
                    });
                    return false;
                });

                return false;
            }
            processCal();

            $("#bt-now").click(function(){
                today = new Date();
                dd = today.getDate();
                mm = today.getMonth()+1;
                yyyy = today.getFullYear();
                loadini();
                processCal();
                return false;
            });
            $("#bt-prev").click(function(){
                if (mm==1){
                    mm =12;
                    yyyy -=1;
                }else{
                    mm -=1;
                }
                loadini();
                processCal();
                return false;
            });
            $("#bt-next").click(function(){
                if (mm==12){
                    mm =1;
                    yyyy +=1;
                }else{
                    mm +=1;
                }
                loadini();
                processCal();
                return false;
            });

            $("#Start").click(function(){
                var d = new Date();
                var _d = $("#txtday").val();
                var _m = $("#txtmonth").val();
                var _y = $("#txtyear").val();
                var _url = "<%=request.getContextPath()%>/Calendar?d="+_d+"&m="+_m+"&y="+_y+"&dd="+d.getTime()+"&store_id="+store_id;
                var result = $.ajax({
                    url:_url,
                    async: false
                }).responseText;
               $("#htmlContainer").html(result);
                return false;
            });

            $("#bn_logout").click(function(){
                var msg_logout = 'ต้องการออกจากระบบใช่หรือไม่?';
                 if ( confirm(msg_logout) ){
                  var _url = "<%=request.getContextPath()%>/Logout.do";
                     window.location = _url;
                 }else{
                    return false;
                 }
            });

</script>
    </body>
</html>

