package cn.edu.nju.software.sda.app.entity;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class AlgorithmsParam implements Serializable {

    @Id
    private String id;

    private String algorithmsid;

    private String param;

    private Integer order;

    private Date createdat;

    private Date updatedat;

    private Integer flag;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAlgorithmsid() {
        return algorithmsid;
    }

    public void setAlgorithmsid(String algorithmsid) {
        this.algorithmsid = algorithmsid == null ? null : algorithmsid.trim();
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param == null ? null : param.trim();
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", algorithmsId=").append(algorithmsid);
        sb.append(", param=").append(param);
        sb.append(", order=").append(order);
        sb.append(", createdAt=").append(createdat);
        sb.append(", updatedAt=").append(updatedat);
        sb.append(", flag=").append(flag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}