package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.RecordMapping;

public class CBLRecordImpl extends RecordMapping {
	@Override
  protected void assertCompatibility(Class<?> c, Field f,
			Annotation fieldAnnotation, FieldMapping mapping) throws MappingException {
		if (!fieldAnnotation.annotationType().getName().startsWith(
"org.jadice.recordmapper.cobol.CBL"))
			throw new MappingException(this, "Incompatible field mapping "
					+ fieldAnnotation + " on " + c + " field " + f);
	}

	@Override
  protected void assertCompatibility(Class<?> c, Annotation recordAnnotation)
			throws MappingException {
		if (!recordAnnotation.annotationType().equals(CBLRecord.class))
			throw new MappingException(this, "Incompatible record mapping "
					+ recordAnnotation + " on " + c);
	}
}
