package com.internship.tool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // Separated from main Application class so @WebMvcTest tests don't fail
    // with "JPA metamodel must not be empty" error
}
