package csv;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;

public class CsvStuff {

    public String CSV_FILE_PATH; //"src/main/java/tmp/Movies.csv";

    public CsvStuff(){
        this(null);
    }

    public CsvStuff(String path){
        this.CSV_FILE_PATH = path;
    }

    public String getCSV_FILE_PATH() {
        return CSV_FILE_PATH;
    }

    public void setCSV_FILE_PATH(String cSV_FILE_PATH) {
        CSV_FILE_PATH = cSV_FILE_PATH;
    }
    
    public List<List<String>> CsvReader(String file){
        try {

            CSVReader csvReader = new CSVReader(new FileReader(file));
            List<List<String>> linhas = new ArrayList<List<String>>();
            String[] columns;
      
            while((columns = csvReader.readNext()) != null){
                linhas.add(Arrays.asList(columns));
            }

            csvReader.close();
            return linhas;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
