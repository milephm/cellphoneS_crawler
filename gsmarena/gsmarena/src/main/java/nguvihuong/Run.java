package nguvihuong;

import nguvihuong.crawler.*;
import nguvihuong.model.Product;
import nguvihuong.utils.*;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.*;
import java.util.regex.*;

public class Run {
    public static void main(String[] args) throws Exception {
        ChromeOptions options = getOptions();
        WebDriver driver = new ChromeDriver(options);

        List<String> brandUrls = JSONReader.getUrls("Brands_urls.json");

        for (String brandUrl : brandUrls) {
            List<Product> list = new ArrayList<>();

            driver.get(brandUrl);
            CrawlLink crawler = new CrawlLink(driver);

            while (true) {
                Thread.sleep(1000); // consider replacing with wait
                crawler.crawlData(list);

                ButtonClicker buttonClicker = new ButtonClicker(driver,
                        "a.prevnextbutton[title*='Next page']");
                boolean res = buttonClicker.execute();
                if (!res) break;
            }

            Pattern pattern = Pattern.compile("([a-zA-Z0-9]+)-");
            Matcher matcher = pattern.matcher(brandUrl);
            String brandName = matcher.find() ? matcher.group(1) : "UnknownBrand";

            // Export links
            CrawlLink.exportJSON(list, "products/" + brandName);

            // Prepare info export
            Map<String, Product> productMap = new LinkedHashMap<>();
            for (Product product : list) {
                productMap.put(product.getName(), product);
            }

            List<String> urls = new ArrayList<>();
            for (Product product : list) {
                if (product.getLink() != null) {
                    urls.add(product.getLink());
                }
            }

            for (String url : urls) {
                try {
                    CrawlInfo.crawl(driver, url, productMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Export info
            CrawlInfo.exportToJson(new ArrayList<>(productMap.values()), "products/" + brandName + "_info.json");
        }

        driver.quit();
    }

    private static ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments(Arrays.asList(
                "--no-sandbox",
                "--disable-setuid-sandbox",
                "--disable-blink-features=AutomationControlled",
                "--disable-extensions",
                "--disable-dev-shm-usage",
                "--disable-background-networking",
                "--disable-background-timer-throttling",
                "--disable-renderer-backgrounding",
                "--disable-sync",
                "--disable-web-security",
                "--ignore-certificate-errors",
                "--ignore-gpu-blacklist",
                "--mute-audio",
                "--disable-notifications",
                "--incognito",
                "--disable-features=AudioServiceOutOfProcess,IsolateOrigins,site-per-process",
                "--disable-site-isolation-trials",
                "--disable-popup-blocking",
                "--lang=en",
                "--hide-scrollbars",
                "--metrics-recording-only",
                "--use-gl=swiftshader",
                "--no-first-run",
                "--no-default-browser-check",
                "--disable-client-side-phishing-detection",
                "--disable-gpu",
                "--disable-images",
                "--blink-settings=imagesEnabled=false",
                "--disable-javascript"
        ));
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");
        options.addArguments("--window-size=720,720");

        return options;
    }
}
