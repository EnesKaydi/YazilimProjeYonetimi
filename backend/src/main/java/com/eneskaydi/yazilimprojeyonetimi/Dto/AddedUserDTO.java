package com.eneskaydi.yazilimprojeyonetimi.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddedUserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String phoneNumber;
    private String address;
    private String status;
} 