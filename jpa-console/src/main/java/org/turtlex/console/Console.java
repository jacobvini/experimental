package org.turtlex.console;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.turtlex.services.EnvironmentService;

/**
 * Created by coolkids on 7/10/2016.
 */
@Slf4j
public class Console {
    public static void main(String[] args) {
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext("org.turtlex");
            //context.getBean(EnvironmentService.class).dump();
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }
}
