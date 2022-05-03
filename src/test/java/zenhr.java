import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class zenhr {

	public static WebDriver driver;

	@Test(priority = 1)

	public void initialization() throws InterruptedException {

		WebDriverManager.chromedriver().setup();

		driver = new ChromeDriver();

		driver.manage().window().maximize();
		//Open the link: https://zenhr-master.staging2.devops.zenhr.com/
		driver.get("https://zenhr-master.staging2.devops.zenhr.com");

		
		//Login using username: tillawy@hotmail.com / password: password 
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

		driver.quit();
	}

	public static void verifyLinks(String linkUrl) {
		try {
			URL url = new URL(linkUrl);

			// Now we will be creating url connection and getting the response code
			HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();
			httpURLConnect.setConnectTimeout(5000);
			httpURLConnect.connect();
			if (httpURLConnect.getResponseCode() == 404) {
				System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage() + "is a broken link");
			}

			// Fetching and Printing the response code obtained
			else {
				System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage() + "   " + "not 404 link");
			}
		} catch (Exception e) {
		}
	}
}