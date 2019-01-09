package org.jadice.recordmapper.impl.dflt;

import java.io.OutputStream;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.Marshaller;
import org.jadice.recordmapper.impl.RecordMapping;

public class DefaultMarshaller extends DefaultAbstractShaller implements Marshaller {
  private final DefaultMapper mapping;

  public DefaultMarshaller(DefaultMapper mapping) {
    this.mapping = mapping;
  }

  public void marshal(Object record, OutputStream os) throws MappingException {
    RecordMapping rm = mapping.getRecordMapper(record.getClass());
    if (null == rm)
      throw new MappingException("Don't know how to deal with " + record.getClass());

    DefaultMarshalContext mc = new DefaultMarshalContext(null, this, rm, record, os);

    rm.marshal(record, mc);
  }
}
