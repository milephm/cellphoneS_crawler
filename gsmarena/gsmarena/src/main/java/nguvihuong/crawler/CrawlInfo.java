package nguvihuong.crawler;

import nguvihuong.model.Product;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CrawlInfo {
    public static void crawl(WebDriver driver, String url, List<Product> productInfo) {
        System.out.println("=====================================");
        String[] brands = {"Google", "Samsung", "Apple", "Xiaomi", "OnePlus"};

        driver.get(url);

        // get title
        String name = (driver.findElement(By.className("specs-phone-name-title"))).getDomProperty("textContent");
        if (name != null) {
            for (String brand : brands) {
                name = name.replace(brand, "").trim();
            }
        }
        System.out.println(name);

        Map<String, String> specs = new LinkedHashMap<>();
        // or use HashMap

        // get device spec
        WebElement body = driver.findElement(By.id("body"));
        WebElement main = body.findElement(By.id("specs-list"));

        List<WebElement> tables = main.findElements(By.tagName("table"));
        String key = "";
        for (WebElement table : tables) {
            List<WebElement> trs = table.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
            for (WebElement tr : trs) {
                List<WebElement> ths = tr.findElements(By.tagName("th"));
                if (!ths.isEmpty()) {
                    key = ths.getFirst().getText().toUpperCase();
                }

                if (!key.isEmpty()) {
                    try {
                        List<WebElement> ttlElements = tr.findElements(By.cssSelector("td.ttl"));
                        List<WebElement> nfoElements = tr.findElements(By.cssSelector("td.nfo"));

                        if (!ttlElements.isEmpty() && !nfoElements.isEmpty()) {
                            String ttl = ttlElements.get(0).getDomProperty("textContent");
                            String nfo = nfoElements.get(0).getDomProperty("textContent").trim();

                            specs.put(ttl, nfo);
                            System.out.println("\"" + ttl + "\": \"" + nfo + "\"");
                        }
                    } catch (Exception e) {
                        System.out.println("Error processing a table row: " + e.getMessage());
                    }
                }
            }
        }

        for (Product product : productInfo) {
            if (product.getName().equals(name)) {
                for (Map.Entry<String, String> entry : specs.entrySet()) {
                    product.setInfo(entry.getKey(), entry.getValue());
                }
                break;
            }
        }
    }

    // export to JSON
    public static void exportToJson(List<Product> productInfo, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);
        List<Map<String, Object>> products = mapper.readValue(file, List.class);
        for (int i = 0; i < products.size(); i++) {
            Map<String, Object> product = products.get(i);

            // check if the product contains a "info" field
            if (product.get("info") == null || ((Map<?, ?>) product.get("info")).isEmpty()) {
                Map<String, String> specs = productInfo.get(i).getInfo();
                if (specs != null && !specs.isEmpty()) {
                    product.put("info", specs);
                }
            }
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, products);
        System.out.println("Data successfully exported to " + fileName);
    }
}