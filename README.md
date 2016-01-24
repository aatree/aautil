# aautil
Snippets of useful cljc code

[![Clojars Project](https://img.shields.io/clojars/v/aatree/aautil.svg)](https://clojars.org/aatree/aautil)

1. [aalog](#aalog) - A logger that wraps js/console and tools.logging.
1. [closer](#closer) - Lifecycle for ultra-light components.
1. [dewdrop](#dewdrop) - Lenses.
1. [Change Log](#change-log)

---

## aalog

The aautil/aalog.cljc file implements a thin dumb layer over
js/console and org.clojure/tools.logging. Easy to use from
another .cljc file.

The aautil/aalog_test.clj file provides a simple clj test of aalog,
while the [counters](https://github.com/aatree/aademos/tree/master/counters)
demo provides a simple cljc example using hoplon.

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

The aautil/closer_test.clj file provides a simple clj test of closer,
while the [closer](https://github.com/aatree/aademos/tree/master/closer)
demo provides a simple cljc example using hoplon.

#### API

**(open-trait this name f)** opens a trait.

* *this* - the map structure of the component.
* *name* - the name of the trait.
* *f* - the function used to close the trait. Takes a single argument, this.
* The value returned by open-trait is the revised map structure of the component.

**(close-component this)** closes the traits of a component, 
reversing the order in which the traits were opened.
The *this* parameter is the map structure of the component.

---

## dewdrop

Dewdrop is a very simple implementation of lenses,
where a lens is a device for operating on a part of a larger structure.

Lets say you have a map and you want to operate on the value of :x.
You would define the lens like this:

```
(def x-lens (key-lens :x))
```
Here are some sample tests:

```
(println (lreset! x-lens {} 5))
;-> {:x 5}
(println (lderef x-lens {:x 5 :y 6}))
;-> 5
(println (lderef x-lens {}))
;-> nil
(println (lreset! x-lens nil 5))
;-> {:x 5}
(println (lswap! x-lens {:x 5 :y 6}
                (fn [old] (* 2 old))))
;-> {:x 10, :y 6}
```
Now lets create a second lens for operating on the value of :y in a map:

```
(def y-lens (key-lens :y))
```
But what if the value of :y is found in the map which :x holds?
We just compose a new lens using the lenses we already have:
```

(def xy-lens (lcompose x-lens y-lens))
```
And here are some more tests:

```
(println (lreset! xy-lens nil 5))
;-> {:x {:y 5}
(println (lderef xy-lens {:x {:y 5 :z 3}}))
;-> 5
(println (lswap! xy-lens {:x {:y 5 :z 3}}
                  (fn [old] (* 2 old))))
;-> {:x {:y 10, :z 3}}
```

### Write your own lenses

A dewdrop lens is nothing more than a map structure with getter and setter functions as values:

```
(defn new-lens
  "Create a new lens."
  [getter setter]
  {:getter getter :setter setter})

```
Defining a kind of lens then is very simple, and you can easily define lenses for
different types of data structures.
Here is the key-lens function we used above for accessing maps:

```
(defn key-lens [k]
  (lens.
    (fn [d] (get d k))
    (fn [d v] (assoc d k v))))
```

---

# Change Log

**0.0.2** - Fixed bugs in closer-test.

**0.0.1** - Initial release
