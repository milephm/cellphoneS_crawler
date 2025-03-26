import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.ArrayList;
import java.util.List;

public class Run {
    public static void main(String[] args) throws Exception {
        WebDriver driver = new ChromeDriver();

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

        List<Pair> list = new ArrayList<>();
        crawler.crawlData(list);
        Crawler.sortData(list);
        Crawler.showData(list);
        Crawler.exportJSON(list, "Products");

        driver.quit();
    }
}
