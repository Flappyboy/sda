package cn.edu.nju.software.sda.app.entity.common.github;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Tree {
    private String sha;
    private String url;
}
