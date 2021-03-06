package vip.gadfly.chakkispring.common.util;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vip.gadfly.chakkispring.vo.PageResponseVO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * 响应结果生成工具
 */
@SuppressWarnings("unchecked")
@Slf4j
public class ResponseUtil {

    /**
     * 获得当前响应
     *
     * @return 响应
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static void setCurrentResponseHttpStatus(int httpStatus) {
        getResponse().setStatus(httpStatus);
    }

    public static <T> UnifyResponseVO<T> generateCreatedResponse(int code) {
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .message(Code.CREATED.getDescription())
                .code(code)
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateCreatedResponse(int code, T data) {
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .message(data)
                .code(code)
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateDeletedResponse(int code) {
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .message(Code.SUCCESS.getDescription())
                .code(code)
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateDeletedResponse(int code, T data) {
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .message(data)
                .code(code)
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateUpdatedResponse(int code) {
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .message(Code.SUCCESS.getDescription())
                .code(code)
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateUpdatedResponse(int code, T data) {
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .message(data)
                .code(code)
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateUnifyResponse(int code) {
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .code(code)
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static PageResponseVO generatePageResult(int total, List items, int page, int count) {
        return new PageResponseVO(total, items, page, count);
    }

    public static ResponseEntity<FileSystemResource> generateFileResponse(File file, String filename) {
        if (!file.canRead()) {
            throw new NotFoundException(10020);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noStore().mustRevalidate());
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build());
        headers.setPragma("no-cache");
        headers.setExpires(0);
        headers.setLastModified(System.currentTimeMillis());
        headers.setETag("\"" + System.currentTimeMillis() + "\"");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file));
    }
}
