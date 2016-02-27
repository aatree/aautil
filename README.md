# aautil
Snippets of useful cljc code

[![Clojars Project](https://img.shields.io/clojars/v/aatree/aautil.svg)](https://clojars.org/aatree/aautil)

1. [aalog](#aalog) - A logger that wraps js/console and tools.logging.
1. [closer](#closer) - Lifecycle for ultra-light components.
1. [dewdrop](#dewdrop) - Lenses.
1. [bytes](#bytes) - Byte arrays.

[Change Log](#change-log)

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
(println (lset! x-lens {} 5))
;-> {:x 5}
(println (lget x-lens {:x 5 :y 6}))
;-> 5
(println (lget x-lens {}))
;-> nil
(println (lset! x-lens nil 5))
;-> {:x 5}
(println (lupd! x-lens {:x 5 :y 6}
                (fn [old] (* 2 old))))
;-> {:x 10, :y 6}
```
Dewdrop lenses also work with data structures held by atoms:

```
(def my-atom (atom {:x 1 :y 2}))
(println (lderef x-lens my-atom))                           ;-> 1
(println (lreset! x-lens my-atom 3) @my-atom)               ;-> {:x 3, :y 2} {:x 3, :y 2}
(println (lswap! x-lens my-atom
                 (fn [data] (* 2 data))) @my-atom)          ;-> {:x 6, :y 2} {:x 6, :y 2}
```
Now lets create a second lens for operating on the value of :y in a map:

```
(def y-lens (key-lens :y))
```
But what if the value of :y is found in the map which :x holds?
We just compose a new lens using the lenses we already have:
```

(def xy-lens (lcomp y-lens x-lens))
```
And here are some more tests:

```
(println (lset! xy-lens nil 5))
;-> {:x {:y 5}
(println (lget xy-lens {:x {:y 5 :z 3}}))
;-> 5
(println (lupd! xy-lens {:x {:y 5 :z 3}}
                  (fn [old] (* 2 old))))
;-> {:x {:y 10, :z 3}}
```

Sometimes we need a key-lens which allows the value of 
the key to change. For this we can use key-atom-lens:

```
(def my-key (atom :w))
(def my-key-lens (key-atom-lens my-key))
(println (lset! my-key-lens {} 5)) ;-> {:w 5}
(println (lget my-key-lens {:w 5 :y 6})) ;-> 5
(println (lget my-key-lens {})) ;-> nil
(reset! my-key :n)
(println (lset! my-key-lens nil 5)) ;-> {:n 5}
(println (lupd! my-key-lens {:n 5 :y 6}
                 (fn [old] (* 2 old)))) ;-> {:n 10, :y 6}
```

Now if your data structure happens to be an EDN string, 
or happens to contain a string that holds an EDN string,
then the edn-lens may be quite handy:

```
(println (lset! edn-lens nil 5)) ;-> "5"
(def edn-xy-lens (lcomp xy-lens edn-lens))
(println (lset! edn-xy-lens nil 5)) ;-> "{:x {:y 5}}"
(println (lget edn-xy-lens "{:x {:y 5 :z 3}}")) ;-> 5
(println (lupd! edn-xy-lens "{:x {:y 5 :z 3}}"
                 (fn [old] (* 2 old)))) ;-> "{:x {:y 10, :z 3}}"
```

#### Views

The lens-view in dewdrop is a record created using the lview function,
which combines a lens with an atom (or hoplon cell):

```
(defn lview [lens data-atom]
  (->lens-view lens data-atom))
```

Functions which work with a lens-view are @, reset! and swap!.

#### Write your own lenses

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
(defn key-lens [key]
  {:getter (fn [data] (get data key)) 
   :setter (fn [data item] (assoc data key item))})
```

The key-atom-lens is almost the same:

```
(defn atom-key-lens
  [key-atom]
  {:getter (fn [data] (get data @key-atom))
   :setter (fn [data item] (assoc data @key-atom item))})
```

And edn-lens is just as simple:

```
(def edn-lens
  {:getter read-string
   :setter (fn [_ item] (pr-str item))})
```

---

## bytes

The aautil/bytes.cljc file implements a thin dumb layer over
various clojure and clojurescript functions. Easy to use from
another .cljc file.

The [bytes](https://github.com/aatree/aademos/tree/master/bytes)
demo provides a simple cljc example using hoplon, as well as unit tests.

#### API

**(make-bytes s)** Returns a byte array of size s.

**(set-byte! ba i v)** Sets byte i in array ba to v.

**(bytes-equal ba1 ba2)** Returns true only if the two byte arrays are equal.

**(vec-bytes ba)** Returns a vector of bytes.

---

# Change Log

**0.0.5** - Accessing byte arrays.

**0.0.4** - Dewdrop now supports views.

**0.0.3** - Added dewdrop lenses.

**0.0.2** - Fixed bugs in closer-test.

**0.0.1** - Initial release.
