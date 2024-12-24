package com.ebanking.NotificationsService.model;

import com.ebanking.NotificationsService.entity.Beneficiary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SMS {
    private TransferType transferType;

    private  double  amount  ;
    private  String pin ;
    private  String ref ;
    private String phone;
    private  Boolean sendRef ;
    private  String customerFirstName;
    private  String customerLastName;
    private  String beneficiaryFirstName;
    private  String beneficiaryLastName;

}
