package cn.edu.nju.software.sda.core.domain;

import lombok.Data;

import java.util.List;

@Data
public class PageQueryDto<T> {

    private List<T> result;

    private Integer pageNum;

    private Integer pageSize;

    private Long total;

    public PageQueryDto(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static PageQueryDto create(Integer pageNum, Integer pageSize){
        return new PageQueryDto(pageNum, pageSize);
    }

    public PageQueryDto addResult(List<T> result, Long total){
        this.result = result;
        this.total = total;
        return this;
    }
}
