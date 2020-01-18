/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arthurmanoha
 */
public class MoveInstructionModel extends InstructionModel {

    private CardinalPoint currentDirection;

    public MoveInstructionModel() {
        super();
        currentDirection = CardinalPoint.NORTH;
    }

    public CardinalPoint getCardinalPoint() {
        return this.currentDirection;
    }

    public void toggleDirection() {

        switch (currentDirection) {
            case NORTH:
                currentDirection = CardinalPoint.EAST;
                break;
            case EAST:
                currentDirection = CardinalPoint.SOUTH;
                break;
            case SOUTH:
                currentDirection = CardinalPoint.WEST;
                break;
            case WEST:
                currentDirection = CardinalPoint.NORTH;
                break;
            default:
                break;
        }
    }

    /**
     * Apply a specific treatment to the worker. That includes updating the
     * worker's current address (i.e. next instruction)
     *
     */
    @Override
    public void execute(int date, Worker w) {

        // Mark this worker as trying to move in that direction.
        w.moveInDirection(this.getCardinalPoint());
        w.setCurrentAddress(w.getCurrentAddress() + 1); // go to the next instruction
        super.execute(date, w);
    }
}
