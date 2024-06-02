package org.example.backend.utils;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Table {
    private float topLeftX;
    private float topLeftY;
    private float bottomRightX;
    private float bottomRightY;

    /**
     * Extends the coordinates of the table with the given line
     * @param line Line which needs to be included in the table coordinates
     */
    public void combineTable(Line line) {
        if(line.getStartX() < this.getTopLeftX())
            this.setTopLeftX(line.getStartX());
        if(line.getStartY() < this.getTopLeftY())
            this.setTopLeftY(line.getStartY());
        if(line.getEndX() > this.getBottomRightX())
            this.setBottomRightX(line.getEndX());
        if(line.getEndY() > this.getBottomRightY())
            this.setBottomRightY(line.getEndY());
    }

    /**
     * toString method
     * @return String representation of the object in a human-readable format
     */
    public String toString() {
        return "Top Left: (" + topLeftX + ", " + topLeftY + ") Bottom right: (" + bottomRightX + ", " + bottomRightY + ")";
    }
}
