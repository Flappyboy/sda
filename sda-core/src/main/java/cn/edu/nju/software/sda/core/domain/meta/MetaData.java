package cn.edu.nju.software.sda.core.domain.meta;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MetaData {

    List<MetaDataItem> metaDataItemList = new ArrayList<>();

    public MetaData addMetaDataItem(MetaDataItem metaDataItem){
        metaDataItemList.add(metaDataItem);
        return this;
    }

    public FormDataType getFormDataTypeByName(String name){
        for (MetaDataItem metaDataItem:
                metaDataItemList) {
            if(metaDataItem.getDataType().equals(DataType.FormData) && metaDataItem.getName().equals(name)){
                MetaFormDataItem item = (MetaFormDataItem) metaDataItem;
                return item.getType();
            }
        }
        return null;
    }
}
