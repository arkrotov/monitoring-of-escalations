package ru.eldorado;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "properties")
public class Properties {

    private List<String> emails;
    private String query;
    private String period;
    private Integer limit;
    private String subject;
    private String message;
    private String sender;
    private String consoleMessage;

}


