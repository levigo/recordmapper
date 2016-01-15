package org.jadice.recordmapper.cobol;

@CBLRecord
public class TPropertyArrayByBinaryCount {
	@CBLNumeric(4)
	public int count;

	@CBLTable(countRef="count")
	public TPropertyBinaryLength properties[];
}
