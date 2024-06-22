package org.example.backend.requestModels;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FeedbackForm {
    private String name;
    private String role;
    private String message;
}
