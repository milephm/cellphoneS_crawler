import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Crawler {
    private final WebDriver driver;
    public Crawler(WebDriver driver) {
        this.driver = driver;
    }

    public void crawlData(List<Product> productInfo) {
        String currentUrl = driver.getCurrentUrl();
        Document doc = Jsoup.parse(driver.getPageSource());

        String savePath = "downloaded_images";

        ArrayList<Element> products = new ArrayList<>(
                doc.select("div[class*=product-info-container product-item]") /* cellphoneS */
                // doc.select("div[class*=grid-cols-2][class*=gap-2] div[class*=ProductCard_brandCard__VQQT8]") /* fpt */
        );

        //Set<String> crawledProductNames = new HashSet<>();

        for (Element product : products) {
            String name = "";
            Elements titleElements = product.select("div[class*=product__name]"); /* cellphoneS */
            // Elements titleElements = product.select("h3[class*=ProductCard_cardTitle]"); /* fpt */
            if (!titleElements.isEmpty()) {
                name = titleElements.text().replace(" | Chính hãng VN/A", "");
                name = name.trim();
            }

            // get image
            Elements img_elements = product.select("div.product__image img.product__img");
            String imgUrl = img_elements.attr("src");

            String imgName = name.replaceAll("[^a-zA-Z0-9.-]", "_") + ".png";
            String imgPath = savePath + "/" + imgName;
            try {
                downloadImage(imgUrl, imgPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String link ="";
            Elements linkElements = product.select("a[class*=product__link]");
            if (!linkElements.isEmpty()) {
                link = linkElements.attr("href"); // Get the href attribute (product URL)
            }

            int price = 0;
            try {
                Elements priceElements = product.select("p[class*=product__price--show]"); /* cellphoneS */
                // Elements priceElements = product.select("p[class*=Price_currentPrice]"); /* fpt */
                if (!priceElements.isEmpty()) {
                    String priceText = priceElements.text().replaceAll("[.đ\\s]", "");
                    price = Integer.parseInt(priceText);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing price: " + e.getMessage());
            }

            productInfo.add(new Product(name, link, price));
        }
    }
    public static void sortData(List<Product> productInfo){
        productInfo.sort((o1, o2) -> {
            if (o1.getName().compareTo(o2.getName()) != 0) {
                return o2.getName().compareTo(o1.getName());
            }
            return o2.getPrice().compareTo(o1.getPrice());
        });
    }
    public static void showData(List<Product> productInfo){
        for (Product product : productInfo) {
            System.out.println(product.getName() + ": " + product.getPrice() + " | " + product.getLink());
        }
    }
    public static void exportJSON(List<Product> productInfo, String filename) throws IOException {
        List<Object> infoList = new ArrayList<>();
        for (Product info : productInfo) {
            infoList.add(Map.of(
                    "name", info.getName(),
                    "price", info.getPrice()
            ));
        }

        List<String> linkList = new ArrayList<>();
        for (Product info : productInfo) {
            linkList.add(info.getLink());
        }

        JSONExporter jsonExporter = new JSONExporter();
        jsonExporter.writeJSON(productInfo, filename + "_info");
        jsonExporter.writeJSON(linkList, filename + "_links");
    }

    public static void downloadImage(String imgUrl, String imgPath) throws IOException {
        URL url = new URL(imgUrl);

        try (InputStream in = url.openStream();
             OutputStream out = new BufferedOutputStream(new FileOutputStream(imgPath))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}