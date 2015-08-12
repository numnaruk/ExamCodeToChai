/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
            var part = "<%=request.getContextPath()%>/CalendarController";
            var part_sms = "<%=request.getContextPath()%>/CalendarDay";
            var part_sms_del = "<%=request.getContextPath()%>/CalendarDay";

            var part_sms_edit = "<%=request.getContextPath()%>/ProcessSMS";

            var today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth()+1;
            var yyyy = today.getFullYear();

            var myMonth = new Array("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม");



        function getURLParameter(name) {
            return decodeURI(
                (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]
            );
        }
         var store_id = getURLParameter("storeid");
         if (store_id!='0000'){
            $(".display_admin").hide();
         }
        $("#span_store").html(store_id);
	$("#btadd").click(function(){
//        $.fancybox.open({
////                    href : '<%=request.getContextPath()%>/jsp/add.jsp',
//////                    href : 'https://www.google.com',
////                    type : 'iframe',
////                    width : '500px',
////                    padding : 5,
////                    'afterClose'      :   function(){
////                        processCal();
////                      loadini();
////                    }
//            });
     //            $("#addJob").show();
	});
//            $.fancybox.open({
//                    href : '../popup/calendar_edit.jsp'+_url_edit,
//                    type : 'iframe',
//                    width : '400px',
//                    padding : 5,
//                    'afterClose'      :   function(){
//                        processCal();
//                        loadini();
//                    }
//            });

            function loadini(){
                $("#dp_list").slideUp();
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
                    var dd = new Date();
                    var _url_sms = part_sms+"?d="+dsms+"&m="+mm+"&y="+yyyy+"&dd="+dd.getTime()+"&store_id="+store_id;
                    var res_sms = $.ajax({
                        url:_url_sms,
                        async: false
                    }).responseText;
                    $("#dp_list").html(res_sms);
                    $("#dp_list").slideDown();
                    $(".edit_list").click(function() {
                        var _url_edit = ($(this).attr("href"));
                        var _url_edit = part_sms_edit+$(this).attr("href");
                        window.location.href = _url_edit;
                        return false;
                    });
                    $(".delete_list").click(function() {
                        var msg_del = 'ต้องการลบกิจกรรมนี้ใช่หรือไม่?';
                        if ( confirm(msg_del) ) {
                            var _url_del = part_sms_del+"?act=del&EID="+$(this).attr("href");
                            var res_del = $.ajax({
                                url:_url_del,
                                async: false
                            }).responseText;
                            if(res_del=='1'){
                                processCal();
                                loadini();
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


