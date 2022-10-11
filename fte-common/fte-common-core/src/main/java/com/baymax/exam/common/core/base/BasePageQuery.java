package com.baymax.exam.common.core.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author huawei
 * @desc 基础分页请求对象
 * @email huawei_code@163.com
 * @date 2021/2/28
 */
@Data
@Schema
public class BasePageQuery {

    @Schema( name= "页码", example = "1")
    private int pageNum = 1;

    @Schema(name = "每页记录数", example = "10")
    private int pageSize = 10;
}
