package Turtle;

import processing.core.*;

/**
 * TurtleMove class, storing a single basic Turtle move.
 * Every move has a type and a magnitude, which is
 * either a length or an angle, depending on the move type.
 * @author Franklin Pezzuti Dyer
 */
public class TurtleMove {
    MoveType moveType;
    float amount;

    enum MoveType {
        FORWARD, BACK, LEFT, RIGHT
    }

    /**
     * Constructs an "empty" TurtleMove (with zero magnitude).
     * @param typeOfMove The type of move.
     */
    public TurtleMove(MoveType typeOfMove) {
        moveType = typeOfMove;
        amount = 0;
    }

    /**
     * Constructs a TurtleMove with given type and magnitude.
     * @param typeOfMove The type of move.
     * @param moveAmount The magnitude of the move, in length or degrees.
     */
    public TurtleMove(MoveType typeOfMove, float moveAmount) {
        moveType = typeOfMove;
        amount = moveAmount;
    }

    /**
     * Creates an identical copy of this TurtleMove.
     * @return An identical copy of this TurtleMove.
     */
    public TurtleMove copy() {
        return new TurtleMove(moveType, amount);
    }

    /**
     * "Reverses" this TurtleMove by swapping FORWARD/BACK and LEFT/RIGHT.
     */
    public void reverse() {
        switch (moveType) {
            case FORWARD: moveType = MoveType.BACK; break;
            case BACK: moveType = MoveType.FORWARD; break;
            case LEFT: moveType = MoveType.RIGHT; break;
            case RIGHT: moveType = MoveType.LEFT; break;
        }
    }

    /**
     * Scales the magnitude of this TurtleMove by a given factor,
     * if it is a length and not an angle measure.
     * @param factor The factor of dilation.
     */
    public void dilate(float factor) {
        if (moveType == MoveType.FORWARD || moveType == MoveType.BACK) {
            amount = amount * factor;
        }
    }

}