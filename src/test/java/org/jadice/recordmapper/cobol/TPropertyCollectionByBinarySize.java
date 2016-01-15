package org.jadice.recordmapper.cobol;

import java.util.List;

@CBLRecord
public class TPropertyCollectionByBinarySize {
  @CBLNumeric(4)
  public int size;

  @CBLTable(sizeRef = "size")
  public List<TPropertyBinaryLength> properties;
}
