package cn.edu.nju.software.sda.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Base {
    String id;

    Date createdAt;

    Date updatedAt;
}
