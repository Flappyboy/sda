package cn.edu.nju.software.sda.jsherp.selenium.depot.DepotDelivery;

import cn.edu.nju.software.sda.jsherp.selenium.base.BaseInfo;
import cn.edu.nju.software.sda.jsherp.selenium.base.BaseOperate;
import org.openqa.selenium.By;

public class DepotDeliveryAdd extends BaseOperate {
    public DepotDeliveryAdd() {
        super(new BaseInfo());
    }

    @Override
    protected void before() {
        driver.findElement(By.xpath("//span[contains(text(),'增加')]")).click();
        driver.findElement(By.xpath("//span[contains(text(),'新增')]")).click();
        driver.findElement(By.xpath("//div[contains(@class,'DepotId')]/table/tbody/tr/td/span/span/span")).click();
        driver.findElement(By.xpath("//div[contains(text(),'金沙店')]")).click();
        driver.findElement(By.xpath("//div[contains(@class,'MaterialId')]/table/tbody/tr/td/span/span/span")).click();
        driver.findElement(By.xpath("//div[contains(text(),'棉线(A21-4321)')]")).click();
        driver.findElement(By.linkText("保存")).click();
        driver.findElement(By.linkText("确定")).click();
    }
}
