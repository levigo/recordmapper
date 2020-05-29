package org.jadice.recordmapper.impl;

import org.jadice.recordmapper.MappingException;

public interface MarshalContext extends MappingContext {

  void put(String s) throws MappingException;

  void put(byte[] buffer) throws MappingException;

  void put(byte b) throws MappingException;

  MarshalContext getMemberContext(Object member);
}
