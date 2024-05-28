package org.example.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Student {
    private String studentNo;
    private String username;
    private String lastName;
    private String firstName;
    private String email;
    private String groupCategory;
    private String groupName;

    @Override
    public String toString() {
        return "Student@" + Integer.toHexString(hashCode()) +
                ":[studentNo=" + studentNo +
                ",username=" + username +
                ",lastName=" + lastName +
                ",firstName=" + firstName +
                ",email=" + email +
                ",groupCategory=" + groupCategory +
                ",groupName=" + groupName + "]\n";
    }
}
