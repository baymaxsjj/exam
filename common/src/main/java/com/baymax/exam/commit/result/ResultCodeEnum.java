package com.baymax.exam.commit.result;

public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),

    PHONE_CODE_ERROR(211, "手机验证码错误"),

    MTCLOUD_ERROR(210, "直播接口异常"),

    COUPON_GET(220, "优惠券已经领取"),
    COUPON_LIMIT_GET(221, "优惠券已发放完毕"),

    FILE_UPLOAD_ERROR(21004, "文件上传错误"),
    FILE_DELETE_ERROR(21005, "文件刪除错误"),

    VOD_PALY_ERROR(209, "请购买后观看"),
    COURSES_TO_ADD_FAILURE(20001, "课程添加失败"),

    COURSE_UPDATE_FAIL(20001, "课程更新失败"),
    CHAPTERS_ADD_FAILURE(20001, "章节添加失败"),
    UPDATE_SECTION_FAILURE(20001, "更新章节失败"),
    ADD_FAILURE_SECTION(20001, "小节添加失败"),
    VIDEO_UPLOAD_FAILS(20001, "视频上传失败"),
    DELETE_SECTION_FAILURE(20001, "删除小节失败"),
    DELETE_COURSES_FAIL(20001, "删除课程失败"),
    THERE_IS_NO_VIDEO(20001, "该小节暂时无视频"),
    CHAPTER_DELETE_FAIL(20001, "章节删除失败"),
    COURSE_RELEASE_FAILURE(20001, "课程发布失败");

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
