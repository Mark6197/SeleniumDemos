import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SeleniumTests {
    public WebDriver driver = null;

    @BeforeEach
    public void initializeSelenium() {
        //Config the webdriver.chrome.driver which is a permanent key with the path value
        System.setProperty("webdriver.chrome.driver", "E:/Selenium/Drivers/chromedriver.exe");
        //The web driver is an interface. The ChromeDriver inherits the WebDriver. ChromeDriver will open the chrome browser for us.
        driver = new ChromeDriver();
        driver.manage().window().maximize();//Make the browser open on the whole screen
    }

    @Test
    public void testUrl() {
        driver.get("http://the-internet.herokuapp.com/");//Open the specified url in the browser
        Assertions.assertEquals("http://the-internet.herokuapp.com/", driver.getCurrentUrl());//Check that the url is as expected
        driver.getPageSource();//There is an option to get the whole page html source file
    }

    @Test
    public void testAddRemoveElements() {
        driver.get("http://the-internet.herokuapp.com/add_remove_elements/");
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/button")).click();//Get the add button by it's xpath and click on it (the xpath can be extracted easily from the dev tools,
        // right click on element=>copy=>copy xpath)
        WebElement addedBtn = driver.findElement(By.className("added-manually"));//Get the added button by it's class name
        Assertions.assertEquals("Delete", addedBtn.getText());//Check that the added button's text is as expected
    }

    @Test
    public void testAddRemoveElementsManyElement() {
        driver.get("http://the-internet.herokuapp.com/add_remove_elements/");
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/button")).click();//Click 3 time on the add button
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/button")).click();
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/button")).click();
        List<WebElement> addedBtns = driver.findElements(By.className("added-manually"));//Get all the added buttons by their class name
        Assertions.assertEquals(3, addedBtns.size());//Check that the amount of added buttons is the same as expected
    }

    @Test
    public void testWritingToTextBox() {
        driver.get("http://the-internet.herokuapp.com/key_presses");
        driver.findElement(By.id("target")).sendKeys("Test Textbox");//Get the text box by it's id and write to it
        WebElement result = driver.findElement(By.id("result"));//Get the result label by it's id
        Assertions.assertEquals("You entered: X", result.getText());//The program writes only the last character that was entered to the result label
    }

    @Test
    public void testSelectList() {
        driver.get("http://the-internet.herokuapp.com/dropdown");
        Select selectList = new Select(driver.findElement(By.id("dropdown")));//Get select list by it's id and cast it to Select
        List<WebElement> options = selectList.getOptions();//Get the list of options of the select list
        Assertions.assertEquals(3, options.size());//Check that the size of the list is as expected
        selectList.selectByIndex(1);//Select option from the list by it's index
        Assertions.assertEquals(1, selectList.getAllSelectedOptions().size());//Check that only one option selected
        Assertions.assertEquals(options.get(1), selectList.getFirstSelectedOption());//Check that the selected option is as expected

        selectList.selectByValue("2");//Select option from the list by it's value
        Assertions.assertEquals(options.get(2), selectList.getFirstSelectedOption());//Check that the selected option is as expected

        selectList.selectByVisibleText("Please select an option");//Select option from the list by it's visible text
        Assertions.assertEquals(options.get(2), selectList.getFirstSelectedOption());//Check that the selected option is as expected-
        // in this case the selected option hasn't changed because we selected the disabled option

        Assertions.assertFalse(options.get(0).isEnabled());//The option at index 1 is disabled
        Assertions.assertTrue(options.get(1).isEnabled());//The option at index 1 is enabled
    }

    @Test
    public void testActionsOnPage() {
        driver.get("http://the-internet.herokuapp.com/");
        driver.findElement(By.linkText("A/B Testing")).click();//Get link by it's text and click it

        Assertions.assertEquals("http://the-internet.herokuapp.com/abtest", driver.getCurrentUrl());//Check that the url is as expected

        driver.navigate().back();//Go back

        Assertions.assertEquals("http://the-internet.herokuapp.com/", driver.getCurrentUrl());//Check that the url is as expected

        driver.navigate().forward();//Go forward

        Assertions.assertEquals("http://the-internet.herokuapp.com/abtest", driver.getCurrentUrl());//Check that the url is as expected

        driver.navigate().refresh();//Refresh the page

        Assertions.assertEquals("http://the-internet.herokuapp.com/abtest", driver.getCurrentUrl());//Check that the url is as expected
    }

    @Test
    public void testUrlWithScreenShot() {
        driver.get("http://the-internet.herokuapp.com/");
        Assertions.assertEquals("http://the-internet.herokuapp.com/", driver.getCurrentUrl());
        takeSnapShot(driver, "E://test.png");
    }

    public static void takeSnapShot(WebDriver webdriver, String fileWithPath) {

        //Convert web driver object to TakeScreenshot

        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);

        //Call getScreenshotAs method to create image file

        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination

        File DestFile = new File(fileWithPath);

        //Copy file at destination

        try {
            FileHandler.copy(SrcFile, DestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogin() {
        driver.get("http://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.name("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.className("radius")).click();
        String text= driver.findElement(By.id("flash")).getText();
        Assertions.assertEquals("You logged into a secure area!\n×",text);
    }

    @Test
    public void testAlerts() {
        driver.get("http://the-internet.herokuapp.com/javascript_alerts");
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/ul/li[1]/button")).click();
        driver.switchTo().alert().accept();
        String resultText=driver.findElement(By.id("result")).getText();
        Assertions.assertEquals("You successfully clicked an alert", resultText);

        driver.findElement(By.xpath("//*[@id=\"content\"]/div/ul/li[2]/button")).click();
        driver.switchTo().alert().accept();
        resultText=driver.findElement(By.id("result")).getText();
        Assertions.assertEquals("You clicked: Ok", resultText);

        driver.findElement(By.xpath("//*[@id=\"content\"]/div/ul/li[2]/button")).click();
        driver.switchTo().alert().dismiss();
        resultText=driver.findElement(By.id("result")).getText();
        Assertions.assertEquals("You clicked: Cancel", resultText);

        driver.findElement(By.xpath("//*[@id=\"content\"]/div/ul/li[3]/button")).click();
        driver.switchTo().alert().sendKeys("Hello");
        driver.switchTo().alert().accept();
        resultText=driver.findElement(By.id("result")).getText();
        Assertions.assertEquals("You entered: Hello", resultText);
    }

    @AfterEach
    public void cleanUp() {
        driver.quit(); // close the browser
    }
}
