package com.ebanking.NotificationsService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class SendVerificationCodeResponse {
    private String code ;
    private String message;


}
