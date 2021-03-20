package vip.gadfly.chakkispring.common.configuration;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gadfly
 * <p>
 * Swagger2配置类
 */
@Slf4j
@EnableSwagger2WebMvc
@Configuration(proxyBeanMethods = false)
@Import(BeanValidatorPluginsConfiguration.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class Swagger2Configuration {

    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    public Swagger2Configuration(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     */
    @Bean()
    public Docket defaultApi2() {
        //添加header参数
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name(HttpHeaders.AUTHORIZATION).description("用户 token，除登录接口及验证码接口以外都需要")
                .modelRef(new ModelRef("string")).parameterType("header")
                //header中的token参数非必填，传空也可以
                .required(false).build();
        pars.add(ticketPar.build());
        log.info("Knife4J 初始化成功");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("vip.gadfly.chakkispring.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .extensions(openApiExtensionResolver.buildSettingExtensions());
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：<a href="http://localhost:5000/api/swagger2/doc.html">http://localhost:5000/api/swagger2/doc.html</a>
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Chakki 作业平台接口文档")
                .description("毕业设计")
                .termsOfServiceUrl("https://chakki.gadfly.vip/")
                .contact(new Contact(
                        "Gadfly",
                        "https://www.gadfly.vip/",
                        "gadfly@gadfly.vip"))
                .version("1.0")
                .build();
    }
}
