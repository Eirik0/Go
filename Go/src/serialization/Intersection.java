// automatically generated, do not modify

package serialization;

import java.nio.*;
import java.lang.*;
import java.util.*;
import flatbuffers.*;

public class Intersection extends Struct {
  public Intersection __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public byte X() { return bb.get(bb_pos + 0); }
  public byte Y() { return bb.get(bb_pos + 1); }

  public static int createIntersection(FlatBufferBuilder builder, byte X, byte Y) {
    builder.prep(1, 2);
    builder.putByte(Y);
    builder.putByte(X);
    return builder.offset();
  }
};

