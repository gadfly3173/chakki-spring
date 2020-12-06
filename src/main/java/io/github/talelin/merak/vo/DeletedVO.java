package io.github.talelin.merak.vo;

import io.github.talelin.merak.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;

public class DeletedVO extends UnifyResponseVO {

    public DeletedVO(int code) {
        this.setCode(code);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }
}
