package org.jadice.recordmapper.cobol;

@CBLRecord
public class TPropertyArrayByBinarySize {
	@CBLNumeric(4)
	public int size;

	@CBLTable(sizeRef="size")
	public TPropertyBinaryLength properties[];
}
