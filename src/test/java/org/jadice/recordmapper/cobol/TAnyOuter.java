package org.jadice.recordmapper.cobol;

import java.lang.reflect.Field;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.SeeAlso;
import org.jadice.recordmapper.cobol.TAnyOuter.EmptyNestedElement;
import org.jadice.recordmapper.cobol.TAnyOuter.NameOnlyNestedElement;
import org.jadice.recordmapper.cobol.TAnyOuter.NameValueNestedElement;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.UnmarshalContext;

@CBLRecord
@SeeAlso({
    NameValueNestedElement.class, NameOnlyNestedElement.class, EmptyNestedElement.class
})
public class TAnyOuter {
  public interface SomeNestedElement {

  }

  @CBLRecord
  public static class NameValueNestedElement implements SomeNestedElement {
    @CBLString(10)
    public String name;

    @CBLString(10)
    public String value;
  }

  @CBLRecord
  public static class NameOnlyNestedElement implements SomeNestedElement {
    @CBLString(10)
    public String name;
  }

  @CBLRecord
  public static class EmptyNestedElement implements SomeNestedElement {
  }

  public static class NestedDiscriminator implements Discriminator {
    @Override
    public void init(Class<?> recordClass, Field field) throws MappingException {
      // nothing to do
    }

    @Override
    public Class<?> getComponentType(MappingContext ctx) throws MappingException {
      switch (((TAnyOuter) ctx.getRecord()).type){
        case 0 :
          return EmptyNestedElement.class;
        case 1 :
          return NameOnlyNestedElement.class;
        case 2 :
          return NameValueNestedElement.class;
      }
      throw new IllegalArgumentException();
    }

    @Override
    public void beforeMarshal(MarshalContext ctx) throws MappingException {
      final TAnyOuter record = (TAnyOuter) ctx.getRecord();
      if (record.nested == null)
        throw new NullPointerException();
      if (record.nested instanceof EmptyNestedElement)
        record.type = 0;
      else if (record.nested instanceof NameOnlyNestedElement)
        record.type = 1;
      else if (record.nested instanceof NameValueNestedElement)
        record.type = 2;
      else
        throw new IllegalArgumentException();
    }

    @Override
    public void afterUnmarshal(UnmarshalContext ctx) throws MappingException {
      // nothing to do
    }
  }

  @CBLNumericString(1)
  public int type;

  @CBLAnyNested(NestedDiscriminator.class)
  public SomeNestedElement nested;
}
