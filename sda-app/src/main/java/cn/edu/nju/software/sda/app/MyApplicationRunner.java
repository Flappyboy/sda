package cn.edu.nju.software.sda.app;

import cn.edu.nju.software.sda.core.config.SdaConfig;
import cn.edu.nju.software.sda.plugin.PluginManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component//被spring容器管理
@Order(1)//如果多个自定义ApplicationRunner，用来标明执行顺序
public class MyApplicationRunner implements ApplicationRunner {
    @Value("${sda.properties}")
    private String propertiesName;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        PluginManager.getInstance();
        SdaConfig.loadAllProperties(propertiesName);
    }
}
