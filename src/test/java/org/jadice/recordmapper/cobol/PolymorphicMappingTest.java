package org.jadice.recordmapper.cobol;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.jadice.recordmapper.MappingFactory;
import org.jadice.recordmapper.cobol.TPolymorphicOuterDiscriminator.EmptyNestedElement;
import org.jadice.recordmapper.cobol.TPolymorphicOuterDiscriminator.NameOnlyNestedElement;
import org.jadice.recordmapper.cobol.TPolymorphicOuterDiscriminator.NameValueNestedElement;
import org.junit.Test;

public class PolymorphicMappingTest extends AbstractMappingTest {
  @Test
  public void testAnyMappingOMarshalling() throws Exception {
    final MappingFactory f = MappingFactory.create(TPolymorphicOuterDiscriminator.class);

    final TPolymorphicOuterDiscriminator a1 = new TPolymorphicOuterDiscriminator();

    a1.type = 9;
    a1.nested = new TPolymorphicOuterDiscriminator.EmptyNestedElement();

    marshalAndVerify(f.createMarshaller(), a1, "0");

    final TPolymorphicOuterDiscriminator a2 = new TPolymorphicOuterDiscriminator();
    a2.type = 9;
    final TPolymorphicOuterDiscriminator.NameOnlyNestedElement n = new TPolymorphicOuterDiscriminator.NameOnlyNestedElement();
    n.name = "foo";
    a2.nested = n;

    marshalAndVerify(f.createMarshaller(), a2, "1foo       ");

    final TPolymorphicOuterDiscriminator a3 = new TPolymorphicOuterDiscriminator();
    a3.type = 9;
    final TPolymorphicOuterDiscriminator.NameValueNestedElement nv = new TPolymorphicOuterDiscriminator.NameValueNestedElement();
    nv.name = "foo";
    nv.value = "bar";
    a3.nested = nv;

    marshalAndVerify(f.createMarshaller(), a3, "2foo       bar       ");
  }

  @Test
  public void testAnyMappingUnmarshalling() throws Exception {
    final MappingFactory f = MappingFactory.create(TPolymorphicOuterDiscriminator.class);

    final TPolymorphicOuterDiscriminator u1 = f.createUnmarshaller().unmarshal(
        TPolymorphicOuterDiscriminator.class, new ByteArrayInputStream("0".getBytes()));
    assertEquals(0, u1.type);
    assertEquals(EmptyNestedElement.class, u1.nested.getClass());

    final TPolymorphicOuterDiscriminator u2 = f.createUnmarshaller().unmarshal(
        TPolymorphicOuterDiscriminator.class, new ByteArrayInputStream("1foo       ".getBytes()));
    assertEquals(1, u2.type);
    assertEquals(NameOnlyNestedElement.class, u2.nested.getClass());
    assertEquals("foo", ((NameOnlyNestedElement) u2.nested).name);

    final TPolymorphicOuterDiscriminator u3 = f.createUnmarshaller().unmarshal(
        TPolymorphicOuterDiscriminator.class, new ByteArrayInputStream("2foo       bar       ".getBytes()));
    assertEquals(2, u3.type);
    assertEquals(NameValueNestedElement.class, u3.nested.getClass());
    assertEquals("foo", ((NameValueNestedElement) u3.nested).name);
    assertEquals("bar", ((NameValueNestedElement) u3.nested).value);
  }
}
