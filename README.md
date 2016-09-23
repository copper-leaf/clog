# Android-Clog
Clog logger implementation for the standard Android Log priorities

See https://github.com/cjbrooks12/Clog for information on Clog.

### Android Clog Implementation

Android Clog provides simple Android Log implementations for Clog. The mapping between Clog levels and Android Clog levels is one-to-one, as the Clog API was designed to directly replace Log:

`Clog.d(...) --> Log.d(...)`

`Clog.e(...) --> Log.e(...)`

`Clog.i(...) --> Log.i(...)`

`Clog.v(...) --> Log.v(...)`

`Clog.w(...) --> Log.w(...)`

`Clog.wtf(...) --> Log.wtf(...)`

In addition, Clog tags work exactly like Android Log tags, like so:

```
d(String tag, String message) {
  Log.d(tag, message);
}
```
