package org.jadice.recordmapper.cobol;

import org.jadice.recordmapper.Endian;
import org.jadice.recordmapper.RecordAttributes;

public class CBLRecordAttributes implements RecordAttributes {
  private final Endian endian = Endian.BIG;

  public Endian getEndian() {
    return endian;
  }
}
