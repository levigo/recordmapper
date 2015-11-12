package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.RecordMapping;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLNestedImpl extends FieldMapping {

	// private CBLNested spec;

	private Class<?> componentType;
	private RecordMapping componentMapping;

	@Override
  protected void init(Annotation a) throws MappingException {
		// this.spec = (CBLNested) a;

		// determine member type
		componentType = field.getType();

		if (componentType.getAnnotation(CBLRecord.class) == null)
			throw new MappingException(this, "Component type " + componentType + " for a "
					+ a + " is not annotated with @CBLRecord.");
	}

	@Override
  public Collection<? extends Class<?>> getReferencedClasses() {
		return Collections.singleton(componentType);
	}

	@Override
	protected void postInit() throws MappingException {
		super.postInit();

		componentMapping = recordMapping.getRecordMapping(componentType);
		if (null == componentMapping)
			throw new MappingException(this, "XMLMapping for " + componentType
					+ " could not be found");
	}

	@Override
  public int getSize(MappingContext ctx) throws MappingException {
		if (!(ctx instanceof MarshalContext))
			throw new MappingException(
					this, "I am not supposed to know my size at this point");

		return componentMapping.getSize(((MarshalContext) ctx).getMemberContext(ctx
				.getValue(field)));
	}

	@Override
  public void marshal(MarshalContext ctx, Object value) throws MappingException {
		componentMapping.marshal(value, ctx.getMemberContext(value));
	}

	@Override
  public Object unmarshal(UnmarshalContext ctx) throws MappingException {
		Object component;
		try {
			component = componentType.newInstance();
			final UnmarshalContext mc = ctx
					.createMemberContext(component, componentMapping);
			componentMapping.unmarshal(component, mc);

			return component;
		} catch (final Exception e) {
			throw new MappingException(this, e);
		}
	}

	@Override
	public void registerParameterField(FieldMapping param)
			throws MappingException {
		// we don't care, but let a size field have its way anyway.
	}
}
