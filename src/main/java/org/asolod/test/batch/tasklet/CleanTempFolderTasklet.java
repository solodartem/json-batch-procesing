package org.asolod.test.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;

import static org.asolod.test.BatchConfiguration.OUTPUT_FILE_DIRECTORY;

/**
 * Task removes temp folder and creates new one to clean json data.
 */
public class CleanTempFolderTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        FileSystemUtils.deleteRecursively(OUTPUT_FILE_DIRECTORY);
        Files.createDirectories(OUTPUT_FILE_DIRECTORY);
        return RepeatStatus.FINISHED;
    }
}
