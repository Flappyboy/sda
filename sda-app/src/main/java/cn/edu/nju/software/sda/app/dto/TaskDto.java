package cn.edu.nju.software.sda.app.dto;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskDto {

    String appId;

    String functionName;

    InputData inputData;
}
