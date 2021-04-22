import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumHelloWorld {
    public static void main(String[] args) throws InterruptedException {
        //Config the webdriver.chrome.driver which is a permanent key with the path value
        System.setProperty("webdriver.chrome.driver", "E:/Selenium/Drivers/chromedriver.exe");
        //The web driver is an interface. The ChromeDriver inherits the WebDriver. ChromeDriver will open the chrome browser for us.
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();//Make the browser open on the whole screen
        driver.get("http://google.com");//Open the specified url in the browser

        String title = driver.getTitle();//Get the page title
        System.out.println("Page title is: " + title);//Print title to the console

        String url = driver.getCurrentUrl();//Get the page Url
        System.out.println("Page address is: " + url);//Print url to the console

        Thread.sleep(3000);

        WebElement input= driver.findElement(By.name("q"));//Find the element with name "q"
        input.sendKeys("Hello World!");//Write "Hello World!" inside the input element

        Thread.sleep(3000);

        driver.findElement(By.name("btnK")).click();//Find the element with name "btnK" and click it

        Thread.sleep(3000);

        driver.quit();//Close the browser and ChromeDriver
    }
}