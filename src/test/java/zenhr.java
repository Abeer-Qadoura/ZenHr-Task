import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mongodb.assertions.Assertions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class zenhr {

	public static WebDriver driver;
	public static List<Integer> BrokenList = new ArrayList<>();

	@Test(priority = 1)

	public void initialization() throws InterruptedException {

		WebDriverManager.chromedriver().setup();

		driver = new ChromeDriver();

		driver.manage().window().maximize();
		// Open the link: https://zenhr-master.staging2.devops.zenhr.com/
		driver.get("https://zenhr-master.staging2.devops.zenhr.com");

		// Login using username: tillawy@hotmail.com / password: password
		driver.findElement(By.linkText("Log in")).click();
		driver.findElement(By.name("user[login]")).sendKeys("tillawy@hotmail.com");
		driver.findElement(By.name("user[password]")).sendKeys("password");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		// limiting the links in a certain area ( menu area )
		WebElement leftSideMenu = driver.findElement(By.className("side-nav__elements"));
		// Storing the links in a list .....
		List<WebElement> links = leftSideMenu.findElements(By.tagName("a"));
		System.out.println("No of links are " + links.size());
		// checking the links fetched.

		for (int i = 0; i < links.size(); i++) {
			WebElement E1 = links.get(i);
			String url = E1.getAttribute("href");
			verifyLinks(url);
		}
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".dropdown-menu__collapse")).click();

		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[@class='user-menu__item__link '])[9]")));
		driver.findElement(By.xpath("(//a[@class='user-menu__item__link '])[9]")).click();

		driver.quit();
	}

	public static void verifyLinks(String linkUrl) {
		// List<Integer> BrokenList = new ArrayList<>();

		try {
			URL url = new URL(linkUrl);

			// Now we will be creating url connection and getting the response code
			HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();
			httpURLConnect.setConnectTimeout(5000);
			httpURLConnect.connect();
			if (httpURLConnect.getResponseCode() == 404) {

				int st3 = httpURLConnect.getResponseCode();
				System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage() + "is a broken link");
				BrokenList.add(st3);

			}

			// Fetching and Printing the response code obtained
			else {
				System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage() + "   " + "not 404 link");
			}
		} catch (Exception e) {
		}
		Assert.assertFalse(BrokenList.size() > 0);
	}
}
