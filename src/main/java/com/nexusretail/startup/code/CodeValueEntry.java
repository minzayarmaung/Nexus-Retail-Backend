package com.nexusretail.startup.code;

public record CodeValueEntry(
        String value,
        String displayName,
        int    orderPosition,
        boolean active
) {}