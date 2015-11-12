package org.jadice.recordmapper.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jadice.recordmapper.AfterUnmarshal;
import org.jadice.recordmapper.BeforeMarshal;
import org.jadice.recordmapper.Mapping;
import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.SeeAlso;

public abstract class RecordMapping extends Mapping {
  protected Map<Class<?>, RecordMapping> recordMappings;

	protected List<FieldMapping> fieldMappings = new ArrayList<FieldMapping>();
	protected Map<String, FieldMapping> fieldMappingsByName = new HashMap<String, FieldMapping>();

	private final List<Method> beforeMarshal = new ArrayList<Method>(0);
	private final List<Method> afterUnmarshal = new ArrayList<Method>(0);

  private List<Class<?>> seeAlso;

  protected Class<?> recordClass;

  public Collection<? extends Class<?>> getReferencedClasses() {
    final List<Class<?>> referencedClasses = new ArrayList<Class<?>>();
		if (null != seeAlso)
			referencedClasses.addAll(seeAlso);

		for (final FieldMapping fm : fieldMappings)
			referencedClasses.addAll(fm.getReferencedClasses());

		return referencedClasses;
	}

  public void postInit(Map<Class<?>, RecordMapping> mappings)
			throws MappingException {
		recordMappings = mappings;

		for (final FieldMapping fm : fieldMappings)
			fm.postInit();
	}

  public void setRecordClass(Class<?> c, Annotation recordAnnotation)
			throws MappingException {
		recordClass = c;

		assertCompatibility(c, recordAnnotation);

		final List<Field> fields = new ArrayList<Field>();
		gatherFields(c, fields);

		for (final Field f : fields) {
			for (final Annotation a : f.getAnnotations()) {
				// don't try to create field mappings for auxiliary annotations
				if (null != a.annotationType().getAnnotation(Auxiliary.class))
					continue;

				final String implName = a.annotationType().getPackage().getName() + ".impl."
						+ a.annotationType().getSimpleName() + "Impl";

				try {
          final Class<?> mapping = Class.forName(implName);
					if (FieldMapping.class.isAssignableFrom(mapping)) {
						final FieldMapping fm = (FieldMapping) mapping.newInstance();

						if (fieldMappingsByName.containsKey(f.getName()))
							throw new MappingException(this, "Already have a field mapping for "
									+ f);

						assertCompatibility(c, f, a, fm);

						f.setAccessible(true);
						fm.init(this, f, a);

						fieldMappings.add(fm);
						fieldMappingsByName.put(f.getName(), fm);
					}
				} catch (final Exception e) {
					throw new MappingException(this, "Can't create mapping class", e);
				}
			}
		}

		// look for callbacks
		for (final Method m : c.getDeclaredMethods()) {
			if (m.getAnnotation(BeforeMarshal.class) != null) {
				if (m.getParameterTypes().length != 0)
					throw new MappingException(
							this, "@BeforeMarshal callback methods must not take parameters");
				m.setAccessible(true);
				beforeMarshal.add(m);
			}

			if (m.getAnnotation(AfterUnmarshal.class) != null) {
				if (m.getParameterTypes().length != 0)
					throw new MappingException(
							this, "@AfterUnmarshal callback methods must not take parameters");
				m.setAccessible(true);
				afterUnmarshal.add(m);
			}
		}

		// look for @SeeAlso
		final SeeAlso a = c.getAnnotation(SeeAlso.class);
		if (null != a)
			seeAlso = Arrays.asList(a.value());
	}

  private void gatherFields(Class<?> c, List<Field> fields) {
		if (null != c.getSuperclass())
			gatherFields(c.getSuperclass(), fields);

		for (final Field f : c.getDeclaredFields())
			fields.add(f);
	}

  protected abstract void assertCompatibility(Class<?> c, Field f,
			Annotation fieldAnnotation, FieldMapping mapping) throws MappingException;

  protected abstract void assertCompatibility(Class<?> c,
			Annotation recordAnnotation) throws MappingException;

	public void marshal(Object record, MarshalContext mc) throws MappingException {
		for (final Method m : beforeMarshal)
			try {
				m.invoke(record, (Object[]) null);
			} catch (final Exception e) {
				throw new MappingException(
						this, "Can't invoke @BeforeMarshal callback method", e);
			}

		for (final FieldMapping fm : fieldMappings)
			fm.beforeMarshal(mc);

		for (final FieldMapping fm : fieldMappings) {
			final Object value = mc.getValue(fm.getField());
			fm.marshal(mc, value);
		}
	}

	public void unmarshal(Object record, UnmarshalContext mc)
			throws MappingException {
		for (final FieldMapping fm : fieldMappings)
			mc.setValue(fm.getField(), fm.unmarshal(mc));

		for (final FieldMapping fm : fieldMappings)
			fm.afterUnmarshal(mc);

		for (final Method m : afterUnmarshal)
			try {
				m.invoke(record, (Object[]) null);
			} catch (final Exception e) {
				throw new MappingException(
						this, "Can't invoke @AfterUnmarshal callback method", e);
			}
	}

	@Override
	public int getSize(MappingContext ctx) throws MappingException {
		int size = 0;

		for (final FieldMapping fm : fieldMappings)
			size += fm.getSize(ctx);

		return size;
	}

	public FieldMapping getFieldMapping(String ref) {
		return fieldMappingsByName.get(ref);
	}

  public RecordMapping getRecordMapping(Class<?> cls) {
		return recordMappings.get(cls);
	}

  public Class<?> getRecordClass() {
		return recordClass;
	}
}
