package io.github.talelin.merak.vo;

import io.github.talelin.merak.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;

public class UpdatedVO extends UnifyResponseVO {

    public UpdatedVO(int code) {
        this.setCode(code);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }
}
