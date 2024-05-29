package org.example.importmodels;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Coordinator {
    private String fullName;
    private String email;

    private List<Association> associations;

    public Coordinator(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        associations = new ArrayList<>();
    }

    /**
     * Override of toString method from Object class
     *
     * @return A string representation of a Coordinator object.
     */
    @Override
    public String toString() {
        return "Coordinator@" + Integer.toHexString(hashCode()) +
                ":[fullName=" + fullName +
                ",email=" + email + "]" +
                "associations= " + associations.toString();
    }
}
