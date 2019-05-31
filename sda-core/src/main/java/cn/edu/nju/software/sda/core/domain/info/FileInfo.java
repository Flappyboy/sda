package cn.edu.nju.software.sda.core.domain.info;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class FileInfo extends Info {

    private File file;

    public FileInfo(String name) {
        super(name);
    }
}
