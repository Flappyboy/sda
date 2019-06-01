package cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FileInfo {
    private String fileName;
    private int count;
}
