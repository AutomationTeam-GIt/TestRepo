package CI_Project.Perfecto_CI;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.*;
import org.openqa.selenium.Platform;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
/**
 * For programming samples and updated templates refer to the Perfecto GitHub at: https://github.com/PerfectoCode
 */
	
public class RemoteWebDriverTest {
	static RemoteWebDriver driver = null;

	static GenericMethods gm = new GenericMethods();
	
	@BeforeTest
	public static void main() throws MalformedURLException, IOException {
		System.out.println("Run started");

		String browserName = "mobileOS";
		DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
		String host = "accenturecoe.perfectomobile.com";
		capabilities.setCapability("user", "n.kumarasamy@accenture.com");
		capabilities.setCapability("password", "Jun@2016");

		System.out.println("Newly added");
		//TODO: Change your device ID
		capabilities.setCapability("deviceName", "4622C2B1");
		System.out.println("Capabilities are set");

		// Use the automationName capability to define the required framework - Appium (this is the default) or PerfectoMobile.
		// capabilities.setCapability("automationName", "PerfectoMobile");

		// Call this method if you want the script to share the devices with the Perfecto Lab plugin.
		PerfectoLabUtils.setExecutionIdCapability(capabilities, host);

		// Add a personal to your script (see https://community.perfectomobile.com/posts/1048047-available-personas)
		//capabilities.setCapability(WindTunnelUtils.WIND_TUNNEL_PERSONA_CAPABILITY, WindTunnelUtils.GEORGIA);

		// Name your script
		// capabilities.setCapability("scriptName", "RemoteWebDriverTest");

		driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		gm.logScreenshot("Info", "Device is successfully launched", driver);
	}

	@Test
	public void gphNavigation()
	{
		try {
			// Closing all the opened windows
			Map<String, Object> params14 = new HashMap<>();
			params14.put("keySequence", "APP_SWITCH");
			driver.executeScript("mobile:presskey", params14);

			switchToContext(driver, "NATIVE_APP");
			try{
				driver.findElementByXPath("//*[@resource-id=\"com.android.systemui:id/recents_RemoveAll_button_vzw\"]").click();}
			catch(Exception e){System.out.println("No windows opened");}


			// Navigating to GPH site and Validating some content over there
			switchToContext(driver, "WEBVIEW");
			Thread.sleep(3000);
			driver.get("https://gph-pt.accenture.com/");	
			Thread.sleep(3000);
			gm.logScreenshot("Pass", "Navigated to GPH Login page", driver);
			
			// Closing the Set Secure screen lock window
			switchToContext(driver, "NATIVE_APP");
			try{
				driver.findElementByXPath("//*[@resource-id=\"android:id/button2\"]").click();}
			catch(Exception e){System.out.println("No popup windows");}


			Map<String, Object> params1 = new HashMap<>();
			params1.put("content", "Enterprise Sign On");
			params1.put("timeout", "60");
			driver.executeScript("mobile:checkpoint:text", params1);

			switchToContext(driver, "WEBVIEW");
			driver.findElementByXPath("//*[@id=\"userNameInput\"]").sendKeys("Adtestid100");			
			driver.findElementByXPath("//*[@id=\"passwordInput\"]").sendKeys("Rs5MtY42tghBRT7FD");		
			driver.findElementByXPath("//*[@id=\"submitButton\"]").click();
			Thread.sleep(2000);
			gm.logScreenshot("Pass", "Navigated to GPH home page", driver);
			
			switchToContext(driver, "NATIVE_APP");
			Map<String, Object> params2 = new HashMap<>();
			params2.put("content", "Attendance");
			params2.put("timeout", "60");
			driver.executeScript("mobile:checkpoint:text", params2);

			// Log out from the application			
			switchToContext(driver, "WEBVIEW");
			driver.findElementByXPath("//*[@id=\"gphHamburger\"]").click();
			gm.logScreenshot("Info", "Clicked on Hamburger Icon in the GPH Home page", driver);

			Thread.sleep(2000);
			driver.findElementByXPath("//*[text()=\"Settings\"]").click();
			Thread.sleep(2000);
			driver.findElementByXPath("//*[@class=\"links-text-mobile\" and text()='Log-Off']").click();

			
			Map<String, Object> params3 = new HashMap<>();
			params3.put("content", "you have successfully logged-off");
			params3.put("timeout", "60");
			driver.executeScript("mobile:checkpoint:text", params3);
			
			gm.logScreenshot("Pass", "Successfully logged out of GPH Application", driver);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Retrieve the URL of the Single Test Report, can be saved to your execution summary and used to download the report at a later point
				@SuppressWarnings("unused")
				String reportURL = (String)(driver.getCapabilities().getCapability(WindTunnelUtils.SINGLE_TEST_REPORT_URL_CAPABILITY));

				driver.close();

				// In case you want to download the report or the report attachments, do it here.
				PerfectoLabUtils.downloadReport(driver, "pdf", "C:\\Report Folder\\report");
				// PerfectoLabUtils.downloadAttachment(driver, "video", "C:\\test\\report\\video", "flv");
				// PerfectoLabUtils.downloadAttachment(driver, "image", "C:\\test\\report\\images", "jpg");

			} catch (Exception e) {
				e.printStackTrace();
			}
			driver.quit();
		}
		System.out.println("Run ended");
	}

	private static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	@SuppressWarnings("unused")
	private static String getCurrentContextHandle(RemoteWebDriver driver) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}

	@SuppressWarnings("unused")
	private static List<String> getContextHandles(RemoteWebDriver driver) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		@SuppressWarnings("unchecked")
		List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}
}
