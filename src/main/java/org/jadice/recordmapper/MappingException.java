package org.jadice.recordmapper;

import java.util.LinkedList;
import java.util.List;

public class MappingException extends Exception {
	private static final long serialVersionUID = 1L;
  private final List<Mapping> ctx = new LinkedList<Mapping>();

	public MappingException(Mapping ctx, String message, Throwable cause) {
		super(message, cause);
		
		if(cause instanceof MappingException)
		  this.ctx.addAll(((MappingException)cause).ctx);
		
    this.ctx.add(ctx);
	}

	public MappingException(Mapping ctx, String message) {
		super(message);
		this.ctx.add(ctx);
	}

	public MappingException(Mapping ctx, Throwable cause) {
		super(cause);

		if(cause instanceof MappingException)
      this.ctx.addAll(((MappingException)cause).ctx);
    
		this.ctx.add(ctx);
	}
	
	public MappingException(String message) {
    super(message);
  }

  public MappingException(Exception cause) {
    super(cause);
  }

  public MappingException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
	public String toString() {
	  return super.toString() + formatContext();
	}

  @Override
	public String getMessage() {
	  return super.getMessage() + formatContext();
	}
  
  private String formatContext() {
    return "\n  @" + ctx.toString();
  }
}
