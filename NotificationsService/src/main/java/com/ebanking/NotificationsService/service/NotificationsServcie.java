package com.ebanking.NotificationsService.service;

import com.ebanking.NotificationsService.model.Customer;
import com.ebanking.NotificationsService.model.SMS;
import com.ebanking.NotificationsService.model.SendVerificationCodeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface NotificationsServcie {
    String  sendSMS( SMS sms );
    SendVerificationCodeResponse verifyIdentity(String phone , String code );
//    String test(Customer customer) throws JsonProcessingException;

}
