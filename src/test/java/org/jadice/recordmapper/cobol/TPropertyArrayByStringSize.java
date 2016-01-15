package org.jadice.recordmapper.cobol;

@CBLRecord
public class TPropertyArrayByStringSize {
	@CBLNumericString(4)
	public int size;

	@CBLTable(sizeRef="size")
	public TPropertyStringLength properties[];
}
