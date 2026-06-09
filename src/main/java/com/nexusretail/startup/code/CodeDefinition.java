package com.nexusretail.startup.code;

import java.util.List;

public record CodeDefinition(
        String              codeType,
        boolean             systemDefined,
        String              description,
        List<CodeValueEntry> values
) {}