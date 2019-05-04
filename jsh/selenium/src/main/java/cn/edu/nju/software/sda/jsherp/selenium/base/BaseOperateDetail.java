package cn.edu.nju.software.sda.jsherp.selenium.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class BaseOperateDetail extends BaseOperate {
    public BaseOperateDetail(BaseInfo baseInfo) {
        super(baseInfo);
    }

    @Override
    protected void before() {
        driver.findElement(By.xpath("//table[@class='datagrid-btable']/tbody/tr[1]/td[2]/div/img[@title='查看']")).click();
        WebElement element = driver.findElement(By.className("panel-tool-close"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
    }
}
