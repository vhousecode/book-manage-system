package com.book.common.result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Paginated Response
 *
 * @param <T> data type
 */
@Data
@Schema(description = "Paginated Response")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Total count")
    private Long total;

    @Schema(description = "Current page number")
    private Long pageNum;

    @Schema(description = "Page size")
    private Long pageSize;

    @Schema(description = "Total pages")
    private Long pages;

    @Schema(description = "Data list")
    private List<T> list;

    public PageResult() {
    }

    public PageResult(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public static <T> PageResult<T> of(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setPages(page.getPages());
        result.setList(page.getRecords());
        return result;
    }

    public static <T> PageResult<T> of(Long total, List<T> list) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setList(list);
        return result;
    }
}
