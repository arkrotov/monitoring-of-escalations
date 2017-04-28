package ru.eldorado;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Log4j
@Component
public class BasicMailSender {

    private final JavaMailSender mailSender;
    private final Properties properties;
    private final JdbcTemplate jdbcTemplate;
    private final String SQL_QUERY;

    @Autowired
    public BasicMailSender(JavaMailSender mailSender, Properties properties, JdbcTemplate jdbcTemplate) {
        this.mailSender = mailSender;
        this.properties = properties;
        this.jdbcTemplate = jdbcTemplate;
        this.SQL_QUERY = properties.getQuery();
    }

    @Scheduled(cron = "#{environment['properties.period']}")
    void process() throws InterruptedException {

        Integer count = getCount();

        System.out.println(LocalDateTime.now() + " " + properties.getConsoleMessage() + getCount());

        if (count > properties.getLimit()) {
            sendMessage(count);
        }

    }

    private Integer getCount() {
        return jdbcTemplate.queryForObject(SQL_QUERY, Integer.class);
    }

    private void sendMessage(int count) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(properties.getSender());
            List<String> emails = properties.getEmails();
            message.setTo(emails.toArray(new String[emails.size()]));
            message.setSubject(properties.getSubject());
            message.setText(String.format(properties.getMessage(),
                    count,
                    SQL_QUERY,
                    LocalDateTime.now()));
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e.getStackTrace());
        }

    }



}


