// automatically generated, do not modify

package serialization;

import java.nio.*;
import java.lang.*;
import java.util.*;
import flatbuffers.*;

public class Placement extends Struct {
  public Placement __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public Intersection place() { return place(new Intersection()); }
  public Intersection place(Intersection obj) { return obj.__init(bb_pos + 0, bb); }
  public byte player() { return bb.get(bb_pos + 2); }

  public static int createPlacement(FlatBufferBuilder builder, byte Intersection_X, byte Intersection_Y, byte player) {
    builder.prep(1, 3);
    builder.putByte(player);
    builder.prep(1, 2);
    builder.putByte(Intersection_Y);
    builder.putByte(Intersection_X);
    return builder.offset();
  }
};

