package com.vitral.auth.domain.model.gateway;

import com.vitral.auth.domain.model.EmailMessage;

public interface EmailGateway {
    void sendEmail(EmailMessage email);
}