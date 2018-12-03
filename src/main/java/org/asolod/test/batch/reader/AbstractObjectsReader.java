package org.asolod.test.batch.reader;

import org.asolod.test.BatchConfiguration;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.util.ClassUtils;

/**
 * Generates collection of objects of type generic T
 * Collection size determines by {@link BatchConfiguration#JSON_GENERATOR_COLLECTION_SIZE}
 *
 * @param <T>
 */
public abstract class AbstractObjectsReader<T> extends AbstractItemCountingItemStreamItemReader<T> {

    public AbstractObjectsReader() {
        setMaxItemCount(BatchConfiguration.JSON_GENERATOR_COLLECTION_SIZE);
        this.setExecutionContextName(ClassUtils.getShortName(this.getClass()));
    }

    @Override
    protected void doOpen() throws Exception {

    }

    @Override
    protected void doClose() throws Exception {

    }

}
