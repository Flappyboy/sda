package cn.edu.nju.software.sda.jsherp.selenium;

import cn.edu.nju.software.sda.jsherp.selenium.base.*;
import cn.edu.nju.software.sda.jsherp.selenium.depot.*;
import org.openqa.selenium.By;

public class Jsh extends Base {


    public Jsh() {
        super(new BaseInfo(), 1);
        add(new Depot());
    }

    @Override
    protected void before() {
        driver.get("http://localhost:8010/login.html");//打开指定的网站
        driver.findElement(By.id("user_name")).sendKeys(new String[]{"jsh"});
        driver.findElement(By.id("user_pwd")).sendKeys(new String[]{"123456"});
        driver.findElement(By.id("btn_login")).click();
    }

    @Override
    protected void eachBefore() {
        driver.switchTo().defaultContent();
        driver.switchTo().frame("mainframe");
    }

    @Override
    protected void eachPerformBefore() {

    }

    @Override
    protected void after() {

    }

    @Override
    protected void eachAfter() {

    }

    @Override
    protected void eachPerformAfter() {

    }
}
