package org.jadice.recordmapper;

import java.io.InputStream;

public interface Unmarshaller {
  public <T extends RecordAttributes> T getRecordAttributes(Class<T> c);

  public <T extends Object> T unmarshal(Class<T> rootRecord, InputStream is) throws MappingException;
}
