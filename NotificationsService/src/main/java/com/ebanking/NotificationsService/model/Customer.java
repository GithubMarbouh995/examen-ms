package com.ebanking.NotificationsService.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    private  String  firstName ;
    private  String lastname ;
    private  String  phone ;
}