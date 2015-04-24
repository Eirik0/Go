package game;

import serialization.GameState;
import serialization.GameState.Color;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by grubino on 4/24/15.
 */
public class CapturedGroup extends Group {
    private int moveIndex;

    public CapturedGroup(final Color c, final Set<Point> pts, final Set<Point> libs, int i) {
        super(c, pts, libs);
        moveIndex = i;
    }
    public CapturedGroup(final Group g, int i) {
        super(g);
        moveIndex = i;
    }
    public int getMoveIndex() {
        return moveIndex;
    }

    public GameState.Capture toCapture() {
        return GameState.Capture.newBuilder()
                .setMoveIndex(moveIndex)
                .setCapturedGroup(toGroup())
                .build();
    }

}
