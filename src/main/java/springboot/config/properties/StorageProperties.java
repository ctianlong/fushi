package springboot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
//@PropertySource("classpath:storage.properties")
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "uploadDir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
