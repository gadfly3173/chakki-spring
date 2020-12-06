package vip.gadfly.chakkispring.vo;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.util.RequestUtil;
import io.github.talelin.merak.common.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;


/**
 * 统一API响应结果封装
 */
@Data
@Builder
@AllArgsConstructor
public class UnifyResponseVO<T> {

    private int code;

    private T message;

    private String request;

    public UnifyResponseVO() {
        this.code = Code.SUCCESS.getCode();
        this.message = (T) Code.SUCCESS.getDescription();
        this.request = RequestUtil.getSimpleRequest();
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.OK.value());
    }

    public UnifyResponseVO(int code) {
        this.code = code;
        this.request = RequestUtil.getSimpleRequest();
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.OK.value());
    }

    public UnifyResponseVO(String message) {
        this.code = Code.SUCCESS.getCode();
        this.message = (T) message;
        this.request = RequestUtil.getSimpleRequest();
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.OK.value());
    }

    public UnifyResponseVO(HttpStatus httpStatus) {
        this.code = Code.SUCCESS.getCode();
        this.message = (T) Code.SUCCESS.getDescription();
        this.request = RequestUtil.getSimpleRequest();
        ResponseUtil.setCurrentResponseHttpStatus(httpStatus.value());
    }

    public UnifyResponseVO(int code, T message) {
        this.code = code;
        this.message =  message;
        this.request = RequestUtil.getSimpleRequest();
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.OK.value());
    }

    public UnifyResponseVO(int code, HttpStatus httpStatus) {
        this.code = code;
        this.message = (T) Code.SUCCESS.getDescription();
        this.request = RequestUtil.getSimpleRequest();
        ResponseUtil.setCurrentResponseHttpStatus(httpStatus.value());
    }

    public UnifyResponseVO(HttpStatus httpStatus, T message) {
        this.code = Code.SUCCESS.getCode();
        this.message = message;
        this.request = RequestUtil.getSimpleRequest();
        ResponseUtil.setCurrentResponseHttpStatus(httpStatus.value());
    }

    public UnifyResponseVO(int code, T message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.request = RequestUtil.getSimpleRequest();
        ResponseUtil.setCurrentResponseHttpStatus(httpStatus.value());
    }



}
