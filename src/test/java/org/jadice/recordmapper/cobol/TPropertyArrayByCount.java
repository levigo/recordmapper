package org.jadice.recordmapper.cobol;

import org.jadice.recordmapper.cobol.CBLCount;
import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.cobol.CBLTable;

@CBLRecord
public class TPropertyArrayByCount {
	@CBLCount(length = 4, ref = "properties")
	public int count;

	@CBLTable
	public TProperty properties[];
}
