package com.ebanking.NotificationsService.controller;

import com.ebanking.NotificationsService.model.Customer;
import com.ebanking.NotificationsService.model.SMS;
import com.ebanking.NotificationsService.model.SendVerificationCodeResponse;
import com.ebanking.NotificationsService.service.NotificationsServcie;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationsController {
    @Autowired
    NotificationsServcie notificationsServcie  ;
    @PostMapping("/send-sms")
    public String sendSMS(@RequestBody SMS sms) {
        return notificationsServcie.sendSMS(sms);
    }

    @PostMapping("/OTP")
    public ResponseEntity<SendVerificationCodeResponse> verifyIdentity(@RequestParam String phone, @RequestParam String code) {
        return new ResponseEntity<>(notificationsServcie.verifyIdentity(phone,code), HttpStatus.OK);
    }
//    @PostMapping("/test")
//
//    public String test(@RequestBody Customer customer
//    ) throws JsonProcessingException {
//
//        return notificationsServcie.test(customer);
//    }
}
