package com.baymax.exam;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Map;

/**
 * @author ：Baymax
 * @date ：Created in 2023/2/2 14:38
 * @description：vue 路由参数
 * @modified By：
 * @version:
 */
@Data
public class VueRouteLocationRaw {
    String name;
    String path;
    Map<String,Object> params;
    Map<String,Object> query;
    @JsonIgnore
    public String getJson(){
        return JSONUtil.toJsonStr(this);
    }
}
