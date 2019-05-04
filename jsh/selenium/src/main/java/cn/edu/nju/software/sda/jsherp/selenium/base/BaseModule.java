package cn.edu.nju.software.sda.jsherp.selenium.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public abstract class BaseModule extends Base {


    public BaseModule(BaseInfo baseInfo) {
        super(baseInfo, 1);
    }

    @Override
    protected void eachPerformBefore() {

    }

    @Override
    protected void eachPerformAfter() {

    }

    @Override
    protected void eachBefore() {

    }

    @Override
    protected void eachAfter() {

    }

    @Override
    protected void before() {
        driver.findElement(By.xpath("//img[@alt='" + baseInfo.getName() + "']")).click();
        driver.switchTo().frame("w_" + baseInfo.getId() + "_iframe");
    }

    @Override
    protected void after() {
        driver.switchTo().parentFrame();
        WebElement element = driver.findElement(By.id("w_" + baseInfo.getId())).findElement(By.className("ha-close"));
//        Actions actions = new Actions(driver);
//        actions.moveToElement(element).click().perform();
    }
}
