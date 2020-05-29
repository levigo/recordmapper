package org.jadice.recordmapper.cobol;


public abstract class TProperty {
  protected abstract String getName();

  protected abstract void setName(String name);

  protected abstract String getValue();

  protected abstract void setValue(String value);
}