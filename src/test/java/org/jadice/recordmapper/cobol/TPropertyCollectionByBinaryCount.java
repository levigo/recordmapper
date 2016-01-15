package org.jadice.recordmapper.cobol;

import java.util.List;

@CBLRecord
public class TPropertyCollectionByBinaryCount {
	@CBLNumeric(4)
	public int count;

	@CBLTable(countRef="count")
	public List<TPropertyBinaryLength> properties;
}
