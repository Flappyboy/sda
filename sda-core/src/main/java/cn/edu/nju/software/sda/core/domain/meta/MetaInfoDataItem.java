package cn.edu.nju.software.sda.core.domain.meta;

public class MetaInfoDataItem extends MetaDataItem {

    public MetaInfoDataItem(String name) {
        super(DataType.InfoData, name, false);
    }

    public MetaInfoDataItem(String name, Boolean required) {
        super(DataType.InfoData, name, required);
    }
}
