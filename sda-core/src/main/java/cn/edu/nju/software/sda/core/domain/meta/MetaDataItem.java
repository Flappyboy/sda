package cn.edu.nju.software.sda.core.domain.meta;

import lombok.Getter;

@Getter
public abstract class MetaDataItem {

    private DataType dataType;

    private String name;

    private Boolean required;

    private String desc;

    public MetaDataItem(DataType dataType, String name) {
        this(dataType, name, true);
    }

    public MetaDataItem(DataType dataType, String name, Boolean required) {
        this.dataType = dataType;
        this.name = name;
        this.required = required;
    }
}
