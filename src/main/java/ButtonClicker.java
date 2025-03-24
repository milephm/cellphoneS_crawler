import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class ButtonClicker {
    private WebDriver driver;

    public ButtonClicker(WebDriver driver) {
        this.driver = driver;
    }

    public void execute() {
        driver.get("https://cellphones.com.vn/mobile/apple.html"); // Replace with the actual URL
        JavascriptExecutor js = (JavascriptExecutor) driver;

        while (true) {
            js.executeScript("window.scrollBy(0,3000)");

            List<WebElement> buttons = driver.findElements(By.cssSelector("[class*='button__show-more-product']"));
            if (buttons.isEmpty()) {
                break;
            }

            boolean clicked = false;
            for (WebElement button : buttons) {
                try {
                    if (!button.getText().trim().isEmpty()) {
                        Thread.sleep(1500);
                        js.executeScript("arguments[0].click()", button);
                        clicked = true;
                        System.out.println("Clicked button");
                    }
                } catch (Exception e) {
                    System.out.println("Failed to click button: " + e.getMessage());
                }
            }

            if (!clicked) {
                break;
            }
        }
    }
}
