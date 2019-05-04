package cn.edu.nju.software.sda.jsherp.selenium.depot.DepotDelivery;

import cn.edu.nju.software.sda.jsherp.selenium.base.*;

public class DepotDelivery extends BaseFunction {
    public DepotDelivery() {
        super(new BaseInfo().setName("零售出库"));
//        add(new DepotDeliveryAdd());
        add(new BaseOperateDetail(new BaseInfo()));
//        add(new BaseOperateDel(new BaseInfo()));
//        add(new BaseOperateOk(new BaseInfo()));
//        add(new BaseOperateUndo(new BaseInfo()));
//        add(new BaseOperateQuery(new BaseInfo()));
    }

    @Override
    protected void before() {
        super.before();
    }

    @Override
    protected void after() {
        super.after();
    }
}
