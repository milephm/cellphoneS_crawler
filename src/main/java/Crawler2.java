import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Crawler2 {
    private static final List<Map<String, String>> productsData = new ArrayList<>();

    public static void crawl(WebDriver driver, String url) {
        System.out.println("=====================================");

        driver.get(url);

        Map<String, String> productData = new LinkedHashMap<>();

        // example: extract title
        String title = (driver.findElement(By.className("box-product-name"))).getDomProperty("textContent");
        if(title != null) {
           title = title.replace("| Chính hãng VN/A ", "");
           title = title.trim();
           productData.put("name", title);
        }
        try {
            // ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/3);");
            if (!(driver.findElements(By.cssSelector(".box-more-promotion-title"))).isEmpty()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", (driver.findElement(By.cssSelector(".box-more-promotion-title"))));
                Thread.sleep(500);
            }
            if (!driver.findElements(By.id("extendedWarranty")).isEmpty()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", (driver.findElement(By.id("extendedWarranty"))));
                Thread.sleep(500);
            }
            if (!(driver.findElements(By.cssSelector(".same-product-title"))).isEmpty()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", (driver.findElement(By.cssSelector(".same-product-title"))));
                Thread.sleep(500);
            }
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 350)", "");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }

        // example: extract specific element
        List<WebElement> rows = driver.findElements(By.cssSelector(".modal-item-description>.px-3.is-flex"));
        for (WebElement row : rows) {
            WebElement pElement = row.findElement(By.tagName("p"));
            WebElement divOrAElement = row.findElement(By.tagName("div"));

            String pText = pElement.getDomProperty("textContent");
            String divText = divOrAElement.getDomProperty("textContent");
            productData.put(pText, divText);

            /* making sure all infos are being crawled */
            //System.out.println("\"" + pText + "\": \"" + divText + "\"");
        }

        productsData.add(productData);

        try {
            exportToJson(productsData, "Products_data.json");
        } catch (IOException e) {
            System.err.println("Failed to export data to JSON: " + e.getMessage());
        }
    }

    // export to JSON
    public static void exportToJson(List<Map<String, String>> productsData, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(fileName);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, productsData);  // format JSON
        System.out.println("Data successfully exported to " + fileName);
    }
}
