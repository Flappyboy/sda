package cn.edu.nju.software.sda.app.mock.dto;

public class CallDto {
    private String id;
    private Object caller;
    private Object callee;
    private Integer count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getCaller() {
        return caller;
    }

    public void setCaller(Object caller) {
        this.caller = caller;
    }

    public Object getCallee() {
        return callee;
    }

    public void setCallee(Object callee) {
        this.callee = callee;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
