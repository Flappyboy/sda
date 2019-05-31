package cn.edu.nju.software.sda.core.domain.meta;

import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public enum FormDataType {
    FILE(){
        @Override
        public Object getObj(String value) {
            try {
                return new File(URLDecoder.decode(WorkspaceUtil.absolutePath(value),"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
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
