package Turtle;

import processing.core.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * TurtleInstructions class, storing a list of Turtle-executable instructions.
 * Includes methods for easy manipulation of sequences of Turtle moves
 * that would not be possible to express succinctly in the vanilla Turtle language.
 * @author Franklin Pezzuti Dyer
 */
public class TurtleInstructions {

    public ArrayList<TurtleMove> instructions;
    // Assuming the Turtle starts at the origin, pointing east...
    float netX = 0;
    float netY = 0;
    float netHeading = 90;

    /**
     * Basic constructor for TurtleInstructions.
     * Creates an empty list of TurtleMoves.
     */
    public TurtleInstructions() {
        instructions = new ArrayList<TurtleMove>();
    }

    /**
     * Adds a "FORWARD" instruction to the list.
     * @param distance The forward distance of the move.
     */
    public void forward(float distance) {
        TurtleMove tm = new TurtleMove(TurtleMove.MoveType.FORWARD, distance);
        instructions.add(tm);
        netX += distance * Math.cos(Math.toRadians(netHeading));
        netY += distance * Math.sin(Math.toRadians(netHeading));
    }

    /**
     * Adds a "BACK" instruction to the list.
     * @param distance The backwards distance of the move.
     */
    public void back(float distance) {
        TurtleMove tm = new TurtleMove(TurtleMove.MoveType.BACK, distance);
        instructions.add(tm);
        netX += -distance * Math.cos(Math.toRadians(netHeading));
        netY += -distance * Math.sin(Math.toRadians(netHeading));
    }

    /**
     * Adds a "LEFT" instruction to the list.
     * @param degrees The number of degrees of the counterclockwise turn.
     */
    public void left(float degrees) {
        TurtleMove tm = new TurtleMove(TurtleMove.MoveType.LEFT, degrees);
        instructions.add(tm);
        netHeading += degrees;
        netHeading = netHeading % 360;
    }

    /**
     * Adds a "RIGHT" instruction to the list.
     * @param degrees The number of degrees of the clockwise turn.
     */
    public void right(float degrees) {
        TurtleMove tm = new TurtleMove(TurtleMove.MoveType.RIGHT, degrees);
        instructions.add(tm);
        netHeading += -degrees;
        netHeading = netHeading % 360;
    }

    /**
     * Copies a given TurtleMove to the list of instructions.
     * Note that it adds a copy of the given TurtleMove, not a pointer to the original.
     * @param tm The TurtleMove to be copies into the instructions.
     */
    public void addMove(TurtleMove tm) {
        switch(tm.moveType) {
            case FORWARD: forward(tm.amount); break;
            case BACK: back(tm.amount); break;
            case LEFT: left(tm.amount); break;
            case RIGHT: right(tm.amount); break;
        }
    }

    /**
     * Concatenates the instructions listed by another TurtleInstructions object
     * with this TurtleInstructions object's list of instructions.
     * Note that it adds copies of the given TurtleMoves to the list, not pointers to the originals.
     * @param ti The TurtleInstructions whose instructions are to be concatenated.
     */
    public void then(TurtleInstructions ti) {
        for (TurtleMove tm : ti.instructions) {
            addMove(tm);
        }
    }

    /**
     * Concatenates all TurtleMoves from an ArrayList to this object's list of instructions.
     * Note that it adds copies of the given TurtleMoves, not pointers to the originals.
     * @param moves An ArrayList of TurtleMoves to be concatenated with the current instructions.
     */
    public void then(ArrayList<TurtleMove> moves) {
        for (TurtleMove tm : moves) {
            addMove(tm);
        }
    }

    /**
     * Update instructions list to that the current sequence of moves is repeated
     * a given number of times.
     * @param n The number of times the current sequence should be repeated.
     */
    public void repeat(int n) {
        if (n < 0) {
            reverse();
            repeat(-n);
            return;
        } else if (n == 0) {
            instructions = new ArrayList<TurtleMove>();
        } else {
            int numMoves = instructions.size();
            for (int i = 0; i < n-1; i++) {
                for (int j = 0; j < numMoves; j++) {
                    addMove(instructions.get(j));
                }
            }
        }
    }

    /**
     * Reverse the current sequence of instructions.
     */
    public void reverse() {
        for (TurtleMove tm : instructions) {
            tm.reverse();
        }
        Collections.reverse(instructions);
        float oldX = netX;
        float oldY = netY;
        netX = oldX * (float)Math.cos(netHeading) + oldY * (float)Math.sin(netHeading);
        netY = -oldX * (float)Math.sin(netHeading) + oldY * (float)Math.cos(netHeading);
        netHeading = -netHeading;
    }

    /**
     * Dilates all lengths in the current sequence of moves by a given factor.
     * @param factor The dilation factor.
     */
    public void dilate(float factor) {
        for (TurtleMove tm : instructions) {
            tm.dilate(factor);
        }
        netX = netX * factor;
        netY = netY * factor;
    }

    /**
     * Adjust the initial and final angles of the Turtle so that
     * its final position lies on the positive Y-axis and its final heading is 90 degrees (pointing up).
     */
    public void normalizeAngle() {
        float rDisplacement = (float)Math.sqrt(Math.pow(netX,2) + Math.pow(netY,2));
        float thetaDisplacement = (float)((180/Math.PI)*Math.atan2(-netX, netY));
        TurtleMove normalizeHeading = new TurtleMove(TurtleMove.MoveType.RIGHT, thetaDisplacement);
        instructions.add(0, normalizeHeading);
        right(netHeading - thetaDisplacement - 90);
        netHeading = 90;
        netX = 0;
        netY = rDisplacement;
    }

    /**
     * Scale the Turtle's moves so that its final net displacement has a given length.
     * @param length
     */
    public void normalizeLength(float length) {
        float rDisplacement = (float)Math.sqrt(Math.pow(netX,2) + Math.pow(netY,2));
        dilate(length/rDisplacement);
        rDisplacement = (float)Math.sqrt(Math.pow(netX,2) + Math.pow(netY,2));
    }

    /**
     * Substitute each FORWARD move the Turtle makes with a scaled copy
     * of a given sequence of moves, and repeat this substitution
     * a given number of times.
     * @param ti The sequence of moves to be substituted.
     * @param n The number of iterations of substitution.
     */
    public void fractalize(TurtleInstructions ti, int n) {
        if (n == 1) {
            int numMoves = instructions.size();
            ArrayList<TurtleMove> oldMoves = instructions;
            instructions = new ArrayList<TurtleMove>();
            for (int i = 0; i < numMoves; i++) {
                TurtleMove tm = oldMoves.get(i);
                if (tm.moveType == TurtleMove.MoveType.FORWARD) {
                    ti.normalizeLength(tm.amount);
                    then(ti);
                } else {
                    addMove(tm);
                }
            }
        } else {
            fractalize(ti, 1);
            fractalize(ti, n-1);
        }
    }

}

/*
TO DO:
- make it possible to construct TurtleMoves using strings rather than the built-in enum,
    so that TurtleMove construction doesn't have to be so wordy
 */
