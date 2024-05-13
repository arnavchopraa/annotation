package org.example.utils;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Line {
    private float startX;
    private float startY;
    private float endX;
    private float endY;

    public boolean isVertical() {
        return startX == endX;
    }

    // TODO - unite vertical lines (add an error of approx 0.4)
    // TODO - unite lines of a single table
    // TODO - find out table coordinates
    // TODO - Extract text from tables, remove them from PDF
}
