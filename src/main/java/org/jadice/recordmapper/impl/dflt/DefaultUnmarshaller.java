package org.jadice.recordmapper.impl.dflt;

import java.io.InputStream;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.Unmarshaller;
import org.jadice.recordmapper.impl.RecordMapping;

public class DefaultUnmarshaller extends DefaultAbstractShaller
		implements
			Unmarshaller {
	private final DefaultMapper mapping;

	public DefaultUnmarshaller(DefaultMapper mapping) {
		this.mapping = mapping;
	}

	public <T extends Object> T unmarshal(Class<T> recordClass, InputStream is)
			throws MappingException {
		try {
			T record = recordClass.newInstance();

			RecordMapping rm = mapping.getRecordMapper(recordClass);
			if (null == rm)
				throw new MappingException( "Don't know how to deal with a "
						+ recordClass);

			DefaultUnmarshalContext ctx = new DefaultUnmarshalContext(
					new UnmarshalSource(is), this, rm, record);

			rm.unmarshal(record, ctx);
			return record;
		} catch (InstantiationException e) {
			throw new MappingException( "Can't instantiate record class "
          		+ recordClass, e);
		} catch (IllegalAccessException e) {
			throw new MappingException( "Can't access record class " + recordClass, e);
		}
	}
}
