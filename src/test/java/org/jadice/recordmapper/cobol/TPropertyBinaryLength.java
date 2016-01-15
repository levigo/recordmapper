/*
 * Copyright (c) 2004 levigo holding gmbh. All Rights Reserved. This software is the proprietary
 * information of levigo holding gmbh. Use is subject to license terms. Created on 30.06.2004
 */
package org.jadice.recordmapper.cobol;

/**
 * Eine Property
 * 
 * @author B022449
 */
@CBLRecord
public class TPropertyBinaryLength extends TProperty {
  @CBLNumeric(4)
  int length;

  @CBLString(40)
  public String name;

  @CBLVariableString(sizeRef = "length")
  public String value;

  @Override
  protected String getName() {
    return name;
  }

  @Override
  protected void setName(String name) {
    this.name = name;
  }

  @Override
  protected String getValue() {
    return value;
  }

  @Override
  protected void setValue(String value) {
    this.value = value;
  }
}