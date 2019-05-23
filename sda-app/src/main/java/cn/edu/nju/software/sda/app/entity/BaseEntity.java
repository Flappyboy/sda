package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
public abstract class BaseEntity {
    @Id
    private String id;

    private Integer flag;

    private Date createdAt;

    private Date updatedAt;
}
