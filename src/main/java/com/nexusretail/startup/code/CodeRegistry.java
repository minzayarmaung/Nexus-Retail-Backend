package com.nexusretail.startup.code;

import com.nexusretail.common.constant.Status;

import java.util.List;

public final class CodeRegistry {
    private CodeRegistry() {}

    public static List<CodeDefinition> all() {
        return List.of(

                new CodeDefinition("Gender", true,"Gender Options" , Status.ACTIVE ,List.of(
                        new CodeValueEntry("Male",   "Male",   1, true),
                        new CodeValueEntry("Female", "Female", 2, true),
                        new CodeValueEntry("Other",  "Other",  3, true)
                )),

                new CodeDefinition("MaritalStatus", true, "Marital Status Options", Status.ACTIVE ,List.of(
                        new CodeValueEntry("Single",   "Single",   1, true),
                        new CodeValueEntry("Married",  "Married",  2, true),
                        new CodeValueEntry("Divorced", "Divorced", 3, true),
                        new CodeValueEntry("Widowed",  "Widowed",  4, true)
                )),

                new CodeDefinition("ClientClassification", true,"Client Classification Options", Status.ACTIVE ,List.of(
                        new CodeValueEntry("Individual", "Individual client", 1, true),
                        new CodeValueEntry("Group",      "JLG/Group client",  2, true)
                ))
        );
    }
}