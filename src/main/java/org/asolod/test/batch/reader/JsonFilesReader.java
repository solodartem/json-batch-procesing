package org.asolod.test.batch.reader;

import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Scans all files in {@link org.asolod.test.BatchConfiguration#OUTPUT_FILE_DIRECTORY} folder
 * To apply filtering of file names do override method {@link JsonFilesReader@doOpen}
 */
import static org.asolod.test.BatchConfiguration.OUTPUT_FILE_DIRECTORY;

public class JsonFilesReader extends AbstractItemCountingItemStreamItemReader<File> {

    private Iterator<File> filesIterator;

    public JsonFilesReader() {
        setName(JsonFilesReader.class.getName());
    }

    @Override
    protected File doRead() {
        if (filesIterator.hasNext()) {
            return filesIterator.next();
        }
        return null;
    }

    @Override
    protected void doOpen() {
        filesIterator = Arrays.stream(OUTPUT_FILE_DIRECTORY.toFile().listFiles()).iterator();
    }

    @Override
    protected void doClose() {

    }
}
