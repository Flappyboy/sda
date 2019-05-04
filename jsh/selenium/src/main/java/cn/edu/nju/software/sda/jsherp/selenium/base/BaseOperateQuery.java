package cn.edu.nju.software.sda.jsherp.selenium.base;

import org.openqa.selenium.By;

public class BaseOperateQuery extends BaseOperate {
    public BaseOperateQuery(BaseInfo baseInfo) {
        super(baseInfo);
    }

    @Override
    protected void before() {
        driver.findElement(By.linkText("查询")).click();
    }
}
