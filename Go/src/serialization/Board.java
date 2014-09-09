// automatically generated, do not modify

package serialization;

import java.nio.*;
import java.lang.*;
import java.util.*;
import flatbuffers.*;

public class Board extends Table {
  public static Board getRootAsBoard(ByteBuffer _bb, int offset) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (new Board()).__init(_bb.getInt(offset) + offset, _bb); }
  public Board __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public Placement places(int j) { return places(new Placement(), j); }
  public Placement places(Placement obj, int j) { int o = __offset(4); return o != 0 ? obj.__init(__vector(o) + j * 3, bb) : null; }
  public int placesLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public byte toMove() { int o = __offset(6); return o != 0 ? bb.get(o + bb_pos) : 0; }

  public static void startBoard(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addPlaces(FlatBufferBuilder builder, int placesOffset) { builder.addOffset(0, placesOffset, 0); }
  public static void startPlacesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(3, numElems, 1); }
  public static void addToMove(FlatBufferBuilder builder, byte toMove) { builder.addByte(1, toMove, 0); }
  public static int endBoard(FlatBufferBuilder builder) { return builder.endObject(); }
};

