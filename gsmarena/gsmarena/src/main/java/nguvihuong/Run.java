package nguvihuong;

import nguvihuong.crawler.CrawlLink;
import nguvihuong.crawler.CrawlInfo;
import nguvihuong.model.Product;
import nguvihuong.utils.JSONReader;
import nguvihuong.utils.ButtonClicker;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.util.*;

public class Run {
    public static void main(String[] args) throws Exception {
        ChromeOptions options = new ChromeOptions();
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
                "--disable-client-side-phishing-detection"));

        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.gsmarena.com/apple-phones-48.php"); // Replace with the URL of choice
        JavascriptExecutor js = (JavascriptExecutor) driver;

        CrawlLink crawler = new CrawlLink(driver);
        List<Product> list = new ArrayList<>();

        while (true) {
            js.executeScript("window.scrollBy(0,3000)");
            Thread.sleep(1500);

            crawler.crawlData(list);

            ButtonClicker buttonClicker = new ButtonClicker(driver,
                    "a.prevnextbutton[title*='Next page']");
            boolean res = buttonClicker.execute();
            if (!res) {
                break;
            }
        }

        CrawlLink.showData(list);
        CrawlLink.exportJSON(list, "Products");

        Thread.sleep(1500);
        List<String> urls = JSONReader.getUrls("Products_links.json");

//        for (String url : urls) {
//            try {
//                CrawlInfo.crawl(driver, url, list);
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            }
//            CrawlInfo.exportToJson(list, "Products_info.json");
//        }

        driver.quit();
    }
}
