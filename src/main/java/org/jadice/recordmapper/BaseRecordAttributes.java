package org.jadice.recordmapper;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BaseRecordAttributes implements RecordAttributes {
  private Charset charset = StandardCharsets.ISO_8859_1;

  /**
   * @deprecated use {@link #getCharset()} instead.
   */
  @Deprecated
  public String getEncoding() {
    return charset.name();
  }

  /**
   * @deprecated use {@link #setCharset(Charset)} instead.
   */
  @Deprecated
  public void setEncoding(String encoding) {
    this.charset = Charset.forName(encoding);
  }
  
  public Charset getCharset() {
    return charset;
  }
  
  public void setCharset(Charset charset) {
    this.charset = charset;
  }
}
