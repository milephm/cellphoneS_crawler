package nguvihuong.crawler;

import nguvihuong.model.Product;
import nguvihuong.utils.JSONExporter;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

public class CrawlLink {
    private final WebDriver driver;
    public CrawlLink(WebDriver driver) {
        this.driver = driver;
    }

    public void crawlData(List<Product> productInfo) {
        Document doc = Jsoup.parse(driver.getPageSource());

        String savePath = "D:\\study\\hust\\20242\\oop_project\\crawler\\gsmarena\\src\\main\\resources\\assets\\products";

        ArrayList<Element> products = new ArrayList<>(
                doc.select("div.makers ul li") /* gsmarena */
        );

        for (Element product : products) {
            String name = "";
            Elements nameElements = product.select("a > strong > span");
            if (!nameElements.isEmpty()) {
                name = nameElements.text().trim();
            }

            // get link
            String link = "https://www.gsmarena.com/";
            Elements linkElements = product.select("a");
            if (!linkElements.isEmpty()) {
                link += linkElements.attr("href");
            }

            Map<String, String> info = new LinkedHashMap<>();
            productInfo.add(new Product(name, link, info));

            // get image
            Elements img_elements = product.select("div.product__image img.product__img");
            String imgUrl = img_elements.attr("abs:src");
            System.out.println(imgUrl);

            String imgName = name.replaceAll("[^a-zA-Z0-9.-]", "_") + ".png";
            String imgPath = savePath + "/" + imgName;
        }
    }

    public static void showData(List<Product> productInfo){
        for (Product product : productInfo) {
            System.out.println(product.getName() + ": " +  " | " + product.getLink());
        }
    }

    public static void exportJSON(List<Product> productInfo, String filename) throws IOException {
        List<Object> infoList = new ArrayList<>();
        for (Product info : productInfo) {
            infoList.add(Map.of(
                    "name", info.getName()
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

    private void downloadImage(String imgUrl, String imgPath) throws IOException {
        URL url = new URL(imgUrl);
        try (InputStream in = url.openStream();
             OutputStream out = new FileOutputStream(imgPath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}