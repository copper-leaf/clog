package org.slf4j.impl;

import com.caseyjbrooks.clog.Clog;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class Clog4jLoggerImpl implements Logger {

    private String name;

    public Clog4jLoggerImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    // What is enabled?
    @Override public boolean isTraceEnabled()              { return true; }
    @Override public boolean isTraceEnabled(Marker marker) { return true; }
    @Override public boolean isDebugEnabled()              { return true; }
    @Override public boolean isDebugEnabled(Marker marker) { return true; }
    @Override public boolean isInfoEnabled()               { return true; }
    @Override public boolean isInfoEnabled(Marker marker)  { return true; }
    @Override public boolean isWarnEnabled()               { return true; }
    @Override public boolean isWarnEnabled(Marker marker)  { return true; }
    @Override public boolean isErrorEnabled()              { return true; }
    @Override public boolean isErrorEnabled(Marker marker) { return true; }

    // Logger implementations
    @Override public void trace(String msg)                                             { Clog.tag(name).v(msg);                            }
    @Override public void trace(String format, Object arg)                              { Clog.tag(name).v(format, arg);                    }
    @Override public void trace(String format, Object arg1, Object arg2)                { Clog.tag(name).v(format, arg1, arg2);             }
    @Override public void trace(String format, Object... arguments)                     { Clog.tag(name).v(format, arguments);              }
    @Override public void trace(String msg, Throwable t)                                { Clog.tag(name).v(msg, t);                         }
    @Override public void trace(Marker marker, String msg)                              { Clog.tag(marker.getName()).v(msg);                }
    @Override public void trace(Marker marker, String format, Object arg)               { Clog.tag(marker.getName()).v(format, arg);        }
    @Override public void trace(Marker marker, String format, Object arg1, Object arg2) { Clog.tag(marker.getName()).v(format, arg1, arg2); }
    @Override public void trace(Marker marker, String format, Object... argArray)       { Clog.tag(marker.getName()).v(format, argArray);   }
    @Override public void trace(Marker marker, String msg, Throwable t)                 { Clog.tag(marker.getName()).v(msg, t);             }

    @Override public void debug(String msg)                                             { Clog.tag(name).d(msg);                            }
    @Override public void debug(String format, Object arg)                              { Clog.tag(name).d(format, arg);                    }
    @Override public void debug(String format, Object arg1, Object arg2)                { Clog.tag(name).d(format, arg1, arg2);             }
    @Override public void debug(String format, Object... arguments)                     { Clog.tag(name).d(format, arguments);              }
    @Override public void debug(String msg, Throwable t)                                { Clog.tag(name).d(msg, t);                         }
    @Override public void debug(Marker marker, String msg)                              { Clog.tag(marker.getName()).d(msg);                }
    @Override public void debug(Marker marker, String format, Object arg)               { Clog.tag(marker.getName()).d(format, arg);        }
    @Override public void debug(Marker marker, String format, Object arg1, Object arg2) { Clog.tag(marker.getName()).d(format, arg1, arg2); }
    @Override public void debug(Marker marker, String format, Object... argArray)       { Clog.tag(marker.getName()).d(format, argArray);   }
    @Override public void debug(Marker marker, String msg, Throwable t)                 { Clog.tag(marker.getName()).d(msg, t);             }

    @Override public void info(String msg)                                              { Clog.tag(name).i(msg);                            }
    @Override public void info(String format, Object arg)                               { Clog.tag(name).i(format, arg);                    }
    @Override public void info(String format, Object arg1, Object arg2)                 { Clog.tag(name).i(format, arg1, arg2);             }
    @Override public void info(String format, Object... arguments)                      { Clog.tag(name).i(format, arguments);              }
    @Override public void info(String msg, Throwable t)                                 { Clog.tag(name).i(msg, t);                         }
    @Override public void info(Marker marker, String msg)                               { Clog.tag(marker.getName()).i(msg);                }
    @Override public void info(Marker marker, String format, Object arg)                { Clog.tag(marker.getName()).i(format, arg);        }
    @Override public void info(Marker marker, String format, Object arg1, Object arg2)  { Clog.tag(marker.getName()).i(format, arg1, arg2); }
    @Override public void info(Marker marker, String format, Object... argArray)        { Clog.tag(marker.getName()).i(format, argArray);   }
    @Override public void info(Marker marker, String msg, Throwable t)                  { Clog.tag(marker.getName()).i(msg, t);             }

    @Override public void warn(String msg)                                              { Clog.tag(name).w(msg);                            }
    @Override public void warn(String format, Object arg)                               { Clog.tag(name).w(format, arg);                    }
    @Override public void warn(String format, Object arg1, Object arg2)                 { Clog.tag(name).w(format, arg1, arg2);             }
    @Override public void warn(String format, Object... arguments)                      { Clog.tag(name).w(format, arguments);              }
    @Override public void warn(String msg, Throwable t)                                 { Clog.tag(name).w(msg, t);                         }
    @Override public void warn(Marker marker, String msg)                               { Clog.tag(marker.getName()).w(msg);                }
    @Override public void warn(Marker marker, String format, Object arg)                { Clog.tag(marker.getName()).w(format, arg);        }
    @Override public void warn(Marker marker, String format, Object arg1, Object arg2)  { Clog.tag(marker.getName()).w(format, arg1, arg2); }
    @Override public void warn(Marker marker, String format, Object... argArray)        { Clog.tag(marker.getName()).w(format, argArray);   }
    @Override public void warn(Marker marker, String msg, Throwable t)                  { Clog.tag(marker.getName()).w(msg, t);             }

    @Override public void error(String msg)                                             { Clog.tag(name).e(msg);                            }
    @Override public void error(String format, Object arg)                              { Clog.tag(name).e(format, arg);                    }
    @Override public void error(String format, Object arg1, Object arg2)                { Clog.tag(name).e(format, arg1, arg2);             }
    @Override public void error(String format, Object... arguments)                     { Clog.tag(name).e(format, arguments);              }
    @Override public void error(String msg, Throwable t)                                { Clog.tag(name).e(msg, t);                         }
    @Override public void error(Marker marker, String msg)                              { Clog.tag(marker.getName()).e(msg);                }
    @Override public void error(Marker marker, String format, Object arg)               { Clog.tag(marker.getName()).e(format, arg);        }
    @Override public void error(Marker marker, String format, Object arg1, Object arg2) { Clog.tag(marker.getName()).e(format, arg1, arg2); }
    @Override public void error(Marker marker, String format, Object... argArray)       { Clog.tag(marker.getName()).e(format, argArray);   }
    @Override public void error(Marker marker, String msg, Throwable t)                 { Clog.tag(marker.getName()).e(msg, t);             }

}
