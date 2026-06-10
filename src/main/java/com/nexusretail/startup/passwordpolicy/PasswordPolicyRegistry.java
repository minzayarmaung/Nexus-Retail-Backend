package com.nexusretail.startup.passwordpolicy;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public final class PasswordPolicyRegistry {

    public static List<PasswordPolicyDefinition> all() {
        return List.of(
                new PasswordPolicyDefinition(
                        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                        "Password must be at least 6 characters, no more than 50 characters long, must include at least one upper case letter, one lower case letter, one numeric digit and no space",
                        false,
                        "SECURE"
                ),
                new PasswordPolicyDefinition(
                        "^.{1,50}$",
                        "Password most be at least 1 character and not more that 50 characters long",
                        false,
                        "SIMPLE"
                ),
                new PasswordPolicyDefinition(
                        "^(?!.*(.)\\1)(?!.*\\s)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^\\w\\s]).{12,50}$",
                        "Password must be 12 to 50 characters long, containing at least one uppercase letter, one lowercase letter, one numeric digit, and one special character, with no spaces or consecutive repeating characters",
                        true,
                        "STRONG"
                )
        );
    }
}
