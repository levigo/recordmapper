package org.jadice.recordmapper;

import org.jadice.recordmapper.impl.dflt.DefaultMapper;

public class MappingFactory {
  public static MappingFactory create(Class<?>... recordClasses)
			throws MappingException {
		return new MappingFactory(recordClasses);
	}

	private final DefaultMapper mapping;

  private MappingFactory(Class<?>... recordClasses) throws MappingException {
		mapping = new DefaultMapper(recordClasses);
	}

	public Marshaller createMarshaller() {
		return mapping.createMarshaller();
	}

	public Unmarshaller createUnmarshaller() {
		return mapping.createUnmarshaller();
	}
}
