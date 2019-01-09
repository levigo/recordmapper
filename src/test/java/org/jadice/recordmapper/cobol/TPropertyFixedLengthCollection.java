package org.jadice.recordmapper.cobol;

import java.util.List;

import org.jadice.recordmapper.cobol.CBLFixedLength;
import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.cobol.CBLTable;

@CBLRecord
public class TPropertyFixedLengthCollection {
  @CBLTable
  @CBLFixedLength(5)
  public List<TPropertyBinaryLength> properties;
}
