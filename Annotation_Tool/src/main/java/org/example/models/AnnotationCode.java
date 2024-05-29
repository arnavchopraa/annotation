package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name="annotations")
public class AnnotationCode implements Serializable {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="codeContent")
    private String codeContent;
}
