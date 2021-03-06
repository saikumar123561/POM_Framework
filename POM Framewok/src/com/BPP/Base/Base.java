package com.BPP.Base;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.BPP.utilities.DriverPaths;
import com.BPP.utilities.ScreenshotUtility;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Base {

	private static WebDriver driver;
	private static String curdir;
	private static String tcName;
	private static ExtentReports extentReports;
	private static ExtentTest extentTest;

	/*
	 * This method is used to open the browser based on parameters
	 */
	@Parameters({ "BrowserName" })
	@BeforeSuite
	public void openBrowser(@Optional("chrome") String BrowserName) {
		if (BrowserName.equalsIgnoreCase("chrome") || BrowserName.toUpperCase().equalsIgnoreCase("CHROME")) {

//			ChromeOptions options = new ChromeOptions();
//			options.addArguments("--incognito");

			curdir = System.getProperty("user.dir");
			System.setProperty(DriverPaths.chromekey, DriverPaths.chromevalue);

			driver = new ChromeDriver();
		}

		else if (BrowserName.equalsIgnoreCase("edge") || BrowserName.toUpperCase().equalsIgnoreCase("EDGE")) {

			String dir = System.getProperty("user.dir");
			System.setProperty(DriverPaths.edgekey, DriverPaths.edgevalue);
			driver = new EdgeDriver();
		}

		else {
			System.out.println(BrowserName + "is invalid");

			// throw new Exception("Invalid Browser Name");
		}

		driver.get("https://www.makemytrip.com");
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
		

	}

	/*
	 * This method is used to close the browser if driver is not pointing to null
	 */
	@AfterSuite
	public void closeBrowser() {
		if (driver != null) {
			driver.close();
		} else {
			System.out.println("driver is pointing to null");

		}
	}

	/*
	 * Track which TC is going to execute
	 */
	@BeforeMethod
	public void befroeTCExecution(Method method) {
		tcName = method.getName();
		System.out.println("Current TC Execution is: " + tcName);
		extentTest = extentReports.startTest(tcName);
		extentTest.log(LogStatus.PASS, "Current Execution TC name is: " + tcName);

	}

	/*
	 * Tracking the status of the TC, if the TC failed/skipped taking the Screenshot
	 */
	@AfterMethod
	public void afterTCExecution(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.SUCCESS) {
			System.out.println("TC is passed: " + tcName);
			extentTest.log(LogStatus.PASS, tcName + "is pass");

		} else if (result.getStatus() == ITestResult.FAILURE) {
			System.out.println("TC is failed: " + tcName);
			String imgpath = ScreenshotUtility.takeScreenshot();
			System.out.println("Screenshot path is: " + imgpath);
			System.out.println("TC is failed so taking screenshot for: " + tcName);
			extentTest.log(LogStatus.FAIL, tcName + "is fail");

		} else if (result.getStatus() == ITestResult.SKIP) {
			System.out.println("TC is skipped: " + tcName);
			String imgpath = ScreenshotUtility.takeScreenshot();
			System.out.println("Screenshot path is: " + imgpath);
			System.out.println("TC is failed so taking screenshot for: " + tcName);
			extentTest.log(LogStatus.SKIP, tcName + "is skip");
		}
		extentReports.endTest(extentTest);
		extentReports.flush();
	}

	/*
	 * 
	 */
	@BeforeTest
	public void initReports() {
		extentReports = new ExtentReports(curdir + "\\Reports\\report.html");
	}

	@AfterTest
	public void closeReports() {
		if (extentReports != null) {
			extentReports.close();

		} else {
			System.out.println("extentreports is pointing to null");
		}
	}

	/*
	 * This getDriver method is used to access the driver outside of the class
	 */
	public static WebDriver getDriver() {
		return driver;
	}

	/*
	 * This getCurDir method is used to access the curdir outside of the class
	 */
	public static String getCurdir() {
		return curdir;
	}

	/*
	 * This getter() is used to access the tcName outside of the class
	 */
	public static String getTcName() {
		return tcName;
	}

	/*
	 * This getter() is used to access the ExtentTest outside of the class
	 */
	public static ExtentTest getExtentTest() {
		return extentTest;
	}
}
