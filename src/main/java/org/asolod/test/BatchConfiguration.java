package org.asolod.test;


import org.asolod.test.batch.listener.JobCompletionNotificationListener;
import org.asolod.test.batch.tasklet.CleanTempFolderTasklet;
import org.asolod.test.batch.writer.UUIDObjectWriter;
import org.asolod.test.model.GroupObject;
import org.asolod.test.model.UUIDObject;
import org.asolod.test.batch.reader.AbstractObjectsReader;
import org.asolod.test.batch.reader.JsonFilesReader;
import org.asolod.test.batch.reader.JsonUUIDObjectParser;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    public static Path OUTPUT_FILE_DIRECTORY = Paths.get("temp", "json");
    public static int JSON_GENERATOR_COLLECTION_SIZE = 10;
    public static int JSON_GENERATOR_BATCH_SIZE = 2;
    public static int GROUPS_COUNT = 5;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step loadJsonToDB) {
        return jobBuilderFactory.get("generateJson")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(steps.get("cleanTempFolder").tasklet(new CleanTempFolderTasklet()).build())
                .next(generateUUIObjectsJsonStep())
                .next(generateGroupsJsonStep())
                .next(loadJsonToDB)
                .build();
    }

    @Bean
    public AbstractObjectsReader<UUIDObject> UUIObjectsGenerator() {
        return new AbstractObjectsReader<UUIDObject>() {
            @Override
            protected UUIDObject doRead() {
                return UUIDObject.genRandomInstance();
            }
        };
    }

    @Bean
    public AbstractObjectsReader<GroupObject> groupObjectsGenerator() {
        return new AbstractObjectsReader<GroupObject>() {
            @Override
            protected GroupObject doRead() {
                return GroupObject.genRandomInstance();
            }
        };
    }

    @Bean
    public Step generateUUIObjectsJsonStep() {
        return stepBuilderFactory.get("generateUUIObjectsJsonStep")
                .<UUIDObject, UUIDObject>chunk(JSON_GENERATOR_BATCH_SIZE)
                .reader(UUIObjectsGenerator())
                .writer(UUIDObjectWriter())
                .build();
    }

    @Bean
    public Step generateGroupsJsonStep() {
        return stepBuilderFactory.get("generateGroupsJsonStep")
                .<UUIDObject, UUIDObject>chunk(JSON_GENERATOR_BATCH_SIZE)
                .reader(groupObjectsGenerator())
                .writer(UUIDObjectWriter())
                .build();
    }

    @Bean
    public ItemWriter<UUIDObject> UUIDObjectWriter() {
        return new UUIDObjectWriter<>();
    }

    @Bean
    public Step loadJsonToDB(JdbcBatchItemWriter<GroupObject> writer, JobCompletionNotificationListener listener) {
        return stepBuilderFactory.get("loadJsonToDB")
                .<File, GroupObject>chunk(JSON_GENERATOR_BATCH_SIZE)
                .reader(new JsonFilesReader())
                .processor(new JsonUUIDObjectParser())
                .writer(writer)
                .listener(listener)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<GroupObject> dbWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<GroupObject>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO items (uuid, group_id) VALUES (:uuid, :groupId)")
                .dataSource(dataSource)
                .build();
    }

}
