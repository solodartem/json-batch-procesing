package org.asolod.test.batch.writer;

import org.asolod.test.model.GroupObject;
import org.asolod.test.model.UUIDObject;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.asolod.test.BatchConfiguration.OUTPUT_FILE_DIRECTORY;

/**
 * Serialize each item collection of {@link UUIDObject} to separate file in temp directory {@link org.asolod.test.BatchConfiguration#OUTPUT_FILE_DIRECTORY}
 *
 * @param <T>
 */

public class UUIDObjectWriter<T extends UUIDObject> implements ItemWriter<T> {

    public static String JSON_GROUP_PREFIX = "group_";
    public static String JSON_UUI_PREFIX = "uii_";

    private JacksonJsonObjectMarshaller marshaller = new JacksonJsonObjectMarshaller();

    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            String filePrefix = item instanceof GroupObject ? JSON_GROUP_PREFIX : JSON_UUI_PREFIX;
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(OUTPUT_FILE_DIRECTORY.toString(), filePrefix + item.getUuid() + ".json"))) {
                writer.write(marshaller.marshal(item));
            }
        }
    }
}
