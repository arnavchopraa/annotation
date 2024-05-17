package org.example.utils;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Line implements Comparable<Line> {
    private float startX;
    private float startY;
    private float endX;
    private float endY;

    /**
     * Method that checks whether the line is horizontal or vertical
     * @return true => Line is vertical; false => otherwise
     */
    public boolean isVertical() {
        return startX == endX;
    }

    /**
     * Checks whether this line intersects with another line
     * @param other line given to test intersection with
     * @return true iff lines intersect
     */
    public boolean intersectsWith(Line other) {
        // two lines with the same orientation cannot intersect, unless they are the same
        if(this.isVertical() == other.isVertical())
            return false;
        // TODO - testat pentru tabele cu intersectie in colturi -> daca crapa, adauga eroare la >=
        if(this.isVertical()) {
            return other.startY >= this.startY && other.startY <= this.endY && this.startX >= other.startX && this.startX <= other.endX;
        }
        else if(other.isVertical()) {
            return this.startY >= other.startY && this.startY <= other.endY && other.startX >= this.startX && other.startX <= this.endX;
        }
        return false;
    }

    /**
     * Checks whether this line can be merged with another line, taking into account a small error
     * @param other line given to test merge possibility with
     * @param error small discrepancy between lines
     * @return true iff lines can be merged, while also modifying parameters of this object accordingly
     */
    public boolean mergeWith(Line other, float error) {
        // Only merge lines with same orientation
        if(this.isVertical() != other.isVertical())
            return false;

        float v1 = (float) Math.sqrt((this.getEndX() - other.getStartX()) * (this.getEndX() - other.getStartX()) +
            (this.getEndY() - other.getStartY()) * (this.getEndY() - other.getStartY()));
        float v2 = (float) Math.sqrt((this.getStartX() - other.getEndX()) * (this.getStartX() - other.getEndX()) +
            (this.getStartY() - other.getEndY()) * (this.getStartY() - other.getEndY()));
        if(v1 < error) {
            this.endX = other.endX;
            this.endY = other.endY;
            return true;
        } else if (v2 < error) {
            this.startX = other.startX;
            this.startY = other.startY;
            return true;
        }
        return false;
    }

    /**
     * Comparator method for Line object, in ascending order
     * @param other the object to be compared.
     * @return order of Line objects
     */
    @Override
    public int compareTo(Line other) {
        if(this.startX == other.startX)
            return Float.compare(this.startY, other.startY);
        return Float.compare(this.startX, other.startX);
    }

    @Override
    public String toString() {
        return "startX = " + this.startX + ", startY = " + this.startY + ", endX = " + this.endX + ", endY = " + this.endY;
    }
}
