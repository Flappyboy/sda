package cn.edu.nju.software.sda.app.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class PartitionInfo implements Serializable {

    @Id
    private String id;

    private String appid;

    private String dynamicanalysisinfoid;

    private String algorithmsid;

    @Column(name = "`desc`")
    private String desc;

    private Integer status;

    private Integer flag;

    private Integer type;

    private Date createdat;

    private Date updatedat;

    private static final long serialVersionUID = 1L;

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

    public String getDynamicanalysisinfoid() {
        return dynamicanalysisinfoid;
    }

    public void setDynamicanalysisinfoid(String dynamicanalysisinfoid) {
        this.dynamicanalysisinfoid = dynamicanalysisinfoid == null ? null : dynamicanalysisinfoid.trim();
    }

    public String getAlgorithmsid() {
        return algorithmsid;
    }

    public void setAlgorithmsid(String algorithmsid) {
        this.algorithmsid = algorithmsid == null ? null : algorithmsid.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", appId=").append(appid);
        sb.append(", dynamicAnalysisInfoId=").append(dynamicanalysisinfoid);
        sb.append(", algorithmsId=").append(algorithmsid);
        sb.append(", desc=").append(desc);
        sb.append(", status=").append(status);
        sb.append(", flag=").append(flag);
        sb.append(", type=").append(type);
        sb.append(", createdAt=").append(createdat);
        sb.append(", updatedAt=").append(updatedat);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}