package cn.edu.nju.software.sda.jsherp.selenium.base;

import org.openqa.selenium.By;

public class BaseOperateDel extends BaseOperate {
    public BaseOperateDel(BaseInfo baseInfo) {
        super(baseInfo);
    }

    @Override
    protected void before() {
        driver.findElement(By.xpath("//table[@class='datagrid-btable']/tbody/tr[1]/td[2]/div/img[@title='删除']")).click();
        driver.findElement(By.linkText("确定")).click();
    }
}
