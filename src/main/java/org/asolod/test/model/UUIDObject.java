package org.asolod.test.model;

import org.asolod.test.BatchConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UUIDObject {

    public static final List<String> EXISTING_UUIDS = new ArrayList<>(BatchConfiguration.GROUPS_COUNT);
    protected String uuid;

    public static UUIDObject genRandomInstance() {
        UUIDObject uuidObject = new UUIDObject();
        String uuid = UUID.randomUUID().toString();
        uuidObject.uuid = uuid;
        EXISTING_UUIDS.add(uuid);
        return uuidObject;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "UUIDObject{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}

