package org.jadice.recordmapper.cobol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jadice.recordmapper.BaseRecordAttributes;
import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.MappingFactory;
import org.jadice.recordmapper.Marshaller;
import org.jadice.recordmapper.Unmarshaller;
import org.jadice.recordmapper.cobol.TAnyOuter.EmptyNestedElement;
import org.jadice.recordmapper.cobol.TAnyOuter.NameOnlyNestedElement;
import org.jadice.recordmapper.cobol.TAnyOuter.NameValueNestedElement;
import org.junit.Assert;
import org.junit.Test;

public class TestCBLMapping {
  @Test
  public void testPropertyBinaryLength() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyBinaryLength.class);

    for (int i = 0; i < 5; i++) {
      final String value = "Foo" + "ABCDEFGHI".substring(0, i);

      TPropertyBinaryLength p = new TPropertyBinaryLength();
      p.name = "Foo";
      p.value = value;

      final ByteArrayOutputStream os = new ByteArrayOutputStream();
      mf.createMarshaller().marshal(p, os);

      final byte[] bytes = os.toByteArray();

      p = mf.createUnmarshaller().unmarshal(TPropertyBinaryLength.class, new ByteArrayInputStream(bytes));

      assertEquals("Foo", p.name);
      assertEquals(value, p.value);
    }
  }
  
  @Test
  public void testPropertyStringLength() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyStringLength.class);
    
    for (int i = 0; i < 5; i++) {
      final String value = "Foo" + "ABCDEFGHI".substring(0, i);
      
      TPropertyStringLength p = new TPropertyStringLength();
      p.name = "Foo";
      p.value = value;
      
      final ByteArrayOutputStream os = new ByteArrayOutputStream();
      mf.createMarshaller().marshal(p, os);
      
      final byte[] bytes = os.toByteArray();
      
      p = mf.createUnmarshaller().unmarshal(TPropertyStringLength.class, new ByteArrayInputStream(bytes));
      
      assertEquals("Foo", p.name);
      assertEquals(value, p.value);
    }
  }

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

  private void verifyProps(final List<? extends TProperty> l) {
    for (int i = 0; i < 5; i++) {
      final String value = "Foo" + "ABCDEFGHI".substring(0, i);
      assertEquals("Foo" + i, l.get(i).getName());
      assertEquals(value, l.get(i).getValue());
    }
  }

  private <T extends TProperty> void fillProps(final List<T> l, final Class<T> c) throws Exception {
    for (int i = 0; i < 5; i++) {
      final String value = "Foo" + "ABCDEFGHI".substring(0, i);

      final T p = c.newInstance();
      p.setName("Foo" + i);
      p.setValue(value);

      l.add(p);
    }
  }

  @Test
  public void testNested() throws Exception {
    final MappingFactory mf = MappingFactory.create(TNestedOuter.class);

    TNestedOuter c = new TNestedOuter();

    c.i1.p1.name = "Foo1";
    c.i1.p1.value = "Bar1";
    c.i1.p2.name = "Foo2";
    c.i1.p2.value = "Bar2";
    c.i2.p1.name = "Foo3";
    c.i2.p1.value = "Bar3";
    c.i2.p2.name = "Foo4";
    c.i2.p2.value = "Bar4";

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TNestedOuter.class, new ByteArrayInputStream(bytes));

    assertEquals("Foo1", c.i1.p1.name);
    assertEquals("Bar1", c.i1.p1.value);
    assertEquals("Foo2", c.i1.p2.name);
    assertEquals("Bar2", c.i1.p2.value);
    assertEquals("Foo3", c.i2.p1.name);
    assertEquals("Bar3", c.i2.p1.value);
    assertEquals("Foo4", c.i2.p2.name);
    assertEquals("Bar4", c.i2.p2.value);
  }

  // ******************************************************
  public enum MyEnumWithTooLongAName {
    FOO, BAR, IXXI
  }

  @CBLRecord
  public static class FailingClass1 {
    @CBLEnum(3)
    MyEnumWithTooLongAName foo;
  }

  @Test
  public void testFailingEnum1() throws Exception {
    try {
      MappingFactory.create(FailingClass1.class);
    } catch (final MappingException e) {
      Assert.assertTrue(e.getCause().getMessage().contains("The enum value IXXI is longer than"));
    }
  }

  // ******************************************************
  public enum MyEnumWithoutCBLValues {
    FOO, BAR, BAZ, A, BB
  }

  @CBLRecord
  public static class EnumClass1 {
    @CBLEnum(3)
    MyEnumWithoutCBLValues foo;
  }

  @Test
  public void testEnumWithoutCBLValues() throws Exception {
    final MappingFactory f = MappingFactory.create(EnumClass1.class);
    final Marshaller m = f.createMarshaller();
    final Unmarshaller u = f.createUnmarshaller();

    for (final MyEnumWithoutCBLValues e : MyEnumWithoutCBLValues.values()) {
      final EnumClass1 ec = new EnumClass1();
      ec.foo = e;

      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      m.marshal(ec, baos);

      final EnumClass1 ec1out = u.unmarshal(EnumClass1.class, new ByteArrayInputStream(baos.toByteArray()));

      Assert.assertEquals(e, ec1out.foo);
    }
  }

  @CBLRecord
  public static class EnumClass11 {
    @CBLEnum(value = 3, nullValue = "")
    MyEnumWithoutCBLValues foo;
  }

  @CBLRecord
  public static class EnumClass12 {
    @CBLEnum(value = 3, nullValue = "NNN")
    MyEnumWithoutCBLValues foo;
  }

  @Test
  public void testNullHandlingWithoutCBLValues() throws Exception {
    final MappingFactory f = MappingFactory.create(EnumClass1.class, EnumClass11.class, EnumClass12.class);
    final Marshaller m = f.createMarshaller();
    final Unmarshaller u = f.createUnmarshaller();
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    // should fail: null not allowed
    try {
      m.marshal(new EnumClass1(), baos);
    } catch (final NullPointerException e) {
      Assert.assertTrue(e.getMessage().contains("must not contain a null value"));
    }

    final EnumClass11 ec11 = new EnumClass11();
    m.marshal(ec11, baos);

    final EnumClass11 ec1out = u.unmarshal(EnumClass11.class, new ByteArrayInputStream(baos.toByteArray()));

    Assert.assertNull(ec1out.foo);

    final EnumClass12 ec = new EnumClass12();

    baos.reset();
    m.marshal(ec, baos);

    final EnumClass12 ec12out = u.unmarshal(EnumClass12.class, new ByteArrayInputStream(baos.toByteArray()));

    Assert.assertNull(ec12out.foo);
  }

  @CBLRecord
  public static class EnumClass13 {
    @CBLEnum(value = 3, unknownValue = "BAZ")
    MyEnumWithoutCBLValues foo;
  }

  @Test
  public void testUnknownValueWithoutCBLValues() throws Exception {
    final MappingFactory f = MappingFactory.create(EnumClass13.class);
    final Unmarshaller u = f.createUnmarshaller();
    u.getRecordAttributes(BaseRecordAttributes.class).setEncoding("ascii");

    final EnumClass13 ec1out = u.unmarshal(EnumClass13.class, new ByteArrayInputStream("XYZ".getBytes()));

    Assert.assertEquals(MyEnumWithoutCBLValues.BAZ, ec1out.foo);
  }

  // ******************************************************
  public enum MyEnumWithCBLValues {
    @CBLEnumValue(value = "v_foo", aliases = {"v_as1", "v_as2"}) FOO,
    @CBLEnumValue("v_bar") BAR,
    @CBLEnumValue("v_baz") BAZ,
    @CBLEnumValue("v_a") A,
    @CBLEnumValue("v_bb") BB;
  }

  @CBLRecord
  public static class EnumClass2 {
    @CBLEnum(5)
    MyEnumWithCBLValues foo;
  }

  @Test
  public void testEnumWithCBLValues() throws Exception {
    final MappingFactory f = MappingFactory.create(EnumClass2.class);
    final Marshaller m = f.createMarshaller();
    m.getRecordAttributes(BaseRecordAttributes.class).setEncoding("ascii");

    final Unmarshaller u = f.createUnmarshaller();
    u.getRecordAttributes(BaseRecordAttributes.class).setEncoding("ascii");

    for (final MyEnumWithCBLValues e : MyEnumWithCBLValues.values()) {
      final EnumClass2 ec = new EnumClass2();
      ec.foo = e;

      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      m.marshal(ec, baos);

      final String msg = new String(baos.toByteArray(), "ascii");
      Assert.assertTrue(msg.contains("v_" + e.name().toLowerCase()));

      final EnumClass2 ecout = u.unmarshal(EnumClass2.class, new ByteArrayInputStream(baos.toByteArray()));

      Assert.assertEquals(e, ecout.foo);
    }
  }

  @CBLRecord
  public static class EnumClass21 {
    @CBLEnum(value = 5, nullValue = "")
    MyEnumWithCBLValues foo;
  }

  @CBLRecord
  public static class EnumClass22 {
    @CBLEnum(value = 5, nullValue = "NNN")
    MyEnumWithCBLValues foo;
  }

  @Test
  public void testNullHandlingWithCBLValues() throws Exception {
    final MappingFactory f = MappingFactory.create(EnumClass2.class, EnumClass21.class, EnumClass22.class);
    final Marshaller m = f.createMarshaller();
    final Unmarshaller u = f.createUnmarshaller();
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    // should fail: null not allowed
    try {
      m.marshal(new EnumClass2(), baos);
    } catch (final NullPointerException e) {
      Assert.assertTrue(e.getMessage().contains("must not contain a null value"));
    }

    final EnumClass21 ec21 = new EnumClass21();
    m.marshal(ec21, baos);

    final EnumClass21 ec1out = u.unmarshal(EnumClass21.class, new ByteArrayInputStream(baos.toByteArray()));

    Assert.assertNull(ec1out.foo);

    final EnumClass22 ec = new EnumClass22();

    baos.reset();
    m.marshal(ec, baos);

    final EnumClass22 ec22out = u.unmarshal(EnumClass22.class, new ByteArrayInputStream(baos.toByteArray()));

    Assert.assertNull(ec22out.foo);
  }

  @CBLRecord
  public static class EnumClass23 {
    @CBLEnum(value = 5, unknownValue = "v_baz")
    MyEnumWithCBLValues foo;
  }

  @Test
  public void testUnknownValueWithCBLValues() throws Exception {
    final MappingFactory f = MappingFactory.create(EnumClass23.class);
    final Unmarshaller u = f.createUnmarshaller();
    u.getRecordAttributes(BaseRecordAttributes.class).setEncoding("ascii");

    final EnumClass23 ec1out = u.unmarshal(EnumClass23.class, new ByteArrayInputStream("XYZ  ".getBytes()));

    Assert.assertEquals(MyEnumWithCBLValues.BAZ, ec1out.foo);
  }
  
  @Test
  public void testAliasesWithCBLValues() throws Exception {
    final MappingFactory f = MappingFactory.create(EnumClass23.class);
    final Unmarshaller u = f.createUnmarshaller();
    u.getRecordAttributes(BaseRecordAttributes.class).setEncoding("ascii");
    
    Assert.assertEquals(MyEnumWithCBLValues.FOO, u.unmarshal(EnumClass23.class, new ByteArrayInputStream("v_as1".getBytes())).foo);
    Assert.assertEquals(MyEnumWithCBLValues.FOO, u.unmarshal(EnumClass23.class, new ByteArrayInputStream("v_as2".getBytes())).foo);
  }

  // *****************************************
  @CBLRecord
  public static class MyTestClass1 {
    @CBLDate(value = "yyyy-MM-dd", nullDate = "2001-01-01")
    Date date;

    @CBLDate("HH:mm:ss.SSS")
    Date time;

    @CBLDate(value = "yyyy-MM-dd HH:mm:ss.SSS", nullDate = "NOW")
    Date timestamp;
  }

  @Test
  public void testCalendarNotNull() throws Exception {
    final MappingFactory f = MappingFactory.create(MyTestClass1.class);
    final Marshaller m = f.createMarshaller();
    final Unmarshaller u = f.createUnmarshaller();
    u.getRecordAttributes(BaseRecordAttributes.class).setEncoding("ascii");

    final MyTestClass1 tc = new MyTestClass1();

    final GregorianCalendar c = new GregorianCalendar(2013, 0, 1, 10, 11, 12);
    c.set(GregorianCalendar.MILLISECOND, 13);

    tc.date = c.getTime();
    tc.time = c.getTime();
    tc.timestamp = c.getTime();

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(tc, baos);

    final String encoded = new String(baos.toByteArray());

    Assert.assertEquals("2013-01-0110:11:12.0132013-01-01 10:11:12.013", encoded);

    final MyTestClass1 tcu = u.unmarshal(MyTestClass1.class, new ByteArrayInputStream(baos.toByteArray()));

    Assert.assertEquals(new GregorianCalendar(2013, 0, 1).getTime(), tcu.date);
    final GregorianCalendar c2 = new GregorianCalendar(1970, 0, 1, 10, 11, 12);
    c2.set(GregorianCalendar.MILLISECOND, 13);
    Assert.assertEquals(c2.getTime(), tcu.time);
    Assert.assertEquals(c.getTime(), tcu.timestamp);
  }

  @Test
  public void testCalendarNull() throws Exception {
    final MappingFactory f = MappingFactory.create(MyTestClass1.class);
    final Marshaller m = f.createMarshaller();
    final Unmarshaller u = f.createUnmarshaller();
    u.getRecordAttributes(BaseRecordAttributes.class).setEncoding("ascii");

    final MyTestClass1 tc = new MyTestClass1();

    try {
      m.marshal(tc, new ByteArrayOutputStream());
    } catch (final MappingException e) {
      Assert.assertTrue(e.getMessage().contains("TestCBLMapping$MyTestClass1.time contains an invalid nullDate"));
    }

    tc.time = new Date();

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(tc, baos);

    final MyTestClass1 tcu = u.unmarshal(MyTestClass1.class, new ByteArrayInputStream(baos.toByteArray()));

    Assert.assertNotNull(tcu.date);
    Assert.assertNotNull(tcu.time);
    Assert.assertNotNull(tcu.timestamp);

    Assert.assertEquals(new GregorianCalendar(2001, 0, 1).getTime(), tcu.date);
  }

  // *****************************************
  @CBLRecord
  public static class MyTestClass2 {
    @CBLNumericString(3)
    Byte myByte;

    @CBLNumericString(5)
    Short myShort;

    @CBLNumericString(10)
    Integer myInteger;

    @CBLNumericString(20)
    Long myLong;

    @CBLNumericString(20)
    Float myFloat;

    @CBLNumericString(23)
    Double myDouble;

    @CBLNumericString(3)
    byte mybyte;

    @CBLNumericString(5)
    short myshort;

    @CBLNumericString(10)
    int myint;

    @CBLNumericString(20)
    long mylong;

    @CBLNumericString(20)
    float myfloat;

    @CBLNumericString(23)
    double mydouble;
  }

  @Test
  public void testNumericStrings() throws Exception {
    final MappingFactory f = MappingFactory.create(MyTestClass2.class);
    final Marshaller m = f.createMarshaller();
    final Unmarshaller u = f.createUnmarshaller();
    u.getRecordAttributes(BaseRecordAttributes.class).setEncoding("ascii");

    final MyTestClass2 tc = new MyTestClass2();
    tc.mybyte = Byte.MAX_VALUE - 1;
    tc.myByte = Byte.MAX_VALUE - 1;

    tc.myshort = Short.MAX_VALUE - 1;
    tc.myShort = Short.MAX_VALUE - 2;

    tc.myint = Integer.MAX_VALUE - 1;
    tc.myInteger = Integer.MAX_VALUE - 2;

    tc.mylong = Long.MAX_VALUE - 1;
    tc.myLong = Long.MAX_VALUE - 2;

    tc.mylong = Long.MAX_VALUE - 1;
    tc.myLong = Long.MAX_VALUE - 2;

    tc.myfloat = Float.MAX_VALUE - 1;
    tc.myFloat = Float.MAX_VALUE - 2;

    tc.mydouble = Double.MAX_VALUE - 1;
    tc.myDouble = Double.MAX_VALUE - 2;

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(tc, baos);

    final String encoded = new String(baos.toByteArray());

    Assert.assertEquals(
        "12632765214748364509223372036854775805000000003.4028235E3801.7976931348623157E30812632766214748364609223372036854775806000000003.4028235E3801.7976931348623157E308",
        encoded);

    final MyTestClass2 tcu = u.unmarshal(MyTestClass2.class, new ByteArrayInputStream(baos.toByteArray()));

    Assert.assertEquals(tcu.mybyte, Byte.MAX_VALUE - 1);
    Assert.assertEquals(tcu.myByte, new Byte((byte) (Byte.MAX_VALUE - 1)));

    Assert.assertEquals(tcu.myshort, Short.MAX_VALUE - 1);
    Assert.assertEquals(tcu.myShort, new Short((short) (Short.MAX_VALUE - 2)));

    Assert.assertEquals(tcu.myint, Integer.MAX_VALUE - 1);
    Assert.assertEquals(tcu.myInteger, new Integer(Integer.MAX_VALUE - 2));

    Assert.assertEquals(tcu.mylong, Long.MAX_VALUE - 1);
    Assert.assertEquals(tcu.myLong, new Long(Long.MAX_VALUE - 2));

    Assert.assertEquals(tcu.myfloat, Float.MAX_VALUE - 1, .0001);
    Assert.assertEquals(tcu.myFloat, new Float(Float.MAX_VALUE - 2));

    Assert.assertEquals(tcu.mydouble, Double.MAX_VALUE - 1, .0001);
    Assert.assertEquals(tcu.myDouble, new Double(Double.MAX_VALUE - 2));
  }

  @Test
  public void testNumbericStringsNull() throws Exception {
    final MappingFactory f = MappingFactory.create(MyTestClass2.class);
    final Marshaller m = f.createMarshaller();
    final Unmarshaller u = f.createUnmarshaller();
    u.getRecordAttributes(BaseRecordAttributes.class).setEncoding("ascii");

    final MyTestClass2 tc = new MyTestClass2();

    final ByteArrayOutputStream baos = marshalAndVerify(m, tc,
        "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.0000000000000000000000.0");

    final MyTestClass2 tcu = u.unmarshal(MyTestClass2.class, new ByteArrayInputStream(baos.toByteArray()));

    Assert.assertEquals(tcu.mybyte, (byte) 0);
    Assert.assertEquals(tcu.myByte, new Byte((byte) 0));

    Assert.assertEquals(tcu.myshort, (short) 0);
    Assert.assertEquals(tcu.myShort, new Short((short) 0));

    Assert.assertEquals(tcu.myint, 0);
    Assert.assertEquals(tcu.myInteger, new Integer(0));

    Assert.assertEquals(tcu.mylong, 0L);
    Assert.assertEquals(tcu.myLong, new Long(0));

    Assert.assertEquals(tcu.myfloat, 0f, .0001);
    Assert.assertEquals(tcu.myFloat, new Float(0));

    Assert.assertEquals(tcu.mydouble, 0d, .0001);
    Assert.assertEquals(tcu.myDouble, new Double(0));
  }

  ByteArrayOutputStream marshalAndVerify(final Marshaller m, final Object target, final String expected)
      throws MappingException {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(target, baos);

    final String encoded = new String(baos.toByteArray());

    Assert.assertEquals(expected, encoded);
    return baos;
  }

  // *****************************************
  @CBLRecord
  public static class MyTestClass4 {
    @CBLNumeric(1)
    Byte myByte;

    @CBLNumeric(2)
    Short myShort;

    @CBLNumeric(4)
    Integer myInteger;

    @CBLNumeric(value=8, signed=true)
    Long myLong;

    @CBLNumeric(1)
    byte dummy;
    
    @CBLNumeric(1)
    byte mybyte;

    @CBLNumeric(2)
    short myshort;

    @CBLNumeric(4)
    int myint;

    @CBLNumeric(value=8, signed=true)
    long mylong;
  }

  @Test
  public void testBinaryNumeric() throws Exception {
    final MappingFactory f = MappingFactory.create(MyTestClass4.class);
    final Marshaller m = f.createMarshaller();
    final Unmarshaller u = f.createUnmarshaller();
    u.getRecordAttributes(BaseRecordAttributes.class).setEncoding("ascii");

    final MyTestClass4 tc = new MyTestClass4();
    tc.mybyte = Byte.MAX_VALUE - 1;
    tc.myByte = Byte.MAX_VALUE - 1;

    tc.myshort = Short.MAX_VALUE - 1;
    tc.myShort = Short.MAX_VALUE - 1;

    tc.myint = Integer.MAX_VALUE - 1;
    tc.myInteger = Integer.MAX_VALUE - 1;

    tc.mylong = Long.MAX_VALUE - 1;
    tc.myLong = Long.MAX_VALUE - 1;

    tc.mylong = Long.MAX_VALUE - 1;
    tc.myLong = Long.MAX_VALUE - 1;

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(tc, baos);


    Assert.assertEquals(
        "7E7FFE7FFFFFFE7FFFFFFFFFFFFFFE007E7FFE7FFFFFFE7FFFFFFFFFFFFFFE",
        toHexString(baos.toByteArray()));

    final MyTestClass4 tcu = u.unmarshal(MyTestClass4.class, new ByteArrayInputStream(baos.toByteArray()));

    Assert.assertEquals(tcu.mybyte, Byte.MAX_VALUE - 1);
    Assert.assertEquals(tcu.myByte, new Byte((byte) (Byte.MAX_VALUE - 1)));

    Assert.assertEquals(tcu.myshort, Short.MAX_VALUE - 1);
    Assert.assertEquals(tcu.myShort, new Short((short) (Short.MAX_VALUE - 1)));

    Assert.assertEquals(tcu.myint, Integer.MAX_VALUE - 1);
    Assert.assertEquals(tcu.myInteger, new Integer(Integer.MAX_VALUE - 1));

    Assert.assertEquals(tcu.mylong, Long.MAX_VALUE - 1);
    Assert.assertEquals(tcu.myLong, new Long(Long.MAX_VALUE - 1));
  }

  private static String toHexString(final byte[] b) {
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < b.length; i++) {
      String s = Integer.toHexString(b[i] & 0xff).toUpperCase();
      if (s.length() == 1) {
        buf.append('0');
      }
      buf.append(s);
    }
    return buf.toString();
  }

  // *****************************************
  @CBLRecord
  public static class MyTestClass3 {
    @CBLNumericString(3)
    int foo;
  }

  @Test
  public void testNumericStringOverflow() throws Exception {
    final MappingFactory f = MappingFactory.create(MyTestClass3.class);
    final Marshaller m = f.createMarshaller();

    final MyTestClass3 tc = new MyTestClass3();
    tc.foo = 4711;

    try {
      m.marshal(tc, new ByteArrayOutputStream());
      Assert.fail("Expected exception not thrown");
    } catch (final MappingException e) {
      Assert.assertTrue(e.getMessage().contains(
          "Length of value 4711 for field int org.jadice.recordmapper.cobol.TestCBLMapping$MyTestClass3.foo exceeds allowed length(3)"));
    }
  }

  @Test
  public void testAnyMappingMarshalling() throws Exception {
    final MappingFactory f = MappingFactory.create(TAnyOuter.class);

    final TAnyOuter a1 = new TAnyOuter();

    a1.type = 9;
    a1.nested = new TAnyOuter.EmptyNestedElement();

    marshalAndVerify(f.createMarshaller(), a1, "0");

    final TAnyOuter a2 = new TAnyOuter();
    a2.type = 9;
    final TAnyOuter.NameOnlyNestedElement n = new TAnyOuter.NameOnlyNestedElement();
    n.name = "foo";
    a2.nested = n;

    marshalAndVerify(f.createMarshaller(), a2, "1foo       ");

    final TAnyOuter a3 = new TAnyOuter();
    a3.type = 9;
    final TAnyOuter.NameValueNestedElement nv = new TAnyOuter.NameValueNestedElement();
    nv.name = "foo";
    nv.value = "bar";
    a3.nested = nv;

    marshalAndVerify(f.createMarshaller(), a3, "2foo       bar       ");
  }

  @Test
  public void testAnyMappingUnmarshalling() throws Exception {
    final MappingFactory f = MappingFactory.create(TAnyOuter.class);

    final TAnyOuter u1 = f.createUnmarshaller().unmarshal(TAnyOuter.class, new ByteArrayInputStream("0".getBytes()));
    assertEquals(0, u1.type);
    assertEquals(EmptyNestedElement.class, u1.nested.getClass());

    final TAnyOuter u2 = f.createUnmarshaller().unmarshal(TAnyOuter.class,
        new ByteArrayInputStream("1foo       ".getBytes()));
    assertEquals(1, u2.type);
    assertEquals(NameOnlyNestedElement.class, u2.nested.getClass());
    assertEquals("foo", ((NameOnlyNestedElement) u2.nested).name);

    final TAnyOuter u3 = f.createUnmarshaller().unmarshal(TAnyOuter.class,
        new ByteArrayInputStream("2foo       bar       ".getBytes()));
    assertEquals(2, u3.type);
    assertEquals(NameValueNestedElement.class, u3.nested.getClass());
    assertEquals("foo", ((NameValueNestedElement) u3.nested).name);
    assertEquals("bar", ((NameValueNestedElement) u3.nested).value);
  }
}
