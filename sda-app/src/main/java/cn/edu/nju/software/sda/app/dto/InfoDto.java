package cn.edu.nju.software.sda.app.dto;

import cn.edu.nju.software.sda.core.domain.info.Info;
import lombok.Data;

@Data
public class InfoDto extends Info {
    public InfoDto() {
        super("");
    }

    public InfoDto(String id, String name) {
        super(name);
        setId(id);
    }
}
