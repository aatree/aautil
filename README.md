# aautil
Snippets of useful code

1. [aalog](#aalog)
1. [closer](#closer)

---

## aalog

The aautil/aalog.cljc file implements a thin dumb layer over
js/console and org.clojure/tools.logging. Easy to use from
another .cljc file.

The aautil/aalog_test.clj file provides a simple clj test of aalog,
while [counters](https://github.com/aatree/aademos/tree/master/counters)
provides a simple cljc example using hoplon.

#### API

**(info msg)** displays an info log message.

**(warn msg)** displays a warning log message.

**(debug msg)** displays a debug log message.

**(error msg)** displays an error log message.

---

## closer

We can think of bags (maps) of properties as a light-weight form
of component, where the properties can be either data or functions.
Components are composed from traits simply by combining the
properties of those traits into a single map structure.

But even light-weight components have some life-cycle requirements.
Often a trait needs to be closed. And a composition of components
typically requires that the closes need to be performed in the reverse
of the opening order. This is handled by aautil/closer.cljc.

The aautil/closer_test.clj file provides a simple clj test of closer.

#### API

**(open-trait this name f)** opens a trait.

* *this* - the map structure of the component.
* *name* - the name of the trait.
* *f* - the function used to close the trait. Takes a single argument, this.

**(close-component this)** closes the traits of a component, 
reversing the order in which the traits were opened.
The *this* parameter is the map structure of the component.

---
