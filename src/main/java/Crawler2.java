import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class Crawler2 {
    public static void crawl(WebDriver driver, String url) {
        System.out.println("=====================================");

        driver.get(url);

        // example: extract title
        String title = (driver.findElement(By.className("box-product-name"))).getDomProperty("textContent");
        System.out.println("Title: " + title);
        try {
            // ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/3);");
            if ((driver.findElements(By.cssSelector(".box-more-promotion-title"))).size() != 0) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", (driver.findElement(By.cssSelector(".box-more-promotion-title"))));
                Thread.sleep(1000);
            }
            if (driver.findElements(By.id("extendedWarranty")).size() != 0) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", (driver.findElement(By.id("extendedWarranty"))));
                Thread.sleep(1000);
            }
            if ((driver.findElements(By.cssSelector(".same-product-title"))).size() != 0) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", (driver.findElement(By.cssSelector(".same-product-title"))));
                Thread.sleep(1000);
            }
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 350)", "");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("got interrupted!");
        }
        // example: extract specific element
        List<WebElement> rows = driver.findElements(By.cssSelector(".modal-item-description>.px-3.is-flex"));
        for (WebElement row : rows) {
            WebElement pElement = row.findElement(By.tagName("p"));
            WebElement divOrAElement = row.findElement(By.tagName("div"));

            String pText = pElement.getDomProperty("textContent");
            String divText = divOrAElement.getDomProperty("textContent");

            System.out.println("\"" + pText + "\": \"" + divText + "\"");
        }
    }
}
