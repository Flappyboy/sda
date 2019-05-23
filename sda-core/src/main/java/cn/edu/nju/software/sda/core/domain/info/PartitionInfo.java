package cn.edu.nju.software.sda.core.domain.info;

import cn.edu.nju.software.sda.core.domain.partition.Partition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PartitionInfo extends Info {
    private Partition partition;

    public PartitionInfo(Partition partition) {
        super(Partition.INFO_NAME_PARTITION);
        this.partition = partition;
    }
}
