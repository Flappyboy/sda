package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.app.plugin.Generate;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class App implements Serializable {

    @Id
    private String id;

    private String name;

    private String path;

    private Date createdat;

    private Date updatedat;

    private Integer nodenumber;

    private Integer flag;

    private String desc;

    private Integer classcount;

    private Integer interfacecount;

    private Integer functioncount;

    private Integer interfacefunctioncount;

    private Integer status;

    private Integer pinpointPluginStatus;

    private static final long serialVersionUID = 1L;

    public Integer getPinpointPluginStatus() {
        if(pinpointPluginStatus!=null) {
            return pinpointPluginStatus;
        }else if(name != null && id!=null) {
            Generate generate = new Generate(name, id);
            if (generate.getJar()!=null && generate.getJar().exists()) {
                return 1;
            }
        }
        return 0;
    }

    public void setPinpointPluginStatus(Integer pinpointPluginStatus) {
        this.pinpointPluginStatus = pinpointPluginStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
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

    public Integer getNodenumber() {
        return nodenumber;
    }

    public void setNodenumber(Integer nodenumber) {
        this.nodenumber = nodenumber;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public Integer getClasscount() {
        return classcount;
    }

    public void setClasscount(Integer classcount) {
        this.classcount = classcount;
    }

    public Integer getInterfacecount() {
        return interfacecount;
    }

    public void setInterfacecount(Integer interfacecount) {
        this.interfacecount = interfacecount;
    }

    public Integer getFunctioncount() {
        return functioncount;
    }

    public void setFunctioncount(Integer functioncount) {
        this.functioncount = functioncount;
    }

    public Integer getInterfacefunctioncount() {
        return interfacefunctioncount;
    }

    public void setInterfacefunctioncount(Integer interfacefunctioncount) {
        this.interfacefunctioncount = interfacefunctioncount;
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
        sb.append(", name=").append(name);
        sb.append(", path=").append(path);
        sb.append(", createdAt=").append(createdat);
        sb.append(", updatedAt=").append(updatedat);
        sb.append(", nodenumber=").append(nodenumber);
        sb.append(", flag=").append(flag);
        sb.append(", desc=").append(desc);
        sb.append(", classcount=").append(classcount);
        sb.append(", interfacecount=").append(interfacecount);
        sb.append(", functioncount=").append(functioncount);
        sb.append(", interfacefunctioncount=").append(interfacefunctioncount);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}