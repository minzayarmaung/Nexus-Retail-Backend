package com.nexusretail.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    SYSTEM_ADMIN,
    OWNER,
    SALESPERSON,
    HR,
    MANAGER,
}