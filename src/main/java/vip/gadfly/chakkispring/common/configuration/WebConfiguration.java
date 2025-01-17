package vip.gadfly.chakkispring.common.configuration;

import io.github.talelin.autoconfigure.interceptor.AuthorizeInterceptor;
import io.github.talelin.autoconfigure.interceptor.LogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.gadfly.chakkispring.common.interceptor.ClassVerifyInterceptor;
import vip.gadfly.chakkispring.common.interceptor.RequestLogInterceptor;
import vip.gadfly.chakkispring.extension.limit.LimitInterceptor;
import vip.gadfly.chakkispring.module.file.FileUtil;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;


/**
 * Spring MVC 配置
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${auth.enabled:false}")
    private boolean authEnabled;

    @Value("${request-log.enabled:false}")
    private boolean requestLogEnabled;

    @Value("${qps-limit.enabled:false}")
    private boolean qpsLimitEnabled;

    @Autowired
    private AuthorizeInterceptor authorizeInterceptor;

    @Autowired
    private ClassVerifyInterceptor classVerifyInterceptor;

    @Autowired
    private LogInterceptor logInterceptor;

    @Autowired
    private RequestLogInterceptor requestLogInterceptor;

    @Autowired
    private LimitInterceptor limitInterceptor;

    @Value("${lin.cms.file.store-dir:assets/}")
    private String dir;

    @Value("${lin.cms.file.serve-path:assets/**}")
    private String servePath;

    /**
     * 跨域
     * 注意： 跨域问题涉及安全性问题，这里提供的是最方便简单的配置，任何host和任何方法都可跨域
     * 但在实际场景中，这样做，无疑很危险，所以谨慎选择开启或者关闭
     * 如果切实需要，请咨询相关安全人员或者专家进行配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:18080", "https://chakki.gadfly.vip")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.CONTENT_DISPOSITION);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // QPS拦截器
        if (qpsLimitEnabled) {
            registry.addInterceptor(limitInterceptor)
                    .excludePathPatterns("doc.html", "/webjars/**");
        }
        if (authEnabled) {
            //开发环境忽略签名认证
            registry.addInterceptor(authorizeInterceptor)
                    .excludePathPatterns(getDirServePath());
            registry.addInterceptor(classVerifyInterceptor)
                    .excludePathPatterns(getDirServePath());
        }
        if (requestLogEnabled) {
            registry.addInterceptor(requestLogInterceptor);
        }
        registry.addInterceptor(logInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决 knife4j SWAGGER 404报错
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    @Qualifier(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new GadflyDispatcherServlet();
    }

    /**
     * cookie 读取bug
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return tomcatServletWebServerFactory ->
                tomcatServletWebServerFactory.addContextCustomizers(context ->
                        context.setCookieProcessor(new LegacyCookieProcessor()));
    }

    /**
     * request parameter 转 java bean 时 snake_case 转 camelCase
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CustomServletModelAttributeMethodProcessor(true));
    }

    private String getDirServePath() {
        // assets/**
        // assets/
        // /usr/local/assets/
        // assets
        return servePath;
    }

    /**
     * 获得文件夹的绝对路径
     */
    private String getAbsDir() {
        if (FileUtil.isAbsolute(dir)) {
            return dir;
        }
        String cmd = System.getProperty("user.dir");
        Path path = FileSystems.getDefault().getPath(cmd, dir);
        return path.toAbsolutePath().toString();
    }
}
