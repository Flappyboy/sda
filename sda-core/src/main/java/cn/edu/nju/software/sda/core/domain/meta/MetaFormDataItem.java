package cn.edu.nju.software.sda.core.domain.meta;

import lombok.Getter;

@Getter
public class MetaFormDataItem extends MetaDataItem {

    private FormDataType type;

    public MetaFormDataItem(String name, FormDataType type) {
        super(DataType.FormData, name);
        this.type = type;
    }

    public MetaFormDataItem(String name, Boolean required, FormDataType type) {
        super(DataType.FormData, name, required);
        this.type = type;
    }
}
