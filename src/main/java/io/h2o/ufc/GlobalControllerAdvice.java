package io.h2o.ufc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    //    @Autowired
//    private ServletWebServerApplicationContext context;
    @Autowired
    private Environment environment;

    @ModelAttribute("url")
    public String getUrl() {
//        return "http://localhost:"+context.getWebServer().getPort();
        return "http://localhost:" + environment.getProperty("local.server.port");
    }
}