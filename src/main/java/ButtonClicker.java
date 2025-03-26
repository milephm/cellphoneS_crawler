import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ButtonClicker {
    private WebDriver driver;

    String selector;

    public ButtonClicker(WebDriver driver, String selector) {
        this.driver = driver;
        this.selector = selector;
    }

    public boolean execute() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        List<WebElement> buttons = driver.findElements(By.cssSelector(selector));
        if (buttons.isEmpty()) {
            return false;
        }

        boolean clicked = false;
        for (WebElement button : buttons) {
            try {
                if (!button.getText().trim().isEmpty()) {
                    js.executeScript("arguments[0].click()", button);
                    clicked = true;
                }
            } catch (Exception e) {
                System.out.println("Failed to click button: " + e.getMessage());
            }
        }

        return clicked;
    }
}