package cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.meta.FormDataType;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.meta.MetaFormDataItem;
import cn.edu.nju.software.sda.core.domain.meta.MetaInfoDataItem;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.utils.FileCompress;
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitCommitInfoCollection extends InfoCollection {

    @Override
    public MetaData getMetaData() {
        MetaData metaData = new MetaData();
//        metaData.addMetaDataItem(new MetaFormDataItem("GitPath", FormDataType.STRING));
        metaData.addMetaDataItem(new MetaFormDataItem("Prefixes", FormDataType.STRING));
        metaData.addMetaDataItem(new MetaFormDataItem("GitFile", FormDataType.FILE));
        return metaData;
    }

    @Override
    public ResultDto check(InputData inputData) {
        return ResultDto.ok();
    }

    @Override
    public InfoSet work(InputData inputData, Work work) {
        File gitFile = (File) inputData.getFormDataObjs(getMetaData()).get("GitFile").get(0);
        List<Object> prefixesObj = inputData.getFormDataObjs(getMetaData()).get("Prefixes");
        List<String> prefixes = new ArrayList<>();
        for(Object obj:prefixesObj){
            String prefixStr = (String)obj;
            String[] prefixList = prefixStr.split(";");
            prefixes.addAll(Arrays.asList(prefixList));
        }
        String fileName = gitFile.getName().substring(0,gitFile.getName().lastIndexOf("."));
        String outPath = work.getWorkspace().getAbsolutePath();
        System.out.println("解压路径：" + outPath);
        FileCompress.unZip(gitFile.getAbsolutePath(), outPath);
        if(!outPath.endsWith("/"))
            outPath = outPath+"/";
        GitCommitData gitCommitData = new GitCommitData(outPath+fileName+"/",prefixes);

        return new InfoSet(gitCommitData.getInfos());
    }

    @Override
    public String getName() {
        return "sys_GitCommitInfoCollection";
    }

    @Override
    public String getDesc() {
        return "collect git commit data.";
    }
}
