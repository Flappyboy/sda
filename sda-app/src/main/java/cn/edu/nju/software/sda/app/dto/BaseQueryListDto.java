package cn.edu.nju.software.sda.app.dto;

import lombok.Data;

@Data
public class BaseQueryListDto {

    Integer page = 1;

    Integer pageSize = 10;
}
