package operation;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class UIOperation {
	WebDriver driver;
	String status="Pass";
	public UIOperation(WebDriver driver){
		this.driver = driver;
	}
	
	public Logger logger = Logger.getLogger("flightReservation");
	
	public String perform(Properties p,String operation,String objectType,String objectName,String value){
		switch(operation.toUpperCase()){
		case "GO TO URL":
			logger.info("Opening Application Using URL :"+p.getProperty("URL"));
			driver.get(p.getProperty(objectName));
			logger.info("Opened Application Using URL :"+p.getProperty("URL"));
			break;
		case "SET":
			logger.info("Entering '"+value+"' in the '"+objectName+"' edit field");                                                           
			WebElement elementToBeSet = driver.findElement(this.getObject(p,objectType,objectName));
			highlightElement(elementToBeSet);
			elementToBeSet.sendKeys(value);
			logger.info("Entered '"+value+"' in the '"+objectName+"' edit field"); 
			break;
		case "CLICK":
			logger.info("Clicking on '"+objectName+"'");
			WebElement elementToBeClicked = driver.findElement(this.getObject(p, objectType, objectName));
			highlightElement(elementToBeClicked);
			elementToBeClicked.click();
			logger.info("Clicked on '"+objectName+"'");
			break;
		case "SELECT":
			logger.info("Selecting '"+value+"' from '"+objectName+"' drop down");
			WebElement elementToBeSelected = driver.findElement(this.getObject(p, objectType, objectName));
			highlightElement(elementToBeSelected);
			Select select = new Select(elementToBeSelected);
			select.selectByVisibleText(value);
			logger.info("Selected '"+value+"' from '"+objectName+"' drop down");
			break;
		case "VERIFY TITLE":
			logger.info("Verifying Title '"+value+"'");
			if(driver.getTitle().equals(value)){
				status = "Pass";
				
			}else{
				status= "Fail";
				logger.error("Status --- Failed | #Expected Title: '"+value+"' | #Actual Title: '"+driver.getTitle()+"'");
			}
			logger.info("Verified Title '"+value+"'");
			break;
			
		case "VERIFY TEXT":
			logger.info("Verifying Text '"+value+"'");
			if(driver.getPageSource().contains(value)){
				status = "Pass";
			}else{
				status = "Fail";
				logger.error("Status -- Failed | #Expected Text: '"+value+"' | ##Actual Text: '"+driver.getPageSource());
			}
			logger.info("Verified Text '"+value+"'");
			break;
		case "VERIFY ELEMENT":
			logger.info("Verifying element : '"+objectName+"'");
			WebElement elementToBeVerified = driver.findElement(this.getObject(p, objectType, objectName));
			highlightElement(elementToBeVerified);
			if(elementToBeVerified.isDisplayed()){
				status = "Pass";
			}else{
				status = "Fail";
				logger.error("Status -- Failed | '"+objectName+"' is not displayed");
			}
			logger.info("Verified element : '"+objectName+"'");
			break;
		default:
			System.out.println("Wrong Operation Type...");
			status = "Fail";
			logger.error("Status -- Failed | Wrong Operation Type : "+operation);
			
		}
		
		return status;
	}

	private By getObject(Properties p, String objectType, String objectName) {
		if(objectType.equals("ID")){
			return By.id(p.getProperty(objectName));
		}else if(objectType.equals("NAME")){
			return By.name(p.getProperty(objectName));
		}else if(objectType.equals("XPATH")){
			return By.xpath(p.getProperty(objectName));
		}else if(objectType.equals("CSS")){
			return By.cssSelector(p.getProperty(objectName));
		}else if(objectType.equals("LINK TEXT")){
			return By.linkText(p.getProperty(objectName));
		}else if(objectType.equals("TAG NAME")){
			return By.tagName(p.getProperty(objectName));
		}else{
			return By.partialLinkText(p.getProperty(objectName));
		}
	}
	
	private void highlightElement(WebElement element){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].setAttribute('style','background: yellow; border: 2px solid red;');", element);
	}

}
