package com.nexusretail.startup.code;

import com.nexusretail.common.constant.Status;

import java.util.List;

public record CodeDefinition(
        String              codeType,
        boolean             systemDefined,
        String              description,
        Status              status,
        List<CodeValueEntry> values
) {}