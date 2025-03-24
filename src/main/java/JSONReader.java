import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {
    public static List<String> getUrls(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(filePath));
        List<String> urls = new ArrayList<>();

        if (root.isArray()) {
            for (JsonNode node : root) {
                urls.add(node.asText());
            }
        }
        return urls;
    }
}
