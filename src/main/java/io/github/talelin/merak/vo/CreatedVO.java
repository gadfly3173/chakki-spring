package io.github.talelin.merak.vo;

import io.github.talelin.merak.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;

public class CreatedVO extends UnifyResponseVO {

    public CreatedVO(int code) {
        this.setCode(code);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

}
