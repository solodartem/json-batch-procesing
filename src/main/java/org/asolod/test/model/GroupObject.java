package org.asolod.test.model;

import org.asolod.test.BatchConfiguration;
import org.asolod.test.utils.RandomUtils;

import java.util.UUID;

public class GroupObject extends UUIDObject {

    private Integer groupId;

    public static GroupObject genRandomInstance() {
        GroupObject groupObject = new GroupObject();
        groupObject.groupId = RandomUtils.generatedIntFromRange(1, BatchConfiguration.GROUPS_COUNT + 1);
        if (groupObject.groupId % 2 == 0) {
            groupObject.uuid = UUID.randomUUID().toString();
        } else {
            groupObject.uuid = UUIDObject.EXISTING_UUIDS.get(groupObject.groupId);
        }
        return groupObject;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "GroupObject{" +
                "groupId=" + groupId +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
