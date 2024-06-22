package org.example.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TriplePair {
    private List<Float> frontX;
    private List<Float> endX;
    private List<Float> ys;
}
