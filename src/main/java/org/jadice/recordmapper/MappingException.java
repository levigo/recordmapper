package org.jadice.recordmapper;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MappingException extends Exception {
  private static final long serialVersionUID = 1L;
  private final List<Mapping> ctx = new LinkedList<Mapping>();

  public MappingException(final Mapping ctx, final String message, final Throwable cause) {
    super(message, cause);

    if (cause instanceof MappingException)
      this.ctx.addAll(0, ((MappingException) cause).ctx);

    this.ctx.add(0, ctx);
  }

  public MappingException(final Mapping ctx, final String message) {
    super(message);
    this.ctx.add(0, ctx);
  }

  public MappingException(final Mapping ctx, final Throwable cause) {
    super(cause instanceof MappingException ? ((MappingException) cause).rawMessage() : cause.getLocalizedMessage(),
        cause);

    if (cause instanceof MappingException)
      this.ctx.addAll(0, ((MappingException) cause).ctx);

    this.ctx.add(0, ctx);
  }

  public MappingException(final String message) {
    super(message);
  }

  public MappingException(final String message, final Throwable cause) {
    super(message, cause);
  }

  @Override
  public String getMessage() {
    return super.getMessage() + formatContext();
  }

  private String rawMessage() {
    return super.getMessage();
  }

  private String formatContext() {
    return "\n  @" + ctx.stream().map(Object::toString).collect(Collectors.joining(" -> "));
  }
}
