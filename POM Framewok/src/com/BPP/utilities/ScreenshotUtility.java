package com.BPP.utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.BPP.Base.Base;

public interface ScreenshotUtility {

public static void takeScreenshot(String tcName) throws IOException  {
		
		TakesScreenshot takescreenshot = (TakesScreenshot) Base.getDriver();
		File file = takescreenshot.getScreenshotAs(OutputType.FILE);        //Base64, Bytes
		FileUtils.copyFile(file, new File(Base.getCurdir()+"\\Screenshots//"+tcName+".jpeg"));
		
	}
	
	public static String takeScreenshot() throws IOException  {
		
		TakesScreenshot takescreenshot = (TakesScreenshot) Base.getDriver();
		File file = takescreenshot.getScreenshotAs(OutputType.FILE);
		String imagepath = Base.getCurdir()+"\\Screenshots\\"+Base.getTcName()+".jpeg";
		FileUtils.copyFile(file, new File(imagepath));
		return imagepath;
		
		//here we overloaded the static method
	}
}
