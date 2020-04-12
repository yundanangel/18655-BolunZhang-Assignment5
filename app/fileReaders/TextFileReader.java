package fileReaders;

import item.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import item.Weight;

public class TextFileReader implements DataFileReader {
    protected Config config;
    public TextFileReader(){
        this.config = ConfigFactory.load();
    }
    public Service[][] readFile(){
        Service[][] result = new Service[3][];
        List<Service> sc1 = new ArrayList<>(), sc2=new ArrayList<>(), sc3=new ArrayList<>();
        String path = System.getProperty("user.dir");
        BufferedReader reader;
        try {
            String textPath =  config.getString("textPath");
            String filePath = path + (textPath==null?"":textPath)+"/data.txt";
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            int i=0;
            while ((line = reader.readLine()) != null) {
                String[] strs = line.split(",");
                if(i<16){
                    Service tempService = new Service(strs[1], Double.parseDouble(strs[2]), Double.parseDouble(strs[3])/100, Double.parseDouble(strs[4]),Double.parseDouble(strs[5])/100);
                    switch(strs[0]) {
                        case "SC1": {
                            sc1.add(tempService);
                            break;
                        }
                        case "SC2": {
                            sc2.add(tempService);
                            break;
                        }
                        case "SC3": {
                            sc3.add(tempService);
                            break;
                        }
                        default:
                            break;
                    }
                }
                else {
                    switch(strs[0]){
                        case "c":{
                            Weight.c = Double.parseDouble(strs[1])/100;
                            break;
                        }
                        case "r":{
                            Weight.r = Double.parseDouble(strs[1])/100;
                            break;
                        }
                        case "p":{
                            Weight.p = Double.parseDouble(strs[1])/100;
                            break;
                        }
                        case "a":{
                            Weight.a = Double.parseDouble(strs[1])/100;
                            break;
                        }
                    }
                }
                i++;
            }
            result[0] = sc1.toArray(new Service[sc1.size()]);
            result[1] = sc2.toArray(new Service[sc2.size()]);
            result[2] = sc3.toArray(new Service[sc3.size()]);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
