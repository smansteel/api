package com.iseplife.api.conf;

import com.iseplife.api.services.fileHandler.AmazonHandler;
import com.iseplife.api.services.fileHandler.CloudinaryHandler;
import com.iseplife.api.services.fileHandler.FileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Created by Guillaume on 18/08/2017.
 * back
 */
@Configuration
public class StorageConfig extends WebMvcConfigurerAdapter {

  @Bean("FileHandlerBean")
  @ConditionalOnProperty(
    name = "cloud_handler.name",
    havingValue = "cloudinary")
  public FileHandler setCloudinaryAsHandler() {
    return new CloudinaryHandler();
  }

  @Bean("FileHandlerBean")
  @ConditionalOnProperty(
    name = "cloud_handler.name",
    havingValue = "aws")
  public FileHandler setAWSAsHandler() {
    return new AmazonHandler();
  }

}
