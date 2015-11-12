package org.jadice.recordmapper.cobol;

import java.util.List;

import org.jadice.recordmapper.cobol.CBLCount;
import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.cobol.CBLTable;

@CBLRecord
public class TPropertyCollectionByCount {
	@CBLCount(length = 4, ref = "properties")
	public int count;

	@CBLTable
	public List<TProperty> properties;
}
