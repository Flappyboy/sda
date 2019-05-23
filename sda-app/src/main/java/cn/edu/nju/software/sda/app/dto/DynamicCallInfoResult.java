package cn.edu.nju.software.sda.app.dto;

import cn.edu.nju.software.sda.app.entity.PairRelationEntity;

import java.util.List;

public class DynamicCallInfoResult {
    private String status;
    private String msg;
    private Data data;
    private Boolean ok;
    public static class Data{
        private Integer flag;
        private List<PairRelationEntity> list;

        @Override
        public String toString() {
            return "Data{" +
                    "flag=" + flag +
                    ", list=" + list +
                    '}';
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public List<PairRelationEntity> getList() {
            return list;
        }

        public void setList(List<PairRelationEntity> list) {
            this.list = list;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    @Override
    public String toString() {
        return "DynamicCallInfoResult{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", ok=" + ok +
                '}';
    }
}
