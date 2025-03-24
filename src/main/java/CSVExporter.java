import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.opencsv.CSVWriter;
public class CSVExporter {
    private List<String[]> data;
    public CSVExporter(List<String[]> data){
        this.data = data;
    }
    public List<String[]> createCsvDataSimple() {
        return this.data;
    }
    public void writeCSV(String[] title, String filename) throws IOException{
        List<String[]> info = createCsvDataSimple();
        try (CSVWriter writer = new CSVWriter(new FileWriter(filename + ".csv"))) {
            writer.writeNext(title);
            writer.writeAll(info);
        }
    }
}
