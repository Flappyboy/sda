package cn.edu.nju.software.sda.core.domain.meta;

import java.io.File;

public enum FormDataType {
    FILE(){
        @Override
        public Object getObj(String value) {
            return new File(value);
        }
    },
    STRING() {
        @Override
        public Object getObj(String value) {
            return value;
        }
    },
    TIMESTAMP() {
        @Override
        public Object getObj(String value) {
            return Long.valueOf(value);
        }
    },
    ;

    FormDataType() {}

    public abstract Object getObj(String value);
}
