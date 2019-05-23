package cn.edu.nju.software.sda.core.domain.meta;

import java.io.File;

public enum FormDataType {
    FILE("FILE"){
        @Override
        public Object getObj(String value) {
            return new File(value);
        }
    },
    STRING("STRING") {
        @Override
        public Object getObj(String value) {
            return value;
        }
    },
    TIMESTAMP("TIMESTAMP") {
        @Override
        public Object getObj(String value) {
            return Long.valueOf(value);
        }
    },
    ;

    private String name;

    FormDataType(String name) {
        this.name = name;
    }

    public abstract Object getObj(String value);

    public String getName() {
        return name;
    }
}
