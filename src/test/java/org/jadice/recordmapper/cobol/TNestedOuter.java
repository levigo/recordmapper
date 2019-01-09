package org.jadice.recordmapper.cobol;

import org.jadice.recordmapper.cobol.CBLNested;
import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.cobol.CBLString;

@CBLRecord
public class TNestedOuter {
  @CBLString(10)
  String outer1 = "outer1";

  @CBLNested
  TNestedInner i1 = new TNestedInner();

  @CBLString(10)
  String outer2 = "outer2";

  @CBLNested
  TNestedInner i2 = new TNestedInner();
}
