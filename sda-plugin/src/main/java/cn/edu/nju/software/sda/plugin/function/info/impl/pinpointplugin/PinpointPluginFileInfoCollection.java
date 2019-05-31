package cn.edu.nju.software.sda.plugin.function.info.impl.pinpointplugin;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.info.FileInfo;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import cn.edu.nju.software.sda.core.domain.meta.FormDataType;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.meta.MetaFormDataItem;
import cn.edu.nju.software.sda.core.domain.meta.MetaInfoDataItem;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.utils.FileCompress;
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;
import cn.edu.nju.software.sda.plugin.function.info.impl.staticjava.ClassAdapter;
import cn.edu.nju.software.sda.plugin.function.info.impl.staticjava.JavaData;
import org.springframework.asm.ClassReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class PinpointPluginFileInfoCollection extends InfoCollection {

    public static final String INFO_NAME = "SYS_PINPOINT_PLUGIN_INFO";

    public static final String ServiceTypeFormName = "Service Type";

    public static final String KeyCodeFormName = "Key Code";

    public static final String PluginNameFormName = "Plugin Name";

    @Override
    public MetaData getMetaData() {
        MetaData metaData = new MetaData();
        metaData.addMetaDataItem(new MetaFormDataItem(PluginNameFormName, FormDataType.STRING));
        metaData.addMetaDataItem(new MetaFormDataItem(ServiceTypeFormName, FormDataType.STRING));
        metaData.addMetaDataItem(new MetaFormDataItem(KeyCodeFormName, FormDataType.STRING));
        metaData.addMetaDataItem(new MetaInfoDataItem(Node.INFO_NAME_NODE));
        return metaData;
    }

    @Override
    public ResultDto check(InputData inputData) {
        return ResultDto.ok();
    }

    @Override
    public InfoSet work(InputData inputData, Work work) {
        NodeInfo nodeInfo = inputData.getInfoSet().getInfoByClass(NodeInfo.class);
        Set<ClassNode> classNodes =  nodeInfo.getNodeSet().getSetByClass(ClassNode.class);

        String pluginName = (String) inputData.getFormDataObjs(getMetaData()).get(PluginNameFormName).get(0);
        Integer serviceType = Integer.parseInt((String) inputData.getFormDataObjs(getMetaData()).get(ServiceTypeFormName).get(0));
        Integer keyCode = Integer.parseInt((String) inputData.getFormDataObjs(getMetaData()).get(ServiceTypeFormName).get(0));

        Generate generate = new Generate(pluginName, serviceType, keyCode, classNames(classNodes), work.getWorkspace().getPath());
        File file = generate.generateJar();

        FileInfo fileInfo = new FileInfo(INFO_NAME);
        fileInfo.setFile(file);

        return new InfoSet(fileInfo);
    }

    private String classNames(Set<ClassNode> classNodes){
        StringBuilder sb = new StringBuilder();
        for (ClassNode node:
             classNodes) {
            sb.append(node.getName().replace("/","."));
            sb.append(" ");
        }
        return sb.toString();
    }

    @Override
    public String getName() {
        return "sys_PinpointPluginInfoCollection";
    }

    @Override
    public String getDesc() {
        return "Generate the pinpoint plugin for collect dynamic data from pinpoint.";
    }
}
