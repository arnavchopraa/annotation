package org.example.backend.importmodels;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Project {
    private List<String> studentNos;
    private List<Coordinator> coordinators;

    /**
     * Override of toString method from Object class
     *
     * @return A string representation of a Project object.
     */
    @Override
    public String toString() {
        return "Project@" + Integer.toHexString(hashCode()) +
                ":[students=" + studentNos.toString() +
                ",coordinators=" + coordinators.toString();
    }
}
