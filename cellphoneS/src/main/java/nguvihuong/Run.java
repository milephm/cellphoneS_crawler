package nguvihuong;

import nguvihuong.crawler.Crawler;
import nguvihuong.crawler.CrawlerInfo;
import nguvihuong.model.Product;
import nguvihuong.utils.JSONReader;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import nguvihuong.utils.ButtonClicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Run {
    public static void main(String[] args) throws Exception {
        ChromeOptions options = getOptions();
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://cellphones.com.vn/mobile/apple.html"); // Replace with the URL of choice
        JavascriptExecutor js = (JavascriptExecutor) driver;

        while (true) {
            js.executeScript("window.scrollBy(0,3000)");
            Thread.sleep(1500);
            ButtonClicker buttonClicker = new ButtonClicker(driver,
                    "[class*='btn-show-more button__show-more-product']");
            boolean res = buttonClicker.execute();
            if (!res) {
                break;
            }
        }

        Crawler crawler = new Crawler(driver);

        List<Product> list = new ArrayList<>();
        crawler.crawlData(list);
        Crawler.showData(list);
        Crawler.exportJSON(list, "Products");

        Thread.sleep(1500);
        List<String> urls = JSONReader.getUrls("./Products_links.json");

        for (String url : urls) {
            try {
                CrawlerInfo.crawl(driver, url, list);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            CrawlerInfo.exportToJson(list, "Products_info.json");
        }

        driver.quit();
    }

    private static ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(Arrays.asList(
                "--no-sandbox",
                "--disable-setuid-sandbox",
                "--disable-blink-features=AutomationControlled",
                "--disable-infobars",
                "--ignore-certificate-errors",
                "--disable-extensions",
                "--incognito",
                "--disable-dev-shm-usage",
                "--disable-accelerated-2d-canvas",
                "--no-first-run",
                "--no-zygote",
                "--lang=en",
                "--disable-background-timer-throttling",
                "--disable-backgrounding-occluded-windows",
                "--disable-renderer-backgrounding",
                "--disable-canvas-aa",
                "--disable-2d-canvas-clip-aa",
                "--disable-gl-drawing-for-tests",
                "--allow-running-insecure-content",
                "--disable-speech-api",
                "--disable-background-networking",
                "--disable-breakpad",
                "--disable-client-side-phishing-detection",
                "--disable-component-update",
                "--disable-default-apps",
                "--disable-domain-reliability",
                "--disable-hang-monitor",
                "--disable-ipc-flooding-protection",
                "--disable-notifications",
                "--disable-offer-store-unmasked-wallet-cards",
                "--disable-popup-blocking",
                "--disable-prompt-on-repost",
                "--disable-permissions-api",
                "--disable-sync",
                "--hide-scrollbars",
                "--ignore-gpu-blacklist",
                "--metrics-recording-only",
                "--no-default-browser-check",
                "--no-pings",
                "--password-store=basic",
                "--use-gl=swiftshader",
                "--use-mock-keychain",
                "--disable-web-security",
                "--disable-site-isolation-trials"));
        return options;
    }
}