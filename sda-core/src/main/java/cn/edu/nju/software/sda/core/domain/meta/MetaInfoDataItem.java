package cn.edu.nju.software.sda.core.domain.meta;

public class MetaInfoDataItem extends MetaDataItem {

    private String type;

    public MetaInfoDataItem(String name) {
        this(name, name);
    }

    public MetaInfoDataItem(String name, String type) {
        super(DataType.InfoData, name, false);
        this.type = type;
    }

    public MetaInfoDataItem(String name, Boolean required) {
        super(DataType.InfoData, name, required);
    }

    public String getType() {
        return type;
    }
}
