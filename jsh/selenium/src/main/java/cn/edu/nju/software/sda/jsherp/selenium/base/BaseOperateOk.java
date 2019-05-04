package cn.edu.nju.software.sda.jsherp.selenium.base;

import org.openqa.selenium.By;

//审核
public class BaseOperateOk extends BaseOperate {
    public BaseOperateOk(BaseInfo baseInfo) {
        super(baseInfo);
    }

    @Override
    protected void before() {
        driver.findElement(By.xpath("//table[@class='datagrid-btable']/tbody/tr[1]/td[1]/div/input")).click();
        driver.findElement(By.xpath("//span[contains(text(),'审核')]")).click();
        driver.findElement(By.linkText("确定")).click();
    }
}
