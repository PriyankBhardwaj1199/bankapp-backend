package com.app.bank.service;

import com.app.bank.dto.EmailDetails;

public interface EmailService{

    void sendEmailAlert(EmailDetails emailDetails);
}
