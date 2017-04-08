/*
 * integer constant defined to identify four types of state for each block
 * Reachable: the block is not an obstacle
 * Blocked: the block is an obstacle
 * Unexplored: the block is explored yet
 */
public class BlockState {
    public static final int REACHABLE = 0;
    public static final int UNEXPLORED = 1;
    public static final int BLOCKED = 2;
    public static final int UNREACHABLE = 3;
}