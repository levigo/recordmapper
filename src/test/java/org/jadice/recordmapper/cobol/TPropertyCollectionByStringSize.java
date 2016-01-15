package org.jadice.recordmapper.cobol;

import java.util.List;

@CBLRecord
public class TPropertyCollectionByStringSize {
  @CBLNumericString(4)
  public int size;

  @CBLTable(sizeRef = "size")
  public List<TPropertyStringLength> properties;
}
