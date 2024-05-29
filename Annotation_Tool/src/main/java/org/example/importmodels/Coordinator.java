package org.example.importmodels;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Coordinator {
    private String fullName;
    private String email;

    /**
     * Override of toString method from Object class
     *
     * @return A string representation of a Coordinator object.
     */
    @Override
    public String toString() {
        return "Coordinator@" + Integer.toHexString(hashCode()) +
                ":[fullName=" + fullName +
                ",email=" + email + "]";
    }
}
