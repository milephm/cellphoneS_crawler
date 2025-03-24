import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Run {
    public static void main(String[] args) throws Exception {
        // Create a single WebDriver instance to be used by both ButtonClicker and Crawler
        WebDriver driver = new ChromeDriver();

        // Run Selenium scrolling and clicking first, passing the driver
        ButtonClicker buttonClicker = new ButtonClicker(driver); // Pass the WebDriver to ButtonClicker
        buttonClicker.execute(); // This will scroll and click the buttons

        // Create Crawler with the same WebDriver instance
        Crawler crawler = new Crawler(driver); // Pass the same WebDriver instance to Crawler

        // Execute the crawler using the same WebDriver instance
        List<Pair> list = new ArrayList<>();
        crawler.crawlData(list); // Calling crawlData on the crawler instance
        Crawler.sortData(list);
        Crawler.showData(list);
        Crawler.exportCSV(list, "Products");

        driver.quit();  // Close the WebDriver after crawling
    }
}
