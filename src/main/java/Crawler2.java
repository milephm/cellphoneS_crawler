import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class Crawler2 {
    public static void crawl(WebDriver driver, String url) {
        driver.get(url);

        // example: extract title
        String title = driver.getTitle();
        System.out.println("Title: " + title);

        // example: extract specific element
        List<WebElement> rows = driver.findElements(By.cssSelector(".modal-item-description .px-3 .is-flex"));
        for (WebElement row : rows) {
            WebElement pElement = row.findElement(By.tagName("p"));
            WebElement divOrAElement = row.findElement(By.tagName("div"));
            WebElement aElement = row.findElement(By.tagName("a"));

            String pText = pElement.getText();
            String divText = divOrAElement.getText();

            // Handle cases where an 'a' tag might be present
            if (aElement != null && aElement.isDisplayed()) {
                divText = aElement.getText();
            }

            // Print the "p": "div"
            System.out.println("\"" + pText + "\": \"" + divText + "\"");
          }
    }
}
