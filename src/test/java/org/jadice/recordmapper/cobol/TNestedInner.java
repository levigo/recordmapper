package org.jadice.recordmapper.cobol;

import org.jadice.recordmapper.cobol.CBLNested;
import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.cobol.CBLString;

@CBLRecord
public class TNestedInner {
	@CBLString(10)
	String inner1 = "inner1";

	@CBLNested
	TProperty p1 = new TProperty();

	@CBLString(10)
	String inner2 = "inner2";

	@CBLNested
	TProperty p2 = new TProperty();
}
