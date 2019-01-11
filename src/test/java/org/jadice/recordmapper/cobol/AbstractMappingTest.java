package org.jadice.recordmapper.cobol;

import java.io.ByteArrayOutputStream;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.Marshaller;
import org.junit.Assert;


public class AbstractMappingTest {

  public AbstractMappingTest() {
    super();
  }

  protected ByteArrayOutputStream marshalAndVerify(final Marshaller m, final Object target, String expected) throws MappingException {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(target, baos);
  
    final String encoded = new String(baos.toByteArray());
  
    Assert.assertEquals(expected, encoded);
    return baos;
  }

}