package cn.edu.nju.software.sda.app.entity;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class PartitionResultEdgeCall implements Serializable {

    @Id
    private String id;

    private String edgeid;

    private String callid;

    private Object call;

    private Integer calltype;

    private Date createdat;

    private Date updatedat;

    private static final long serialVersionUID = 1L;

    public Object getCall() {
        return call;
    }

    public void setCall(Object call) {
        this.call = call;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getEdgeid() {
        return edgeid;
    }

    public void setEdgeid(String edgeid) {
        this.edgeid = edgeid == null ? null : edgeid.trim();
    }

    public String getCallid() {
        return callid;
    }

    public void setCallid(String callid) {
        this.callid = callid == null ? null : callid.trim();
    }

    public Integer getCalltype() {
        return calltype;
    }

    public void setCalltype(Integer calltype) {
        this.calltype = calltype;
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
        sb.append(", edgeid=").append(edgeid);
        sb.append(", callid=").append(callid);
        sb.append(", calltype=").append(calltype);
        sb.append(", createdAt=").append(createdat);
        sb.append(", updatedAt=").append(updatedat);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}