package org.jadice.recordmapper.cobol.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import org.jadice.recordmapper.MappingException;
import org.jadice.recordmapper.cobol.CBLFixedLength;
import org.jadice.recordmapper.cobol.CBLRecord;
import org.jadice.recordmapper.impl.FieldMapping;
import org.jadice.recordmapper.impl.MappingContext;
import org.jadice.recordmapper.impl.MarshalContext;
import org.jadice.recordmapper.impl.RecordMapping;
import org.jadice.recordmapper.impl.UnmarshalContext;

public class CBLTableImpl extends FieldMapping {

	// private CBLTable spec;

	private Class<?> collectionType;
	private Class<?> memberType;

	private RecordMapping memberMapping;

	private FieldMapping sizeOrCountField;

	private int fixedLength = -1;

	@Override
  protected void init(Annotation a) throws MappingException {
		// this.spec = (CBLTable) a;

		// determine member type
		collectionType = field.getType();
		if (Collection.class.isAssignableFrom(collectionType)) {
			if (Map.class.isAssignableFrom(collectionType)) {
				throw new MappingException(this, a + " with maps is not yet supported");
			} else {
				final Type t = field.getGenericType();
				if (!(t instanceof ParameterizedType))
					throw new MappingException(this, a
							+ " with collections require a generic type parameter");

				final ParameterizedType pt = (ParameterizedType) t;

        memberType = (Class<?>) pt.getActualTypeArguments()[0];
			}

		} else if (collectionType.isArray()) {
			memberType = field.getType().getComponentType();
		} else
			throw new MappingException(this, a
					+ " is only allowed in conjunction with Collection or array types.");

		if (memberType.getAnnotation(CBLRecord.class) == null)
			throw new MappingException(this, "Member type " + memberType + " for a " + a
					+ " is not annotated with @CBLRecord.");

		// do I have a fixed length?
		final CBLFixedLength fs = field.getAnnotation(CBLFixedLength.class);
		if (fs != null)
			fixedLength = fs.value();
	}

	@Override
  public Collection<? extends Class<?>> getReferencedClasses() {
		return Collections.singleton(memberType);
	}

	@Override
	protected void postInit() throws MappingException {
		super.postInit();

		memberMapping = recordMapping.getRecordMapping(memberType);
		if (null == memberMapping)
			throw new MappingException(this, "XMLMapping for " + memberType
					+ " could not be found");
	}

	@Override
  public int getSize(MappingContext ctx) throws MappingException {
		if (!(ctx instanceof MarshalContext)) {
			if (null != sizeOrCountField && sizeOrCountField instanceof CBLSizeImpl)
				return ((Number) ctx.getValue(sizeOrCountField.getField())).intValue();

			throw new MappingException(
					this, "I am not supposed to know my size at this point");
		}

		int size = 0;

		for (final Object member : getMembers(ctx))
			size += memberMapping.getSize(((MarshalContext) ctx)
					.getMemberContext(member));

		return size;
	}

	@SuppressWarnings("unchecked")
	private Collection<Object> getMembers(MappingContext ctx)
			throws MappingException {
		final Object v = ctx.getValue(field);

		if (null == v)
			return Collections.EMPTY_LIST;

		if (!collectionType.isArray())
			return (Collection<Object>) v;

		return Arrays.asList((Object[]) v);
	}

	@Override
  public void marshal(MarshalContext ctx, Object value) throws MappingException {
		final Collection<Object> members = getMembers(ctx);

		if (fixedLength >= 0 && members.size() != fixedLength)
			throw new MappingException(
					this, "Collection or array size does not match fixed size: expected "
							+ fixedLength + "is " + members.size());

		for (final Object member : members)
			memberMapping.marshal(member, ctx.getMemberContext(member));
	}

	@Override
  public Object unmarshal(UnmarshalContext ctx) throws MappingException {
		int expectedSize = -1;
		int expectedCount = fixedLength;

		if (sizeOrCountField != null) {
			final int sc = ((Number) ctx.getValue(sizeOrCountField.getField())).intValue();
			if (sizeOrCountField instanceof CBLSizeImpl)
				expectedSize = sc;
			else if (sizeOrCountField instanceof CBLCountImpl)
				expectedCount = sc;
		}

		if (expectedSize < 0 && expectedCount < 0)
			throw new MappingException(
					this, "Can't unmarshal table: neither have size nor count");

		final int startPos = ctx.getPosition();
		final List<Object> members = new ArrayList<Object>(expectedSize > 0
				? expectedSize
				: 0);
		while ((expectedCount < 0 || members.size() < expectedCount)
				&& (expectedSize < 0 || ctx.getPosition() - startPos < expectedSize)) {
			try {
				final Object member = memberType.newInstance();
				final UnmarshalContext mc = ctx.createMemberContext(member, memberMapping);
				memberMapping.unmarshal(member, mc);

				members.add(member);
			} catch (final Exception e) {
				throw new MappingException(this, "Can't create member instance for "
						+ memberType, e);
			}
		}

		// cast collection to proper type
		if (collectionType.isArray())
			return members.toArray((Object[]) Array.newInstance(memberType, members
					.size()));

		if (Set.class.isAssignableFrom(collectionType))
			return new HashSet<Object>(members);

		if (Vector.class.isAssignableFrom(collectionType))
			return new Vector<Object>(members);

		if (Stack.class.isAssignableFrom(collectionType)) {
			final Stack<Object> s = new Stack<Object>();
			s.addAll(members);
			return s;
		}

		return members;
	}

	public long getCount(MarshalContext ctx) throws MappingException {
		return getMembers(ctx).size();
	}

	@Override
	public void registerParameterField(FieldMapping param)
			throws MappingException {
		if (fixedLength >= 0)
			throw new MappingException(
					this, "Size or count not expected: table already has a fixed length");

		sizeOrCountField = param;
	}
}
