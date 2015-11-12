package org.jadice.recordmapper.cobol;

import static org.junit.Assert.*;

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
import org.junit.Assert;
import org.junit.Test;

public class TestCBLMapping {
  @Test
  public void testFlatRecord() throws Exception {
    final MappingFactory mf = MappingFactory.create(TProperty.class);

    for (int i = 0; i < 5; i++) {
      final String value = "Foo" + "ABCDEFGHI".substring(0, i);

      TProperty p = new TProperty();
      p.name = "Foo";
      p.value = value;

      final ByteArrayOutputStream os = new ByteArrayOutputStream();
      mf.createMarshaller().marshal(p, os);

      final byte[] bytes = os.toByteArray();

      p = mf.createUnmarshaller().unmarshal(TProperty.class, new ByteArrayInputStream(bytes));

      assertEquals("Foo", p.name);
      assertEquals(value, p.value);
    }
  }

  @Test
  public void testCollectionOfRecordsByCount() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyCollectionByCount.class);

    TPropertyCollectionByCount c = new TPropertyCollectionByCount();
    c.properties = new ArrayList<TProperty>();

    fillProps(c.properties);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyCollectionByCount.class, new ByteArrayInputStream(bytes));

    assertEquals(5, c.count);

    verifyProps(c.properties);
  }

  @Test
  public void testCollectionOfRecordsBySize() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyCollectionBySize.class);

    TPropertyCollectionBySize c = new TPropertyCollectionBySize();
    c.properties = new ArrayList<TProperty>();

    fillProps(c.properties);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyCollectionBySize.class, new ByteArrayInputStream(bytes));

    assertEquals(245, c.size);

    verifyProps(c.properties);
  }

  @Test
  public void testArrayOfRecordsByCount() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyArrayByCount.class);

    TPropertyArrayByCount c = new TPropertyArrayByCount();
    final ArrayList<TProperty> list = new ArrayList<TProperty>();
    fillProps(list);
    c.properties = list.toArray(new TProperty[5]);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyArrayByCount.class, new ByteArrayInputStream(bytes));

    assertEquals(5, c.count);

    verifyProps(Arrays.asList(c.properties));
  }

  @Test
  public void testArrayOfRecordsBySize() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyArrayBySize.class);

    TPropertyArrayBySize c = new TPropertyArrayBySize();
    final ArrayList<TProperty> list = new ArrayList<TProperty>();
    fillProps(list);
    c.properties = list.toArray(new TProperty[5]);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyArrayBySize.class, new ByteArrayInputStream(bytes));

    assertEquals(245, c.size);

    verifyProps(Arrays.asList(c.properties));
  }

  @Test
  public void testFixedLength() throws Exception {
    final MappingFactory mf = MappingFactory.create(TPropertyFixedLengthCollection.class);

    TPropertyFixedLengthCollection c = new TPropertyFixedLengthCollection();
    c.properties = new ArrayList<TProperty>();

    fillProps(c.properties);
    fillProps(c.properties);

    try {
      mf.createMarshaller().marshal(c, new ByteArrayOutputStream());
      fail("Unexpected exception not thrown");
    } catch (final MappingException e) {
      // expected
    }

    c.properties = new ArrayList<TProperty>();
    fillProps(c.properties);

    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    mf.createMarshaller().marshal(c, os);

    final byte[] bytes = os.toByteArray();

    c = mf.createUnmarshaller().unmarshal(TPropertyFixedLengthCollection.class, new ByteArrayInputStream(bytes));

    verifyProps(c.properties);
  }

  private void verifyProps(List<TProperty> l) {
    for (int i = 0; i < 5; i++) {
      final String value = "Foo" + "ABCDEFGHI".substring(0, i);
      assertEquals("Foo" + i, l.get(i).name);
      assertEquals(value, l.get(i).value);
    }
  }

  private void fillProps(List<TProperty> l) {
    for (int i = 0; i < 5; i++) {
      final String value = "Foo" + "ABCDEFGHI".substring(0, i);

      final TProperty p = new TProperty();
      p.name = "Foo" + i;
      p.value = value;

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
    @CBLEnumValue("v_foo")
    FOO, @CBLEnumValue("v_bar")
    BAR, @CBLEnumValue("v_baz")
    BAZ, @CBLEnumValue("v_a")
    A, @CBLEnumValue("v_bb")
    BB;
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
  public void testNumbericStrings() throws Exception {
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

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(tc, baos);

    final String encoded = new String(baos.toByteArray());

    Assert.assertEquals(
        "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.0000000000000000000000.0",
        encoded);

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
}
