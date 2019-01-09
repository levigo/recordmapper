package org.jadice.recordmapper;

public class BaseRecordAttributes implements RecordAttributes {
  private String encoding = "ISO8859-1";

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }
}
