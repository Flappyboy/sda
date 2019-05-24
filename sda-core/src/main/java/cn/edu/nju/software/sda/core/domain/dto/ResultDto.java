package cn.edu.nju.software.sda.core.domain.dto;

import lombok.Data;

@Data
public class ResultDto {
    Boolean ok = true;

    String msg;

    public static ResultDto ok(){
        return new ResultDto();
    }

    public static ResultDto notOk(){
        ResultDto resultDto = new ResultDto();
        resultDto.setOk(false);
        return resultDto;
    }
}
