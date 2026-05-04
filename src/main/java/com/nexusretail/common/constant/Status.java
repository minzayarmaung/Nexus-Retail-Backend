package com.nexusretail.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status implements BaseEnum<Integer>{
    ACTIVE(1),
    INACTIVE(0);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
