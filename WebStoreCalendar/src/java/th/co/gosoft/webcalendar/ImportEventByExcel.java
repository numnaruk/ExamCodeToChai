/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.gosoft.webcalendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import th.co.gosoft.store.message.java.lib.database.DatabaseAdapter;
import th.co.gosoft.webcalendar.utils.Common;
import th.co.gosoft.webcalendar.utils.Config;
import th.co.gosoft.webcalendar.utils.NullUtils;
import th.co.gosoft.webcalendar.utils.QueryDB;

/**
 *
 * @author kunawutjar
 */
public class ImportEventByExcel extends HttpServlet {
    final private Logger log = Logger.getLogger(this.getClass().getName());
    final String poolName = Config.getDatabasePoolname();
    final int PageSize = Config.getPageSize();
    private static final int THRESHOLD_SIZE = 1024 * 300; // 300KB
    DatabaseAdapter adap = null;
    PreparedStatement pre = null;
    ResultSet rs = null;
    RequestDispatcher dp = null;
    String UPLOAD_DIRECTORY = "";
    List<Map> rowlis = null;
    String[] header = null;
    String useradmin,filename,countdata,missdata,chkType,raelnamefile;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        try {
            countdata = "";
            chkType = "";
            missdata = "";
            raelnamefile = "";
            UPLOAD_DIRECTORY = Config.getUpload_Directory();
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;

            useradmin = NullUtils.cvStr(request.getSession().getAttribute("Username"));
            request.setAttribute("timeper","F");
            out = response.getWriter();
            if(useradmin == null ? "" != null : !useradmin.equals("")){
            request.setAttribute("timeper","T");
            filename = uploadData(request,uploadPath);
            String fullPath=uploadPath+filename;

            if(filename == "" || chkTypeFile(raelnamefile)){
             chkType = "T";
             request.setAttribute("missfile",chkType);
//             File f = new File(fullPath);
//             deleteFolder(f);
            }else{
            String ret = ImportData(fullPath);
            if( ret == null ? "" == null : ret.equals("")){
                Map mapEvent = InsertDataEvent(rowlis);
                ret = InsertStoreEvent(rowlis,mapEvent);
                }
                request.setAttribute("retvalue",ret);
                request.setAttribute("missdata",missdata);
              }
            }

            dp = getServletContext().getRequestDispatcher("/jsp/importEvent.jsp");
            dp.forward(request, response);

        }catch(Throwable t){
             t.printStackTrace();
             log.error(t.toString(), t);
        }
        finally {
            out.close();
        }
    } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try{
        request.setCharacterEncoding("UTF-8");
        processRequest(request, response);
        }catch(Throwable t){
            log.error(t.toString(),t);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try{
        request.setCharacterEncoding("UTF-8");
        processRequest(request, response);
        }catch(Throwable t){
            log.error(t.toString(),t);
        }
     }

    private String ImportData(String filepath){
          String ret = "";
          String key = "";
          String value = "";
          String rowDatamisMath = "";
          Set datamiss = null;
          try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
            int countstore = 0;
            Map map = null;
            datamiss = new HashSet();
            rowlis = new LinkedList<Map>();
            File f = new File(filepath);
            FileInputStream file = new FileInputStream(f);

            //Get the workbook instance for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(file);

            //Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);
            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            header = getHead(sheet);

            if(header.length > 5 && header[0].trim().equals("ลำดับ") && header[1].trim().equals("รหัสร้าน") &&
               header[2].trim().equals("ชื่อสาขา") && header[3].trim().equals("ภาค") &&
               header[4].trim().equals("เขตช่าง")){
             while(rowIterator.hasNext()) {
                 Row row = rowIterator.next();
                 if(row.getRowNum() == 0){
                    continue;
                 }
                map = new HashMap();
                Iterator<Cell> cellIterator = row.cellIterator();
                countstore +=1;
                while(cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    key = header[cell.getColumnIndex()].toString().trim();
                   switch(cell.getCellType()){
                       case Cell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue().toString().trim() ;
                            if(!(DatamissMath(value,cell.getColumnIndex()))){
                                datamiss.add(row.getRowNum());
                            }
                         break;
                       case Cell.CELL_TYPE_NUMERIC:
                          if(DateUtil.isCellDateFormatted(cell)){
                              Date data = cell.getDateCellValue();
                              value = df.format(data);
                             if(!(DatamissMath(value,cell.getColumnIndex()))){
                                 datamiss.add(row.getRowNum());
                            }
                          }else{
                             value = cell.getNumericCellValue() + "";
                             if(!(DatamissMath(value,cell.getColumnIndex()))){
                                 datamiss.add(row.getRowNum());
                            }
                          }
                          break;
                       case Cell.CELL_TYPE_BLANK:
                             value = "";
                       break;
                       }
                       map.put(key,value);
                   }
                        
                        rowlis.add(map);
                }  
              }else{
                ret = "MIH";
                missdata = "กรุณาปรับปรุงข้อมูลชื่อ หรือ ลำดับ คอลัมน์ให้ถูกต้อง";
              }
              deleteFolder(f);
              file.close();
              }catch(Throwable t){
                  log.error(t.toString(),t);
              }
          if(!(datamiss.isEmpty())){
              ret = "MIC";
               rowDatamisMath = sortDataMiss(datamiss);
               missdata = "กรุณาปรับปรุงข้อมูลให้ถูกต้องใน:ลำดับที่(" + rowDatamisMath +")ก่อนการนำเข้าข้อมูล";
          }
        return ret;
    }
    private String[] getHead(HSSFSheet sheet){
        Row row = sheet.getRow(0);
        int index = 0;
        String [] header = new String[row.getLastCellNum()];
        Iterator<Cell> cellIterator = row.cellIterator();
        while(cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            header[index] = cell.getStringCellValue().toString().trim();
            index++;
         }
       return header;
    }
    private String uploadData(HttpServletRequest request,String uploadPath) {
        String fileName="";

        try {
             if (ServletFileUpload.isMultipartContent(request)) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(THRESHOLD_SIZE);
                factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
                ServletFileUpload upload = new ServletFileUpload(factory);
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                List formItems = upload.parseRequest(request);
                Iterator iter = formItems.iterator();
                FileItem item = null;
                while (iter.hasNext()) {
                     item = (FileItem) iter.next();
                    if (!item.isFormField()) {
                        if (item.getFieldName().equals("fileImport")) {
                           raelnamefile = new File(item.getName().replaceAll("\\\\", "//")).getName();
                            String _tmpName = useradmin + "_" + "tempExcel.xls";
                            if (!_tmpName.equals("")) {
                                File con_storeFile = new File(uploadPath + File.separator + _tmpName);
                                item.write(con_storeFile);
                            }
                            fileName = _tmpName;
                        }

                    }
                }

            }

        }catch (Throwable t){
            log.error(t.toString(), t);
        }

        return fileName;
    }
    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { // some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }else{
         folder.delete();
        }
    }
    private boolean  chkTypeFile(String filename){
        boolean typ = filename.endsWith("xls");

        return  !(typ);
    }
    private Map InsertDataEvent(List listdata){
        Map map = new HashMap();
        Map EventIdMap = new HashMap();
        HashSet Dateevent = null;
        int Event_id=0;
        int missData = 0;
        int successdata = 0;
        int datachk = 0;
        List<HashSet> Listevent = new LinkedList<HashSet>();
        for(int i = 0;i<listdata.size();i++){
         map = (Map)listdata.get(i);
         int index = 0;
         for(int j = 5;j<map.size();j++){
          if(Listevent.size() == header.length - 5){
            Dateevent = (HashSet)Listevent.get(index);
            Dateevent.add(map.get(header[j]));
            Listevent.set(index, Dateevent);
          }else{
            Dateevent = new HashSet();
            Dateevent.add(map.get(header[j]));
            Listevent.add(index, Dateevent);
          }         
          index++;
         }
        }
     for(int i = 0;i<Listevent.size();i++){
        Dateevent = (HashSet)Listevent.get(i);
      Iterator iterator = Dateevent.iterator();

      // check values
      while (iterator.hasNext()){
         try{
           int result = 0;
           String cal = iterator.next().toString();
           if(cal == ""){
               continue;
           }
           datachk++;
           String title = header[i+5];
           String message = "";
            String sql = "";
            sql = QueryDB.setEvent();
            adap = new DatabaseAdapter(poolName);
            pre = adap.getPreparedStatement(sql);
            pre.setString(1, Common.getDateCal2Base(cal));
            pre.setString(2, title);
            pre.setString(3, message);         
            pre.setString(4, useradmin);
            pre.setString(5, "import");
            pre.setString(6, raelnamefile);
            result = pre.executeUpdate();
            adap.COMMIT();

            sql = QueryDB.getMaxID();
            pre = adap.getPreparedStatement(sql);
            rs = adap.executeQuery(pre);
            while (rs.next()) {
              Event_id = rs.getInt("MaxID");
            }
            EventIdMap.put(cal+title,Event_id);
            DatabaseAdapter.closeAll(adap);
          }catch(Throwable t){
            missData++;
            DatabaseAdapter.closeResultSet(rs);
            DatabaseAdapter.closePreparedStatement(pre);
            DatabaseAdapter.closeAll(adap);
            log.error(t.toString(),t);
          }
      }
    }
        return EventIdMap;
    }
    private String InsertStoreEvent(List listdata,Map mapevent){
        Map map = new HashMap();
        int successdata = 0;
        String ret = "";
        try{
         for(int i = 0;i<listdata.size();i++){
          map = (Map)listdata.get(i);
            for(int j = 5;j<map.size();j++){
                if(mapevent.get(map.get(header[j])+header[j]) != null){
                    successdata++;
                    String sql = "";
                    String storeId = "";
                    int result = 0;
                    int Event_id = Integer.parseInt(mapevent.get(map.get(header[j])+header[j]).toString());
                    storeId = NullUtils.cv5Digit(map.get(header[1]).toString());
                    adap = new DatabaseAdapter(poolName);
                    sql = QueryDB.setStoreCalendar();
                    pre = adap.getPreparedStatement(sql);
                    pre.setString(1,storeId);
                    pre.setInt(2, Event_id);
                    result = pre.executeUpdate();
                    adap.COMMIT();

                    DatabaseAdapter.closeAll(adap);
                }

             }
          countdata = successdata +"";
         }
         ret = "REIn";
         missdata = "ทำการ Import ข้อมูลสำเร็จ";
        }catch(Throwable t){
            missdata = "เกิดข้อผิดพลาดขึ้นระหว่าง Import ข้อมูล";
              DatabaseAdapter.closePreparedStatement(pre);
              DatabaseAdapter.closeAll(adap);
               log.error(t.toString(),t);
        }

        return ret;
    }
    private boolean DatamissMath(String value , int index){

          if(index == 1 || index > 4){
              if(index == 1){
                if(Common.isValidNumeric(value)){
                    if(!(value.length() >=4 && value.length() <= 5)){
                        return false;
                    }
                }else{
                    return false;
                }
              }else{
                if(!(Common.isValidDate(value))){
                    return false;
                }
              }
          }

        return true;
    }
    private String sortDataMiss(Set datamiss){
          Iterator<Integer> data = datamiss.iterator();
          List<Integer> Sdata = new ArrayList<Integer>();
          String rowDatamisMath = "";
          try{
          while(data.hasNext()){
              Sdata.add(data.next());
          }
          Collections.sort(Sdata);
          data = Sdata.iterator();
          while(data.hasNext()){
            rowDatamisMath += " , " + data.next();
          }
           rowDatamisMath = rowDatamisMath.substring(2);
        }catch(Throwable t){
            log.error(t.toString(),t);
        }
        return rowDatamisMath;
    }
}
