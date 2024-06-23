package org.example.backend.requestModels;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ContactForm {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
}
