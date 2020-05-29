package org.jadice.recordmapper.cobol;

@CBLRecord
public class TPropertyArrayByStringCount {
  @CBLNumericString(4)
  public int count;

  @CBLTable(countRef = "count")
  public TPropertyStringLength properties[];
}
