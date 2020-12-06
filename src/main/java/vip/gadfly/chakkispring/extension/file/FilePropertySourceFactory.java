package vip.gadfly.chakkispring.extension.file;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Gadfly
 */

@Configuration
public class FilePropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource)
            throws IOException {
        String fileSuffix = "";
        try {
            String filename = resource.getResource().getFilename();
            fileSuffix = filename.substring(filename.lastIndexOf("."));
            resource.getResource().getFile();
            return new ResourcePropertySource(name, resource);
        } catch (Exception e) {
            InputStream inputStream = FilePropertySourceFactory.class.getClassLoader()
                    .getResourceAsStream(name + fileSuffix);
            //转成resource
            InputStreamResource inResource = new InputStreamResource(inputStream);
            return new ResourcePropertySource(new EncodedResource(inResource));
        }
    }
}
