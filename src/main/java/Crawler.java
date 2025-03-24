import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

public class Crawler {
    private WebDriver driver;
    public Crawler(WebDriver driver) {
        this.driver = driver;
    }

    public void crawlData(List<Pair> productInfo) throws Exception {
        String currentUrl = driver.getCurrentUrl();
        Document doc = Jsoup.parse(driver.getPageSource());

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
                name = titleElements.text();
            }

           /*if (crawledProductNames.contains(name)) {
                continue;
            }
            crawledProductNames.add(name);*/
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

            productInfo.add(new Pair(name, link, price));
        }
    }
    public static void sortData(List<Pair> productInfo){
        Collections.sort(productInfo, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                if(o1.getName().compareTo(o2.getName()) != 0){
                    return o2.getName().compareTo(o1.getName());
                }
                return o2.getPrice().compareTo(o1.getPrice());
            }
        });
    }
    public static void showData(List<Pair> productInfo){
        for (Pair pair : productInfo) {
            System.out.println(pair.getName() + ": " + pair.getPrice() + " | " + pair.getLink());
        }
    }
    public static void exportJSON(List<Pair> productInfo, String filename) throws IOException {
        List<Object> infoList = new ArrayList<>();
        for (Pair info : productInfo) {
            infoList.add(Map.of(
                    "name", info.getName(),
                    "price", info.getPrice()
            ));
        }

        List<String> linkList = new ArrayList<>();
        for (Pair info : productInfo) {
            linkList.add(info.getLink());
        }

        JSONExporter jsonExporter = new JSONExporter();
        jsonExporter.writeJSON(productInfo, filename + "_info");
        jsonExporter.writeJSON(linkList, filename + "_links");
    }
}