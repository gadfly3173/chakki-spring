package vip.gadfly.chakkispring.common.configuration;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author Gadfly
 * <p>
 * Swagger2配置类
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration(proxyBeanMethods = false)
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
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
    @Bean("defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("vip.gadfly.chakkispring.controller"))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildSettingExtensions());
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://localhost:5000/api/doc.html
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Chakki 作业平台接口文档")
                .description("毕业设计")
                .termsOfServiceUrl("https://chakki.gadfly.vip/")
                .contact(new Contact("Gadfly", "https://www.gadfly.vip/", "gadfly@gadfly.vip"))
                .version("1.0")
                .build();
    }
}
