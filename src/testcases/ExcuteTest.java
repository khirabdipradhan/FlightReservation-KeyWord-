package testcases;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import excelInputAndOutput.ExcelInteraction;
import operation.ReadObject;
import operation.UIOperation;
import utility.Constant;
import utility.ReportGenerator;

public class ExcuteTest {
	ExcelInteraction excel;
	ReadObject object;
	UIOperation operation;
	ReportGenerator report;
	WebDriver driver;
	Properties allObjects;
	String status = "Pass";
	@Parameters({"browser"})
	@BeforeTest
	public void setUp(String browser) throws IOException{
		if(browser.equals("chrome")){
			System.setProperty("webdriver.chrome.driver", Constant.chromeDriverPath);
			driver = new ChromeDriver();
		}else if(browser.equals("firefox")){
			System.setProperty("webdriver.gecko.driver", Constant.geckoDriverPath);
			driver = new FirefoxDriver();
		}
		
		// Maximize the Browser
		driver.manage().window().maximize();
		
		// Delete Previous Report
		File file = new File(Constant.reportPath);
		if(file.exists()){
			file.delete();
		}
		
		// Initialize the objects
		excel = new ExcelInteraction();
		object = new ReadObject();
		allObjects = object.getObjectRepository();
		operation = new UIOperation(driver);
		report = new ReportGenerator();
		// Write System Information in the Test Execution Report
		report.generateReport("************************************************");
		report.generateReport("Project Name: FlightReservation");
		report.generateReport("Browser: "+browser);
		report.generateReport("Operating System: "+System.getProperty("os.name"));
		report.generateReport("Java Version: "+System.getProperty("java.version"));
		report.generateReport("User: "+System.getProperty("user.name"));
		InetAddress myHost = InetAddress.getLocalHost();
		report.generateReport("Host: "+myHost.getHostName());
		String dateFormat = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss").format(new Date());
		report.generateReport("Date of Execution: "+dateFormat);
		report.generateReport("************************************************");
	}
	
	@Test
	public void executeTest() throws IOException{
		Sheet sheet = excel.getSheet(Constant.filePath, Constant.fileName);
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
	
		for(int i=1;i<=rowCount;i++){
			Row row = sheet.getRow(i);
			if(row.getCell(0).getStringCellValue().trim().length() == 0){
				status = operation.perform(allObjects, row.getCell(1).getStringCellValue().trim(), row.getCell(2).getStringCellValue().trim(), row.getCell(3).getStringCellValue().trim(), row.getCell(4).getStringCellValue().trim());
				report.generateReport(row.getCell(1).getStringCellValue().trim()+"---"+row.getCell(2).getStringCellValue().trim()+"---"+row.getCell(3).getStringCellValue().trim()+"---"+row.getCell(4).getStringCellValue().trim()+"---"+status);
				Cell cell = row.getCell(5);
				cell.setCellValue(status);
				CellStyle style = excel.customizeCell(status);
				cell.setCellStyle(style);
			}else{
				report.generateReport("====================================");
				report.generateReport("Test Case : "+row.getCell(0).getStringCellValue());
				report.generateReport("====================================");
			}
		}
		
		excel.generateReport(Constant.reportPath, "TestExcutionReport.xls");
		excel.closeExcel();
	}
	
	@AfterTest
	public void tearDown(){
		driver.quit();
	}

}
