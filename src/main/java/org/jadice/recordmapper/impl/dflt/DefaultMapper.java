package org.jadice.recordmapper.impl.dflt;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.Marshaller;
import org.jadice.recordmapper.SeeAlso;
import org.jadice.recordmapper.Unmarshaller;
import org.jadice.recordmapper.impl.RecordMapping;

public class DefaultMapper {
  private final Map<Class<?>, RecordMapping> recordMappers = Collections.synchronizedMap(
      new HashMap<Class<?>, RecordMapping>());

  public DefaultMapper(Class<?>[] recordClasses) throws MappingException {
    final List<Class<?>> classesToMap = new LinkedList<Class<?>>(Arrays.asList(recordClasses));

    while (classesToMap.size() > 0) {
      final Class<?> c = classesToMap.remove(0);

      if (recordMappers.containsKey(c))
        continue;

      final RecordMapping rm = createRecordMapping(c);
      recordMappers.put(c, rm);

      classesToMap.addAll(rm.getReferencedClasses());
    }

    for (final RecordMapping drm : recordMappers.values())
      drm.postInit(recordMappers);
  }

  private RecordMapping createRecordMapping(Class<?> c) throws MappingException {
    for (final Annotation a : c.getAnnotations()) {
      if (a instanceof SeeAlso)
        continue;

      final String implName = a.annotationType().getPackage().getName() + ".impl." + a.annotationType().getSimpleName()
          + "Impl";

      try {
        final Class<?> mapping = Class.forName(implName);
        if (RecordMapping.class.isAssignableFrom(mapping)) {
          final RecordMapping rm = (RecordMapping) mapping.newInstance();
          rm.setRecordClass(c, a);
          return rm;
        }
      } catch (final ClassNotFoundException e) {
        throw new MappingException("Can't create mapping class", e);
      } catch (final InstantiationException e) {
        throw new MappingException("Can't create mapping class", e);
      } catch (final IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    throw new MappingException("Class " + c + " is not annotated with any record type");
  }

  public Marshaller createMarshaller() {
    return new DefaultMarshaller(this);
  }

  public Unmarshaller createUnmarshaller() {
    return new DefaultUnmarshaller(this);
  }

  public RecordMapping getRecordMapper(Class<? extends Object> c) {
    return recordMappers.get(c);
  }
}
