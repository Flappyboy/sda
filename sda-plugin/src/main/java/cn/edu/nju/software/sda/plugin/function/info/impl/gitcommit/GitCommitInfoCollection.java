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
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;

public class GitCommitInfoCollection extends InfoCollection {

    @Override
    public MetaData getMetaData() {
        MetaData metaData = new MetaData();
        metaData.addMetaDataItem(new MetaFormDataItem("String", FormDataType.STRING));
        metaData.addMetaDataItem(new MetaFormDataItem("Time", FormDataType.TIMESTAMP));
        metaData.addMetaDataItem(new MetaFormDataItem("File", FormDataType.FILE));
        metaData.addMetaDataItem(new MetaInfoDataItem(Node.INFO_NAME_NODE));
        metaData.addMetaDataItem(new MetaInfoDataItem(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT));
        return metaData;
    }

    @Override
    public ResultDto check(InputData inputData) {
        return ResultDto.ok();
    }

    @Override
    public InfoSet work(InputData inputData, Work work) {
        return new InfoSet();
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
