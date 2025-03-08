package org.acme.model;

public class GithubException extends RuntimeException {
  private final int statusCode;

  public GithubException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
