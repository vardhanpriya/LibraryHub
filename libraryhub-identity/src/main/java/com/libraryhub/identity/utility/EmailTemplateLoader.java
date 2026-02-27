package com.libraryhub.identity.utility;


import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class EmailTemplateLoader {
    public String loadTemplate(String name) {
        try (InputStream is =
                     getClass().getClassLoader()
                             .getResourceAsStream("templates/" + name)) {

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }
}
