package cn.edu.nju.software.sda.jsherp.selenium.depot;

import cn.edu.nju.software.sda.jsherp.selenium.base.BaseInfo;
import cn.edu.nju.software.sda.jsherp.selenium.base.BaseModule;
import cn.edu.nju.software.sda.jsherp.selenium.depot.DepotDelivery.DepotDelivery;

public class Depot extends BaseModule {
    public Depot() {
        super(new BaseInfo().setName("零售管理").setId("23"));
        add(new DepotDelivery());
    }
}
