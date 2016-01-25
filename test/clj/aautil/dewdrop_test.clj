(ns aautil.dewdrop-test
  (:require [aautil.dewdrop :refer :all]))

(def x-lens (key-lens :x))
(println (lreset! x-lens {} 5)) ;-> {:x 5}
(println (lderef x-lens {:x 5 :y 6})) ;-> 5
(println (lderef x-lens {})) ;-> nil
(println (lreset! x-lens nil 5)) ;-> {:x 5}
(println (lswap! x-lens {:x 5 :y 6}
                  (fn [old] (* 2 old)))) ;-> {:x 10, :y 6}

(def my-key (atom :w))
(def my-key-lens (key-atom-lens my-key))
(println (lreset! my-key-lens {} 5)) ;-> {:w 5}
(println (lderef my-key-lens {:w 5 :y 6})) ;-> 5
(println (lderef my-key-lens {})) ;-> nil
(reset! my-key :n)
(println (lreset! my-key-lens nil 5)) ;-> {:n 5}
(println (lswap! my-key-lens {:n 5 :y 6}
                 (fn [old] (* 2 old)))) ;-> {:n 10, :y 6}

(def y-lens (key-lens :y))
(def xy-lens (lcompose x-lens y-lens))
(println (lreset! xy-lens nil 5)) ;-> {:x {:y 5}}
(println (lderef xy-lens {:x {:y 5 :z 3}})) ;-> 5
(println (lswap! xy-lens {:x {:y 5 :z 3}}
                  (fn [old] (* 2 old)))) ;-> {:x {:y 10, :z 3}}

(println (lreset! edn-lens nil 5)) ;-> "5"
(def edn-xy-lens (lcompose edn-lens xy-lens))
(println (lreset! edn-xy-lens nil 5)) ;-> "{:x {:y 5}}"
(println (lderef edn-xy-lens "{:x {:y 5 :z 3}}")) ;-> 5
(println (lswap! edn-xy-lens "{:x {:y 5 :z 3}}"
                 (fn [old] (* 2 old)))) ;-> "{:x {:y 10, :z 3}}"
