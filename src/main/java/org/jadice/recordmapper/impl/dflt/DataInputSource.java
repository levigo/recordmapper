package org.jadice.recordmapper.impl.dflt;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;

import org.jadice.recordmapper.MappingException;

public class DataInputSource implements InputSource {
  private final DataInput di;
  private int position = 0;

  private int peeked = -1;

  public DataInputSource(DataInput di) {
    this.di = di;
  }

  public byte[] getBytes(int size) throws MappingException {
    byte buffer[] = new byte[size];

    if (size > 0)
      try {
        if (peeked > 0) {
          buffer[0] = (byte) peeked;
          di.readFully(buffer, 1, buffer.length - 1);
          peeked = -1;
        } else
          di.readFully(buffer);
      } catch (IOException e) {
        throw new MappingException(e);
      }

    position += size;

    return buffer;
  }

  public int getPosition() {
    return position;
  }

  @Override
  public boolean hasMore() throws IOException {
    if (peeked > 0)
      return true;

    try {
      peeked = di.readUnsignedByte();
    } catch (EOFException e) {
      return false;
    }
    return true;
  }
}
