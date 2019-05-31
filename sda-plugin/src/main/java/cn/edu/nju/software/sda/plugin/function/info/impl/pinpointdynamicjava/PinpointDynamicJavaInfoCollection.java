package cn.edu.nju.software.sda.plugin.function.info.impl.pinpointdynamicjava;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.meta.FormDataType;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.meta.MetaFormDataItem;
import cn.edu.nju.software.sda.core.domain.meta.MetaInfoDataItem;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;

import java.io.*;
import java.util.ArrayList;

public class PinpointDynamicJavaInfoCollection extends InfoCollection {

    String ip = "Pinpoint Ip";
    String port = "Pinpoint Port";
    String name = "App Name";

    @Override
    public MetaData getMetaData() {
        MetaData metaData = new MetaData();
        metaData.addMetaDataItem(new MetaFormDataItem(ip, FormDataType.STRING));
        metaData.addMetaDataItem(new MetaFormDataItem(port, FormDataType.STRING));
        metaData.addMetaDataItem(new MetaFormDataItem(name, FormDataType.STRING));
        metaData.addMetaDataItem(new MetaInfoDataItem(Node.INFO_NAME_NODE));
        return metaData;
    }

    @Override
    public ResultDto check(InputData inputData) {
        return ResultDto.ok();
    }

    @Override
    public InfoSet work(InputData inputData, Work work) {

        return null;
    }

    @Override
    public String getName() {
        return "sys_DynamicJavaFromPinpointInfoCollection";
    }

    @Override
    public String getDesc() {
        return "Collect java program dynamic data from pinpoint.";
    }
}
