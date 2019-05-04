package cn.edu.nju.software.sda.jsherp.selenium;

import cn.edu.nju.software.sda.jsherp.selenium.base.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");//chromedriver服务地址
        WebDriver driver = new ChromeDriver(); //新建一个WebDriver 的对象，但是new 的是FirefoxDriver的驱动

        Base.driver = driver;

        Jsh jsh = new Jsh();
        jsh.perform();

//        wait(driver);

//        driver.switchTo().frame("mainframe");
//        driver.findElement(By.xpath("//img[@alt='零售管理']")).click();
//
//        driver.switchTo().frame(0);
//        driver.findElement(By.linkText("零售出库")).click();
//
//        driver.switchTo().frame(0);
//        driver.findElement(By.xpath("//span[contains(text(),'增加')]")).click();
//        driver.findElement(By.xpath("//span[contains(text(),'新增')]")).click();
//        driver.findElement(By.xpath("//div[contains(@class,'DepotId')]/table/tbody/tr/td/span/span/span")).click();
//        driver.findElement(By.xpath("//div[contains(text(),'金沙店')]")).click();
//        driver.findElement(By.xpath("//div[contains(@class,'MaterialId')]/table/tbody/tr/td/span/span/span")).click();
//        driver.findElement(By.xpath("//div[contains(text(),'棉线(A21-4321)')]")).click();
//        driver.findElement(By.linkText("保存")).click();
//        driver.findElement(By.linkText("确定")).click();


        /**
         * dr.quit()和dr.close()都可以退出浏览器,简单的说一下两者的区别：第一个close，
         * 如果打开了多个页面是关不干净的，它只关闭当前的一个页面。第二个quit，
         * 是退出了所有Webdriver所有的窗口，退的非常干净，所以推荐使用quit最为一个case退出的方法。
         */
//        driver.quit();//退出浏览器
    }

    public static void wait(WebDriver driver) {
        try {
            /**
             * WebDriver自带了一个智能等待的方法。
             dr.manage().timeouts().implicitlyWait(arg0, arg1）；
             Arg0：等待的时间长度，int 类型 ；
             Arg1：等待时间的单位 TimeUnit.SECONDS 一般用秒作为单位。
             */
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
