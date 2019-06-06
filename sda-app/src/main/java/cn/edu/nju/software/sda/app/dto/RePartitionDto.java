package cn.edu.nju.software.sda.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class RePartitionDto {
    String partitionInfoId;
    List<String> relationInfoIds;
}
