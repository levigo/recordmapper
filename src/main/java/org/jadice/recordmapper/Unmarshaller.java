package org.jadice.recordmapper;

import java.io.DataInput;
import java.io.InputStream;

public interface Unmarshaller {
  public <T extends RecordAttributes> T getRecordAttributes(Class<T> c);

  public <T extends Object> T unmarshal(Class<T> rootRecord, InputStream is) throws MappingException;

  public Object unmarshal(Class<?> type, DataInput di) throws MappingException;
}
