package com.aiwsport.core;

import org.apache.commons.httpclient.HttpStatus;

public class ConfigServerExceptionFactor {

    public static final ConfigServerExceptionFactor DEFAULT = new ConfigServerExceptionFactor(
            HttpStatus.SC_INTERNAL_SERVER_ERROR, 10001,
            "system error", "系统错误");
    public static final ConfigServerExceptionFactor INTERNAL_ERROR = new ConfigServerExceptionFactor(
            HttpStatus.SC_INTERNAL_SERVER_ERROR, 10002,
            "internal server error", "内部错误");
    public static final ConfigServerExceptionFactor BIND_ERROR = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 10003,
            "bind param error", "系统参数绑定异常");
    public static final ConfigServerExceptionFactor PATTERN_ERROR = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 10004,
            "pattern error", "数据格式异常");

    public static final ConfigServerExceptionFactor PUSH_CONN_INTERRUPT = new ConfigServerExceptionFactor(
            HttpStatus.SC_BAD_REQUEST, 20001,
            "connection is interrupted", "客户端连接中断");
    public static final ConfigServerExceptionFactor PROTOCOL_ERROR = new ConfigServerExceptionFactor(
            HttpStatus.SC_BAD_REQUEST, 20002,
            "unsupport protocol", "不支持的协议");
    public static final ConfigServerExceptionFactor MISSING_PARAM = new ConfigServerExceptionFactor(
            HttpStatus.SC_BAD_REQUEST, 20003,
            "missing param", "缺少参数");
    public static final ConfigServerExceptionFactor CONFIG_PARAM_TYPE_MISMATCH = new ConfigServerExceptionFactor(
            HttpStatus.SC_BAD_REQUEST, 20010,
            "param type mismatch", "参数类型错误");
    public static final ConfigServerExceptionFactor IS_EXIST_IP_SET_NAME = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40001,
            "ipSetName is exist", "ipSet名称已经存在");
    public static final ConfigServerExceptionFactor IS_EXIST_BACKEND_NAME = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40002,
            "backendName is exist", "backendName名称已经存在");
    public static final ConfigServerExceptionFactor IS_EXIST_FILTER_NAME = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40003,
            "filterName is exist", "filterName名称已经存在");
    public static final ConfigServerExceptionFactor IS_EXIST_RULE_MATCHER = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40004,
            "rule matcher is exist", "rule的matcher已经存在");
    public static final ConfigServerExceptionFactor LACK_FILTER_SORT = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40005,
            "lack filter sort", "global类型filter的sort必填");
    public static final ConfigServerExceptionFactor IP_NOT_VISIT_SERVER = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40006,
            "Not allowed to access", "IP不允许访问服务");
    public static final ConfigServerExceptionFactor DATA_IS_NOT_EXIST = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40007,
            "data is not exist", "数据不存在请检查");
    public static final ConfigServerExceptionFactor NAME_NOT_MODIFY = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40008,
            "name can not modify", "名称不能修改");
    public static final ConfigServerExceptionFactor BACKEND_NOT_EXIST = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40009,
            "backend is not exist", "backend不存在");

    public static final ConfigServerExceptionFactor FILTER_NOT_EXIST = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40010,
            "filter is not exist", "filter不存在");

    public static final ConfigServerExceptionFactor ROUTER_NOT_EXIST = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40011,
            "router is not exist", "router不存在");

    public static final ConfigServerExceptionFactor RULE_NOT_EXIST = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40012,
            "rule is not exist", "rule不存在");
    public static final ConfigServerExceptionFactor IPSET_NOT_EXIST = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40013,
            "ipset is not exist", "ipset不存在");
    public static final ConfigServerExceptionFactor RELATION_RULE_NOT_MODIFY = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40014,
            "relation rule not modify", "关联的rule不能修改");
    public static final ConfigServerExceptionFactor BACKEND_NOT_QUOTE_SELF = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40015,
            "backend nit quote self", "backend不用引用本身");
    public static final ConfigServerExceptionFactor SNAPSHOT_NOT_ROLLBACK = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 40016,
            "snapshot is not rollback", "snapshot版本不能回滚");

    public static final ConfigServerExceptionFactor CONFIG_ERROR = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 50000,
            "param config error", "参数校验配置出错");
    public static final ConfigServerExceptionFactor PARAM_VERIFY_FAIL = new ConfigServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 50001,
            "param verify is fail", "参数校验失败");

    private int httpStatus;
    private int errorCode;
    private String errorMsg;
    private String errorMsgCN;

    public ConfigServerExceptionFactor(int httpStatus, int errorCode, String errorMsg, String errorMsgCN) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.errorMsgCN = errorMsgCN;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsgCN() {
        return errorMsgCN;
    }

    public void setErrorMsgCN(String errorMsgCN) {
        this.errorMsgCN = errorMsgCN;
    }
}
