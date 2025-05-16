package nguvihuong.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nguvihuong.model.Product;

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

    public static List<Product> readProducts(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), new TypeReference<List<Product>>() {});
    }
}
