package com.nexusretail.common.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendEmail(String username , String email, String password);
}
