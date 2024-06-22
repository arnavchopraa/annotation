package org.example.backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Schema(description = "Annotation Code entity")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Table(name="annotations")
public class AnnotationCode {

    @Schema(description = "ID of the annotation code", example = "WD1")
    @Id
    @Column(name="id")
    private String id;

    @Schema(description = "Content of the annotation code", example = "Spelling mistake. Please check your spelling.")
    @Column(name="code_content")
    private String codeContent;
}
