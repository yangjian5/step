package com.aiwsport.core;

/**
 * xin.pang
 */
public class ConfigServerException extends RuntimeException {
    private ConfigServerExceptionFactor factor;

    public ConfigServerException(ConfigServerExceptionFactor factor) {
        this(factor, factor.getErrorMsg());
    }

    public ConfigServerException(ConfigServerExceptionFactor factor, String message) {
        super((message == null ? factor.getErrorMsg() : message));
        this.factor = factor;
    }

    public ConfigServerException(Exception e) {
        this(ConfigServerExceptionFactor.DEFAULT, e.getMessage());
    }

    public ConfigServerExceptionFactor getFactor() {
        return factor;
    }
}
