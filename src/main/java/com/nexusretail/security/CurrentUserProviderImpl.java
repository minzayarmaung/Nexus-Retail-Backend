package com.nexusretail.security;

import com.nexusretail.common.exception.NoAuthorizationException;
import com.nexusretail.data.models.User;
import com.nexusretail.data.repositories.UserRepository;
import com.nexusretail.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserProviderImpl implements CurrentUserProvider {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NoAuthorizationException("No authenticated user found in security context.");
        }

        final String username = resolveUsername(authentication);

        // Re-fetch from DB rather than trusting the principal object, so we get a
        // managed entity with office/role associations properly initialized.
        return userRepository.findOneWithNotFoundDetection(username);
    }

    private String resolveUsername(final Authentication authentication) {
        final Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        if (principal instanceof String username) {
            // Covers the case where the principal is just the username string
            // (e.g. some JWT filter setups set it directly)
            return username;
        }
        throw new NoAuthorizationException("Unable to resolve current user from authentication principal.");
    }
}