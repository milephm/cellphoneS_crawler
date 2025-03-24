import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Run {
    public static void main(String[] args) throws Exception {
        WebDriver driver = new ChromeDriver();

        ButtonClicker buttonClicker = new ButtonClicker(driver);
        buttonClicker.execute();

        Crawler crawler = new Crawler(driver);

        List<Pair> list = new ArrayList<>();
        crawler.crawlData(list);
        Crawler.sortData(list);
        Crawler.showData(list);
        Crawler.exportJSON(list, "Products");

        driver.quit();
    }
}