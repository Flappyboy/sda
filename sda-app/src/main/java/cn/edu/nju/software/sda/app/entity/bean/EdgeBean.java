package cn.edu.nju.software.sda.app.entity.bean;

public class EdgeBean {
    private String sourceId;
    private String targetId;
    private int sourceKey;
    private int targetKey;
    private int weight;

    public String getSourceId() {
        return sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public int getSourceKey() {
        return sourceKey;
    }

    public int getTargetKey() {
        return targetKey;
    }

    public int getWeight() {
        return weight;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setSourceKey(int sourceKey) {
        this.sourceKey = sourceKey;
    }

    public void setTargetKey(int targetKey) {
        this.targetKey = targetKey;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "EdgeBean{" +
                "sourceId='" + sourceId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", sourceKey=" + sourceKey +
                ", targetKey=" + targetKey +
                ", weight=" + weight +
                '}';
    }
}
