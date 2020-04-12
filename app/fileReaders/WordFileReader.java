package fileReaders;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import item.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import item.Weight;
import org.apache.poi.xwpf.usermodel.*;

public class WordFileReader implements DataFileReader {

    protected Config config;
    public WordFileReader(){
        this.config = ConfigFactory.load();
    }

    /**
     * Read Word File
     * @return
     */
    public Service[][] readFile(){
        Service[][] result = new Service[3][];
        String path = System.getProperty("user.dir");
        try
        {
            //get the document
            String wordPath =  config.getString("wordPath");
            String filePath = path + (wordPath==null?"":wordPath)+"/Lab Sample Input.docx";
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            //find the table
            List<XWPFTable> tables = document.getTables();
            XWPFTable table = tables.get(0);
            List<XWPFTableRow> rows = table.getRows();
            List<Service>  serviceList = null;
            int j=-1;
            //loop through the table line by line
            for (int i=1; i< rows.size();i++){
                XWPFTableRow row = rows.get(i);
                List<XWPFTableCell> cells = row.getTableCells();
                int start = 1;
                if(cells.get(0).getText().startsWith("SC")){
                    if(j>=0){
                        result[j]=serviceList.toArray(new Service[serviceList.size()]);
                    }
                    serviceList=new ArrayList<>();
                    j++;
                }
                Service tempService = new Service(
                        cells.get(start).getText(),
                        Double.parseDouble(cells.get(start+1).getText()),
                        Double.parseDouble(cells.get(start+2).getText())/100,
                        Double.parseDouble(cells.get(start+3).getText()),
                        Double.parseDouble(cells.get(start+4).getText())/100
                );
                serviceList.add(tempService);
            }
            result[j]=serviceList.toArray(new Service[serviceList.size()]);

            List<XWPFParagraph> ps = document.getParagraphs();
            for(int i=8; i<=11;i++){
                XWPFParagraph p = ps.get(i);
                String line = p.getText();
                String[] strs = line.split(" ");
                Double value = Double.parseDouble(strs[1].substring(1,3))/100;
                switch(strs[0]){
                    case "Cost":{
                        Weight.c = value;
                    }
                    case "Reliability":{
                        Weight.r = value;
                    }
                    case "Performance":{
                        Weight.p = value;
                    }
                    case "Availability":{
                        Weight.a = value;
                    }
                }
            }
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
