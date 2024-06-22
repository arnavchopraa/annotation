package org.example.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Rectangle2D;

@Setter
@Getter
@AllArgsConstructor
public class CoordPairs {
    private final Rectangle2D.Float rect;
    private final String text;
}
