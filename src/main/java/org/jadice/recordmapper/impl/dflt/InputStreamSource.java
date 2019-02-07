package org.jadice.recordmapper.impl.dflt;

import java.io.IOException;
import java.io.InputStream;

import org.jadice.recordmapper.MappingException;

public class InputStreamSource implements InputSource {
  private final InputStream is;
  private int position = 0;

  public InputStreamSource(InputStream is) {
    this.is = is;
  }

  /* (non-Javadoc)
   * @see org.jadice.recordmapper.impl.dflt.InputSource#getBytes(int)
   */
  @Override
  public byte[] getBytes(int size) throws MappingException {
    byte buffer[] = new byte[size];

    int read = 0;
    int r;
    try {
      while ((r = is.read(buffer, read, size - read)) > 0)
        read += r;
    } catch (IOException e) {
      throw new MappingException(e);
    }

    if (read != size)
      throw new MappingException("Unexpected END_OF_STREAM");

    position += size;

    return buffer;
  }

  /* (non-Javadoc)
   * @see org.jadice.recordmapper.impl.dflt.InputSource#getPosition()
   */
  @Override
  public int getPosition() {
    return position;
  }

  @Override
  public boolean hasMore() throws IOException {
    return is.available() > 0;
  }

}
