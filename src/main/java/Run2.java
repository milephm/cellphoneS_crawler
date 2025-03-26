import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Run2 {
    public static void main(String[] args) {
        try {
            List<String> urls = JSONReader.getUrls("./Products_links.json");
            ChromeOptions options = new ChromeOptions();
            options.addArguments(Arrays.asList(
                    "--no-sandbox",
                    "--disable-setuid-sandbox",
                    "--disable-blink-features=AutomationControlled",
                    "--disable-infobars",
                    "--window-position=0,0",
                    "--window-size=1920,1080",
                    "--ignore-certifcate-errors",
                    "--ignore-certifcate-errors-spki-list",
                    "--disable-extensions",
                    "--start-maximized",
                    "--incognito",
                    "--disable-dev-shm-usage",
                    "--disable-accelerated-2d-canvas",
                    "--no-first-run",
                    "--no-zygote",
                    "--mute-audio",
                    "--devtools-flags=disable",
                    "--lang=en",

                    "--disable-background-timer-throttling",
                    "--disable-backgrounding-occluded-windows",
                    "--disable-renderer-backgrounding",

                    "--disable-canvas-aa",
                    "--disable-2d-canvas-clip-aa",
                    "--disable-gl-drawing-for-tests",
                    "--force-device-scale-factor",

                    "--allow-running-insecure-content",
                    "--window-position=000,000",
                    "--disable-speech-api",
                    "--disable-background-networking",
                    "--disable-background-timer-throttling",
                    "--disable-backgrounding-occluded-windows",
                    "--disable-breakpad",
                    "--disable-client-side-phishing-detection",
                    "--disable-component-update",
                    "--disable-default-apps",
                    "--disable-dev-shm-usage",
                    "--disable-domain-reliability",
                    "--disable-extensions",
                    "--disable-features=AudioServiceOutOfProcess,IsolateOrigins,site-per-process",
                    "--disable-hang-monitor",
                    "--disable-ipc-flooding-protection",
                    "--disable-notifications",
                    "--disable-offer-store-unmasked-wallet-cards",
                    "--disable-popup-blocking",
                    "--disable-print-preview",
                    "--disable-prompt-on-repost",
                    "--disable-renderer-backgrounding",
                    "--disable-setuid-sandbox",
                    "--disable-sync",
                    "--hide-scrollbars",
                    "--ignore-gpu-blacklist",
                    "--metrics-recording-only",
                    "--mute-audio",
                    "--no-default-browser-check",
                    "--no-first-run",
                    "--no-pings",
                    "--no-sandbox",
                    "--no-zygote",
                    "--password-store=basic",
                    "--use-gl=swiftshader",
                    "--use-mock-keychain",
                    "--disable-web-security",
                    "--disable-site-isolation-trials",
                    "--disable-dev-shm-usage"));

            WebDriver driver = new ChromeDriver(options);

            for (String url : urls) {
                Crawler2.crawl(driver, url);
            }

            driver.quit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
