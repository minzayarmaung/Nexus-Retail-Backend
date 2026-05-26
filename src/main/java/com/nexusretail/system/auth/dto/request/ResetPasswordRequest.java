package com.nexusretail.system.auth.dto.request;

import lombok.Builder;

@Builder
public record ResetPasswordRequest(
        String currentPassword,
        String newPassword
) {}
