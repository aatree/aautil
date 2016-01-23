# aautil
Snippets of useful code

1. [aalog](#aalog)

## aalog

The aautil/aalog.cljc file implements a thin dumb layer over
js/console and org.clojure/tools.logging. Easy to use from
another .cljc file.

The aautil/aalog_test.clj file provides a simple clj test of aalog,
while [counters](https://github.com/aatree/aademos/tree/master/counters)
provides a simple cljc example using hoplon.

### API

**(info msg)** displays an info log message.

**(warn msg)** displays a warning log message.

**(debug msg)** displays a debug log message.

**(error msg)** displays an error log message.
