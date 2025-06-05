package com.core.drm.base.batch.constant;

import lombok.Getter;

@Getter
public enum CronExpressionConst {

    EVERY_5_MINUTES("0 0/5 * 1/1 * ? *"),
    DAILY_1_AM("0 0 0,1 ? * * *")
    ;

    private final String expression;

    CronExpressionConst(final String expression) {
        this.expression = expression;
    }

}
