package cn.edu.nju.software.sda.jsherp.selenium.base;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public abstract class Base {
    public static WebDriver driver;
    protected BaseInfo baseInfo;
    protected int count;

    protected List<Base> baseList = new ArrayList<>();

    public Base(BaseInfo baseInfo, int count) {
        this.baseInfo = baseInfo;
        this.count = count;
    }

    public void perform() {
        this.before();
        for (Base base :
                baseList) {
            this.eachBefore();
            for (int i = 0; i < base.getCount(); i++) {
                this.eachPerformBefore();
                base.perform();
                this.eachPerformAfter();
            }
            this.eachAfter();
        }
        this.after();
    }

    abstract protected void before();

    abstract protected void eachBefore();

    abstract protected void eachPerformBefore();

    abstract protected void after();

    abstract protected void eachAfter();

    abstract protected void eachPerformAfter();

    protected void add(Base base) {
        baseList.add(base);
    }

    public int getCount() {
        return count;
    }
}
