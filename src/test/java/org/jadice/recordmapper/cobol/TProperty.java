/*
 * Copyright (c) 2004 levigo holding gmbh. All Rights Reserved. This software is
 * the proprietary information of levigo holding gmbh. Use is subject to license
 * terms. Created on 30.06.2004
 */
package org.jadice.recordmapper.cobol;

import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.cobol.CBLSize;
import org.jadice.recordmapper.cobol.CBLString;
import org.jadice.recordmapper.cobol.CBLVariableString;

/**
 * Eine Property
 * 
 * @author B022449
 */
@CBLRecord
public class TProperty {
	@CBLString(40)
	public String name;

	@CBLSize(length = 4, ref = "value")
	int length;

	@CBLVariableString()
	public String value;
}