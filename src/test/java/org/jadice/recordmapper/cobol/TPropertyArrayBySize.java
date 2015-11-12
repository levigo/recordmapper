package org.jadice.recordmapper.cobol;

import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.cobol.CBLSize;
import org.jadice.recordmapper.cobol.CBLTable;

@CBLRecord
public class TPropertyArrayBySize {
	@CBLSize(length = 4, ref = "properties")
	public int size;

	@CBLTable
	public TProperty properties[];
}
