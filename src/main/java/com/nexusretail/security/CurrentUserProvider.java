package com.nexusretail.security;

import com.nexusretail.data.models.User;

public interface CurrentUserProvider {
    User getCurrentUser();
}