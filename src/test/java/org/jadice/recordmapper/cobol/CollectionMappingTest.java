package org.jadice.recordmapper.cobol;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.MappingFactory;
import org.junit.Test;

public class CollectionMappingTest extends AbstractMappingTest {
  @Test
  public void testCollectionOfRecordsByBinaryCount() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyCollectionByBinaryCount.class);

    TPropertyCollectionByBinaryCount c = new TPropertyCollectionByBinaryCount();
    c.properties = new ArrayList<TPropertyBinaryLength>();

    fillProps(c.properties, TPropertyBinaryLength.class);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyCollectionByBinaryCount.class, new ByteArrayInputStream(bytes));

    assertEquals(5, c.count);

    verifyProps(c.properties);
  }

  @Test
  public void testCollectionOfRecordsByStringCount() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyCollectionByStringCount.class);

    TPropertyCollectionByStringCount c = new TPropertyCollectionByStringCount();
    c.properties = new ArrayList<TPropertyStringLength>();

    fillProps(c.properties, TPropertyStringLength.class);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyCollectionByStringCount.class, new ByteArrayInputStream(bytes));

    assertEquals(5, c.count);

    verifyProps(c.properties);
  }

  @Test
  public void testCollectionOfRecordsByBinarySize() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyCollectionByBinarySize.class);

    TPropertyCollectionByBinarySize c = new TPropertyCollectionByBinarySize();
    c.properties = new ArrayList<TPropertyBinaryLength>();

    fillProps(c.properties, TPropertyBinaryLength.class);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyCollectionByBinarySize.class, new ByteArrayInputStream(bytes));

    assertEquals(245, c.size);

    verifyProps(c.properties);
  }

  @Test
  public void testCollectionOfRecordsByStringSize() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyCollectionByStringSize.class);

    TPropertyCollectionByStringSize c = new TPropertyCollectionByStringSize();
    c.properties = new ArrayList<TPropertyStringLength>();

    fillProps(c.properties, TPropertyStringLength.class);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyCollectionByStringSize.class, new ByteArrayInputStream(bytes));

    assertEquals(245, c.size);

    verifyProps(c.properties);
  }

  @Test
  public void testArrayOfRecordsByBinaryCount() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyArrayByBinaryCount.class);

    TPropertyArrayByBinaryCount c = new TPropertyArrayByBinaryCount();
    final ArrayList<TPropertyBinaryLength> list = new ArrayList<TPropertyBinaryLength>();
    fillProps(list, TPropertyBinaryLength.class);
    c.properties = list.toArray(new TPropertyBinaryLength[5]);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyArrayByBinaryCount.class, new ByteArrayInputStream(bytes));

    assertEquals(5, c.count);

    verifyProps(Arrays.asList(c.properties));
  }

  @Test
  public void testArrayOfRecordsByStringCount() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyArrayByStringCount.class);

    TPropertyArrayByStringCount c = new TPropertyArrayByStringCount();
    final ArrayList<TPropertyStringLength> list = new ArrayList<TPropertyStringLength>();
    fillProps(list, TPropertyStringLength.class);
    c.properties = list.toArray(new TPropertyStringLength[5]);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyArrayByStringCount.class, new ByteArrayInputStream(bytes));

    assertEquals(5, c.count);

    verifyProps(Arrays.asList(c.properties));
  }

  @Test
  public void testArrayOfRecordsByBinarySize() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyArrayByBinarySize.class);

    TPropertyArrayByBinarySize c = new TPropertyArrayByBinarySize();
    final ArrayList<TPropertyBinaryLength> list = new ArrayList<TPropertyBinaryLength>();
    fillProps(list, TPropertyBinaryLength.class);
    c.properties = list.toArray(new TPropertyBinaryLength[5]);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyArrayByBinarySize.class, new ByteArrayInputStream(bytes));

    assertEquals(245, c.size);

    verifyProps(Arrays.asList(c.properties));
  }

  @Test
  public void testArrayOfRecordsByStringSize() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyArrayByStringSize.class);

    TPropertyArrayByStringSize c = new TPropertyArrayByStringSize();
    final ArrayList<TPropertyStringLength> list = new ArrayList<TPropertyStringLength>();
    fillProps(list, TPropertyStringLength.class);
    c.properties = list.toArray(new TPropertyStringLength[5]);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyArrayByStringSize.class, new ByteArrayInputStream(bytes));

    assertEquals(245, c.size);

    verifyProps(Arrays.asList(c.properties));
  }
  
  @Test
  public void testArrayOfRecordsByStreamLength() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyArrayUnbounded.class);

    TPropertyArrayUnbounded c = new TPropertyArrayUnbounded();
    final ArrayList<TPropertyBinaryLength> list = new ArrayList<TPropertyBinaryLength>();
    fillProps(list, TPropertyBinaryLength.class);
    c.properties = list.toArray(new TPropertyBinaryLength[5]);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyArrayUnbounded.class, new ByteArrayInputStream(bytes));

    assertEquals(5, c.properties.length);

    verifyProps(Arrays.asList(c.properties));
  }


  @Test
  public void testFixedLength() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyFixedLengthCollection.class);

    TPropertyFixedLengthCollection c = new TPropertyFixedLengthCollection();
    c.properties = new ArrayList<TPropertyBinaryLength>();

    fillProps(c.properties, TPropertyBinaryLength.class);
    fillProps(c.properties, TPropertyBinaryLength.class);

    try {
      mf.createMarshaller().marshal(c, new ByteArrayOutputStream());
      fail("Unexpected exception not thrown");
    } catch (final MappingException e) {
      // expected
    }

    c.properties = new ArrayList<TPropertyBinaryLength>();
    fillProps(c.properties, TPropertyBinaryLength.class);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyFixedLengthCollection.class, new ByteArrayInputStream(bytes));

    verifyProps(c.properties);
  }

  private void verifyProps(List<? extends TProperty> l) {
    for (int i = 0; i < 5; i++) {
      final String value = "Foo" + "ABCDEFGHI".substring(0, i);
      assertEquals("Foo" + i, l.get(i).getName());
      assertEquals(value, l.get(i).getValue());
    }
  }

  private <T extends TProperty> void fillProps(List<T> l, Class<T> c) throws Exception {
    for (int i = 0; i < 5; i++) {
      final String value = "Foo" + "ABCDEFGHI".substring(0, i);

      final T p = c.newInstance();
      p.setName("Foo" + i);
      p.setValue(value);

      l.add(p);
    }
  }
}
