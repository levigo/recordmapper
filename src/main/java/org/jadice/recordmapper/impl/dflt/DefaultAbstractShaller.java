package org.jadice.recordmapper.impl.dflt;

import java.util.HashMap;
import java.util.Map;

import org.jadice.recordmapper.RecordAttributes;

/**
 * Superclass for Unmar- and Mar- Shallers :-)
 */
public class DefaultAbstractShaller {
  final Map<Class<?>, RecordAttributes> recordAttributes = new HashMap<Class<?>, RecordAttributes>();

	public DefaultAbstractShaller() {
		super();
	}

	@SuppressWarnings("unchecked")
	public <T extends RecordAttributes> T getRecordAttributes(Class<T> c) {
		RecordAttributes ra = recordAttributes.get(c);
		if (null == ra) {
			try {
				ra = c.newInstance();
			} catch (final Exception e) {
				// should not happen
				throw new RuntimeException("Can't create record attributes", e);
			}
			recordAttributes.put(c, ra);
		}

		return (T) ra;
	}

}