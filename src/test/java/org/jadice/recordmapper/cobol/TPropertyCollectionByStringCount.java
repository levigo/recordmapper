package org.jadice.recordmapper.cobol;

import java.util.List;

@CBLRecord
public class TPropertyCollectionByStringCount {
  @CBLNumericString(4)
  public int count;

  @CBLTable(countRef = "count")
  public List<TPropertyStringLength> properties;
}
