package cn.edu.nju.software.sda.core.domain.partition;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Partition {
    public static final String INFO_NAME_PARTITION = "SYS_PARTITION";

    private String id;

    private String name;

    private Set<PartitionNode> partitionNodeSet;
}
