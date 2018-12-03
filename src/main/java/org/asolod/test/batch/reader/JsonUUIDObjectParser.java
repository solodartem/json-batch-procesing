package org.asolod.test.batch.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asolod.test.model.GroupObject;
import org.springframework.batch.item.ItemProcessor;

import java.io.File;

/**
 * Simple json parser that reads single {@link GroupObject} from file.
 */
public class JsonUUIDObjectParser implements ItemProcessor<File, GroupObject> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public GroupObject process(File file) throws Exception {
        return mapper.reader().forType(GroupObject.class).readValue(file);
    }
}
