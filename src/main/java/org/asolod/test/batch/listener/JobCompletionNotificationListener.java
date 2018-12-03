package org.asolod.test.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private String getSQL(boolean missedGroups) {
        return "SELECT DISTINCT group_id FROM items WHERE group_id IS NOT NULL AND uuid " + (missedGroups ? "NOT" : "") + "  IN (SELECT uuid FROM items WHERE group_id IS NULL)";
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job finished. Check results summary");
            List<Integer> missedGroups = jdbcTemplate.queryForList(getSQL(true), Integer.class);
            List<Integer> existingGroups = jdbcTemplate.queryForList(getSQL(false), Integer.class);
            System.out.println("Missed groups: =>" + missedGroups);
            System.out.println("Existing groups: =>" + existingGroups);
            if (!missedGroups.removeAll(existingGroups)) {
                System.out.println("Lists intersections is empty");
            } else {
                throw new RuntimeException("Lists intersections is NOT empty");
            }

        }
    }
}
