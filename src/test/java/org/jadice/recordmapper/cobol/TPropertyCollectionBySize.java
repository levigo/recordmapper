package org.jadice.recordmapper.cobol;

import java.util.List;

import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.cobol.CBLSize;
import org.jadice.recordmapper.cobol.CBLTable;

@CBLRecord
public class TPropertyCollectionBySize {
	@CBLSize(length = 4, ref = "properties")
	public int size;

	@CBLTable
	public List<TProperty> properties;
}
