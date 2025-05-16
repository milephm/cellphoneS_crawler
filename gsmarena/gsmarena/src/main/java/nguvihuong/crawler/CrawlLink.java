package nguvihuong.crawler;

import nguvihuong.model.Product;
import nguvihuong.utils.JSONExporter;

import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

public class CrawlLink {
    private final WebDriver driver;
    private static final String baseURL = "https://www.gsmarena.com/";
    public CrawlLink(WebDriver driver) {
        this.driver = driver;
    }

    public void crawlData(List<Product> productInfo) {
        Document doc = Jsoup.parse(driver.getPageSource());
        Elements products = doc.select("div.makers ul li");

        // get brand
        String brand = extractBrand(doc);

        for (Element product : products) {
            Product p = extractProduct(product);
            p.setBrand(brand);
            productInfo.add(p);
        }
    }

    private Product extractProduct(Element product) {
        String name = extractName(product);
        String link = extractLink(product);
        String imgUrl = extractImageUrl(product);

        Map<String, String> info = new LinkedHashMap<>();
        return new Product(name, link, imgUrl, info, null);
    }

    private String extractBrand(Document doc) {
        return doc.select("h1.article-info-name")
                .text()
                .replace(" phones", "")
                .trim();
    }

    private String extractName(Element product) {
        Elements nameElements = product.select("a > strong > span");
        return nameElements.isEmpty() ? "" : nameElements.text().trim();
    }

    private String extractLink(Element product) {
        Elements linkElements = product.select("a");
        return linkElements.isEmpty() ? baseURL : baseURL + linkElements.attr("href");
    }

    private String extractImageUrl(Element product) {
        return product.select("a > img").attr("src");
    }

    // just to check if the crawled data is correct
    public static void showData(List<Product> productInfo){
        for (Product product : productInfo) {
            System.out.println(product.getName() + " | " + product.getLink() + " | " + product.getImgUrl());
        }
    }

    /*
    find a way to organize json file 
    (as in brand >> name >> link  >> imgURl >> info)
     */
    public static void exportJSON(List<Product> productInfo, String filename) throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        List<String> links = new ArrayList<>();

        for (Product product : productInfo) {
            data.add(product.toMap());
            links.add(product.getLink());
        }

        JSONExporter exporter = new JSONExporter();
        exporter.writeJSON(data, filename + "_info");
        exporter.writeJSON(links, filename + "_links");
    }
}