package com.baymax.exam.common.core.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结构体
 *
 * @author haoxr
 * @date 2022/2/18 23:29
 */
@Data
public class PageResult<T> implements Serializable {
    private List<T> list;
    private long total;
    private long pages;
    private long current;

    /**
     * 设置结果
     *
     * @param page 页面
     * @return {@link PageResult}<{@link T}>
     */
    public static <T> PageResult<T> setResult(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setList(page.getRecords());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setCurrent(page.getCurrent());
        return result;
    }

}
