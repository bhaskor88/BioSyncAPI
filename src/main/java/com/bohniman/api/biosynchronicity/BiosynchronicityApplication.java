package com.bohniman.api.biosynchronicity;

import com.bohniman.api.biosynchronicity.util.CryptoProperties;
import com.bohniman.api.biosynchronicity.util.FileStorageProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class, CryptoProperties.class })
public class BiosynchronicityApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BiosynchronicityApplication.class, args);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    // @Bean
    // public ServletWebServerFactory servletContainer() {
    // TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
    // @Override
    // protected void postProcessContext(Context context) {
    // SecurityConstraint securityConstraint = new SecurityConstraint();
    // securityConstraint.setUserConstraint("CONFIDENTIAL");
    // SecurityCollection collection = new SecurityCollection();
    // collection.addPattern("/*");
    // securityConstraint.addCollection(collection);
    // context.addConstraint(securityConstraint);
    // }
    // };

    // tomcat.addAdditionalTomcatConnectors(redirectConnector());
    // return tomcat;
    // }

    // private Connector redirectConnector() {
    // Connector connector = new
    // Connector("org.apache.coyote.http11.Http11NioProtocol");
    // connector.setScheme("http");
    // connector.setPort(80);
    // connector.setSecure(false);
    // connector.setRedirectPort(443);
    // return connector;
    // }
}
