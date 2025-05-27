package nguvihuong.crawler;

import nguvihuong.model.Product;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
//import java.util.ArrayList;

import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CrawlerInfo {
    public static void crawl(WebDriver driver, String url, List<Product> productInfo) {
        System.out.println("=====================================");

        driver.get(url);

        // example: extract title
        String title = (driver.findElement(By.className("box-product-name"))).getDomProperty("textContent");
        if (title != null) {
            title = title.replace(" | Chính hãng VN/A ", "").trim();
        }

        try {
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

        String rating = "";
        String ratingCount = "";
        List<String> comments = new ArrayList<>();

        try {
            // get rating score
            rating = driver.findElement(By.cssSelector(".title.is-4.m-0.p-0")).getText();
            System.out.println("Rating: " + rating);

            // get number of ratings
            ratingCount = driver.findElement(By.className("boxReview-score__count")).getText();
            System.out.println("Number of Ratings: " + ratingCount);

            // get top comments
            List<WebElement> commentEls = driver.findElements(By.className("comment-content"));
            System.out.println("Comments:");
            for (WebElement el : commentEls) {
                String text = el.getText().trim();
                if (!text.isEmpty()) {
                    comments.add(text);
                    System.out.println("- " + text);
                }
            }
        } catch (Exception e) {
            System.out.println("Rating, rating count, or comments not found");
        }

        Map<String, String> description = new LinkedHashMap<>();

        // example: extract specific element
        List<WebElement> rows = driver.findElements(By.cssSelector(".modal-item-description>.px-3.is-flex"));
        for (WebElement row : rows) {
            WebElement pElement = row.findElement(By.tagName("p"));
            WebElement divOrAElement = row.findElement(By.tagName("div"));

            String pText = pElement.getDomProperty("textContent").replace(".", "_");
            String divText = divOrAElement.getDomProperty("textContent");
            description.put(pText, divText);
        }

        for (Product product : productInfo) {
            if (product.getName().equals(title)) {
                product.setRating(rating);
                product.setRatingCount(ratingCount);
                product.setComments(comments);
                for (Map.Entry<String, String> entry : description.entrySet()) {
                    product.setDescription(entry.getKey(), entry.getValue());
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
            Product p = productInfo.get(i);

            // check if the product contains a "description" field
            if (product.get("description") == null || ((Map<?, ?>) product.get("description")).isEmpty()) {
                Map<String, String> description = productInfo.get(i).getDescription();
                if (description != null && !description.isEmpty()) {
                    product.put("description", description);
                }
            }
            product.put("rating", p.getRating());
            product.put("ratingCount", p.getRatingCount());
            product.put("comments", p.getComments());
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, products);
        System.out.println("Data successfully exported to " + fileName);
    }
}