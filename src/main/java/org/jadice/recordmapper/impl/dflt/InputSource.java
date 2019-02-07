package org.jadice.recordmapper.impl.dflt;

import java.io.IOException;

import org.jadice.recordmapper.MappingException;


public interface InputSource {

  byte[] getBytes(int size) throws MappingException;

  int getPosition();

  boolean hasMore() throws IOException;

}