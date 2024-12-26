package com.dailywork.dreamshops.request;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
