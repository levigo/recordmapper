package org.jadice.recordmapper;

import java.io.OutputStream;

public interface Marshaller {
  public <T extends RecordAttributes> T getRecordAttributes(Class<T> c);

  public void marshal(Object record, OutputStream os) throws MappingException;
}
