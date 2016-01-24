(ns aautil.dewdrop-test
  (:require [aautil.dewdrop :refer :all]))

(def x-lens (key-lens :x))
(println (lreset! x-lens {} 5)) ;-> {:x 5}
(println (lderef x-lens {:x 5 :y 6})) ;-> 5
(println (lderef x-lens {})) ;-> nil
(println (lreset! x-lens nil 5)) ;-> {:x 5}
(println (lswap! x-lens {:x 5 :y 6}
                  (fn [old] (* 2 old)))) ;-> {:x 10, :y 6}

(def y-lens (key-lens :y))
(def xy-lens (lcompose x-lens y-lens))
(println (lreset! xy-lens nil 5)) ;-> {:x {:y 5}}
(println (lderef xy-lens {:x {:y 5 :z 3}})) ;-> 5
(println (lswap! xy-lens {:x {:y 5 :z 3}}
                  (fn [old] (* 2 old)))) ;-> {:x {:y 10, :z 3}}
