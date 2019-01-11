package org.jadice.recordmapper.cobol;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.jadice.recordmapper.MappingFactory;
import org.jadice.recordmapper.cobol.PolymorphicTestRecord.EmptyNestedElement;
import org.jadice.recordmapper.cobol.PolymorphicTestRecord.NameOnlyNestedElement;
import org.jadice.recordmapper.cobol.PolymorphicTestRecord.NameValueNestedElement;
import org.junit.Test;

public class PolymorphicMappingTest extends AbstractMappingTest {
  @Test
  public void testAnyMappingOMarshalling() throws Exception {
    final MappingFactory f = MappingFactory.create(PolymorphicTestRecord.class);

    final PolymorphicTestRecord a1 = new PolymorphicTestRecord();

    a1.type = 9;
    a1.nested = new PolymorphicTestRecord.EmptyNestedElement();

    marshalAndVerify(f.createMarshaller(), a1, "0");

    final PolymorphicTestRecord a2 = new PolymorphicTestRecord();
    a2.type = 9;
    final PolymorphicTestRecord.NameOnlyNestedElement n = new PolymorphicTestRecord.NameOnlyNestedElement();
    n.name = "foo";
    a2.nested = n;

    marshalAndVerify(f.createMarshaller(), a2, "1foo       ");

    final PolymorphicTestRecord a3 = new PolymorphicTestRecord();
    a3.type = 9;
    final PolymorphicTestRecord.NameValueNestedElement nv = new PolymorphicTestRecord.NameValueNestedElement();
    nv.name = "foo";
    nv.value = "bar";
    a3.nested = nv;

    marshalAndVerify(f.createMarshaller(), a3, "2foo       bar       ");
  }

  @Test
  public void testAnyMappingUnmarshalling() throws Exception {
    final MappingFactory f = MappingFactory.create(PolymorphicTestRecord.class);

    final PolymorphicTestRecord u1 = f.createUnmarshaller().unmarshal(
        PolymorphicTestRecord.class, new ByteArrayInputStream("0".getBytes()));
    assertEquals(0, u1.type);
    assertEquals(EmptyNestedElement.class, u1.nested.getClass());

    final PolymorphicTestRecord u2 = f.createUnmarshaller().unmarshal(
        PolymorphicTestRecord.class, new ByteArrayInputStream("1foo       ".getBytes()));
    assertEquals(1, u2.type);
    assertEquals(NameOnlyNestedElement.class, u2.nested.getClass());
    assertEquals("foo", ((NameOnlyNestedElement) u2.nested).name);

    final PolymorphicTestRecord u3 = f.createUnmarshaller().unmarshal(
        PolymorphicTestRecord.class, new ByteArrayInputStream("2foo       bar       ".getBytes()));
    assertEquals(2, u3.type);
    assertEquals(NameValueNestedElement.class, u3.nested.getClass());
    assertEquals("foo", ((NameValueNestedElement) u3.nested).name);
    assertEquals("bar", ((NameValueNestedElement) u3.nested).value);
  }
}
