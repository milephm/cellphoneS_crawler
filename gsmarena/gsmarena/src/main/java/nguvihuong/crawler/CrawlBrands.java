package nguvihuong.crawler;

import nguvihuong.utils.JSONExporter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrawlBrands {
    // url: https://www.gsmarena.com/makers.php3
    // private final List<String[]> phoneBrands = new ArrayList<>();
    private final String url = "https://www.gsmarena.com/";

    public static void main(String[] args) throws IOException {
        CrawlBrands scraper = new CrawlBrands();
        List<String[]> brands = scraper.crawlPhoneBrands();

        // print test
        for (String[] brand : brands) {
            System.out.println("Brand Name: " + brand[0] + ", Devices: " + brand[1] + ", URL: " + brand[2]);
        }

        exportBrandURLs(brands, "Brands");
    }

    public Document crawlHtmlPage(String subUrl) {
        String fullUrl = url + subUrl; // URL for HTML content parsing
        Document doc = null;

        try {
            Thread.sleep(1500);

            // user agent
            doc = Jsoup.connect(fullUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(3000)
                    .get();

            return doc;
        } catch (IOException e) {
            System.out.println("Please check your network connection and re-run the script.");
            System.exit(1);
        } catch (InterruptedException e) {
            System.out.println("Sleep operation was interrupted.");
            Thread.currentThread().interrupt();
            System.exit(1);
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            System.exit(1);
        }

        return doc;
    }

    public List<String[]> crawlPhoneBrands() {
        List<String[]> phoneBrands = new ArrayList<>();
        Document doc = crawlHtmlPage("makers.php3");

        if (doc != null) {
            Element table = doc.select("table").first();
            if (table != null) {
                Elements links = table.select("a");

                for (Element a : links) {
                    String href = a.attr("href");
                    String brandName = href.split("-")[0];
                    String brandDevice = a.select("span").text().split(" ")[0];
                    href = url + href ;

                    String[] brandInfo = {brandName, brandDevice, href};
                    phoneBrands.add(brandInfo);
                }
            }
        }

        return phoneBrands;
    }

    public static void exportBrandURLs(List<String[]> brandInfo, String filename) throws IOException {
        List<String> urlList = new ArrayList<>();

        for (String[] brand : brandInfo) {
            if (brand.length >= 3) {
                urlList.add(brand[2]);
            }
        }

        JSONExporter exporter = new JSONExporter();
        exporter.writeJSON(urlList, filename + "_urls");
    }
}
