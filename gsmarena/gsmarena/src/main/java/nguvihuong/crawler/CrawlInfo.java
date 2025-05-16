package nguvihuong.crawler;

import nguvihuong.model.Product;
import static nguvihuong.utils.BrandList.BRANDS;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CrawlInfo {
    public static void crawl(WebDriver driver, String url, Map<String, Product> productInfo) {
        System.out.println("=====================================");
        driver.get(url);

        // get title
        String name = (driver.findElement(By.className("specs-phone-name-title"))).getDomProperty("textContent");
        if (name != null) {
            for (String brand : BRANDS) {
                if (name.startsWith(brand + " ")) {
                    name = name.substring(brand.length()).trim();
                    break;
                }
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
                            String ttl = ttlElements.getFirst().getDomProperty("textContent");
                            String nfo = nfoElements.getFirst().getDomProperty("textContent").trim();

                            specs.put(ttl, nfo);
                        }
                    } catch (Exception e) {
                        System.out.println("Error processing a table row: " + e.getMessage());
                    }
                }
            }
        }

        Product product = null;
        for (String keyName : productInfo.keySet()) {
            if (name != null && name.equals(keyName)) {
                product = productInfo.get(keyName);
                break;
            }
        }

        if (product != null) {
            for (Map.Entry<String, String> entry : specs.entrySet()) {
                product.setInfo(entry.getKey(), entry.getValue());
            }
        }
    }

    // export to JSON
    public static void exportToJson(List<Product> productInfo, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);
        List<Map<String, Object>> products = mapper.readValue(file, List.class);

        int minSize = Math.min(products.size(), productInfo.size());

        for (int i = 0; i < minSize; i++) {
            Map<String, Object> product = products.get(i);

            // check if the product contains an "info" field
            if (product.get("info") == null || ((Map<?, ?>) product.get("info")).isEmpty()) {
                Map<String, String> specs = productInfo.get(i).getInfo();
                if (specs != null && !specs.isEmpty()) {
                    product.put("info", specs);
                }
            }
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, products);
        System.out.println("Data successfully exported to " + fileName);
        System.out.println("JSON products size: " + products.size());
        System.out.println("Collected productInfo size: " + productInfo.size());
    }
}