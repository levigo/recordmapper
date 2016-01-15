package org.jadice.recordmapper.cobol;

import org.jadice.recordmapper.cobol.CBLNested;
import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.cobol.CBLString;

@CBLRecord
public class TNestedInner {
	@CBLString(10)
	String inner1 = "inner1";

	@CBLNested
	TPropertyBinaryLength p1 = new TPropertyBinaryLength();

	@CBLString(10)
	String inner2 = "inner2";

	@CBLNested
	TPropertyBinaryLength p2 = new TPropertyBinaryLength();
}
