import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONExporter {
    private List<Pair> data;

    public JSONExporter(List<Pair> data) {
        this.data = data;
    }

    public void writeJSON(String filename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename + ".json")) {
            gson.toJson(data, writer);
        }
    }
}
