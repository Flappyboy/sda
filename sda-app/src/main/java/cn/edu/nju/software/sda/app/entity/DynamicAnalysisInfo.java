package cn.edu.nju.software.sda.app.entity;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class DynamicAnalysisInfo implements Serializable {

    @Id
    private String id;

    private String appid;

    private String appName;

    private Date starttine;

    private Date endtime;

    private Date createdat;

    private Date updatedat;

    private Integer flag;

    private Integer type;

    private String desc;

    private Integer status;

    private static final long serialVersionUID = 1L;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public Date getStarttine() {
        return starttine;
    }

    public void setStarttine(Date starttine) {
        this.starttine = starttine;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", appId=").append(appid);
        sb.append(", starttine=").append(starttine);
        sb.append(", endtime=").append(endtime);
        sb.append(", createdAt=").append(createdat);
        sb.append(", updatedAt=").append(updatedat);
        sb.append(", flag=").append(flag);
        sb.append(", type=").append(type);
        sb.append(", desc=").append(desc);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}