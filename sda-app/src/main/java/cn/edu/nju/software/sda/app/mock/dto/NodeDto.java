package cn.edu.nju.software.sda.app.mock.dto;

import lombok.Data;

@Data
public class NodeDto {
    private String name;
    private String id;
    private String desc;
    private Integer size;

    public void setDesc(String desc) {
        if(desc == null){
            desc = "";
        }
        this.desc = desc;
    }
}
