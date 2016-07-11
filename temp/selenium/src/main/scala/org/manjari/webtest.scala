package org.manjari


import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

object  webtest  {
  def main(args: Array[String]) {
    val driver = new FirefoxDriver()

    // And now use this to visit Google
    driver.get("http://www.google.com")

    val element = driver.findElement(By.name("q"))

    element.sendKeys("Cheese!")

    element.submit()

    System.out.println("Page title is: " + driver.getTitle())

    (new WebDriverWait(driver, 10)).until(new ExpectedCondition[Boolean]() {
      def apply( d: WebDriver): Boolean = {
        d.getTitle().toLowerCase().startsWith("cheese!")
      }
    })

    println("Page title is: " + driver.getTitle())

    driver.quit()
  }
}
