import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.IOException;
import java.util.List;

public class Run2 {
    public static void main(String[] args) {
        try {
            List<String> urls = JSONReader.getUrls("Products_links.json");
            WebDriver driver = new ChromeDriver();

            for (String url : urls) {
                Crawler2.crawl(driver, url);
            }

            driver.quit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
