package net.illandril.games.playntest.core;

import static playn.core.PlayN.log;

public class Logger {
  private static boolean isDebugEnabled = true;
  private static boolean isInfoEnabled = true;
  private static boolean isWarnEnabled = true;
  private static boolean isErrorEnabled = true;

  private Logger() {
  }

  private static String getMessage(String identifier, String message) {
    return "[" + identifier + "] " + message;
  }

  public static boolean isDebugEnabled() {
    return isDebugEnabled;
  }

  public static boolean isInfoEnabled() {
    return isInfoEnabled;
  }

  public static boolean isWarnEnabled() {
    return isWarnEnabled;
  }

  public static boolean isErrorEnabled() {
    return isErrorEnabled;
  }

  public static void debug(String identifier, String message) {
    if (isDebugEnabled) {
      log().debug(getMessage(identifier, message));
    }
  }

  public static void info(String identifier, String message) {
    if (isInfoEnabled) {
      log().info(getMessage(identifier, message));
    }
  }

  public static void warn(String identifier, String message) {
    if (isWarnEnabled) {
      log().warn(getMessage(identifier, message));
    }
  }

  public static void error(String identifier, String message) {
    if (isErrorEnabled) {
      log().error(getMessage(identifier, message));
    }
  }

}
