package org.example.models;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Table(name="annotations")
public class AnnotationCode {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="code_content")
    private String codeContent;
}
