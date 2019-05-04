package cn.edu.nju.software.sda.jsherp.selenium.base;

import org.openqa.selenium.By;

public abstract class BaseFunction extends Base {
    public BaseFunction(BaseInfo baseInfo) {
        super(baseInfo, 1);
    }

    @Override
    protected void eachPerformBefore() {

    }

    @Override
    protected void eachPerformAfter() {

    }

    @Override
    protected void before() {
        driver.findElement(By.linkText(baseInfo.getName())).click();
        driver.switchTo().frame(0);
    }

    @Override
    protected void eachBefore() {

    }

    @Override
    protected void after() {
        driver.switchTo().parentFrame();
    }

    @Override
    protected void eachAfter() {

    }
}
