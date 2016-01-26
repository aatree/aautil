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
(def xy-lens (lcomp x-lens y-lens))
(println (lreset! xy-lens nil 5)) ;-> {:x {:y 5}}
(println (lderef xy-lens {:x {:y 5 :z 3}})) ;-> 5
(println (lswap! xy-lens {:x {:y 5 :z 3}}
                  (fn [old] (* 2 old)))) ;-> {:x {:y 10, :z 3}}

(println (lreset! edn-lens nil 5)) ;-> "5"
(def edn-xy-lens (lcomp edn-lens xy-lens))
(println (lreset! edn-xy-lens nil 5)) ;-> "{:x {:y 5}}"
(println (lderef edn-xy-lens "{:x {:y 5 :z 3}}")) ;-> 5
(println (lswap! edn-xy-lens "{:x {:y 5 :z 3}}"
                 (fn [old] (* 2 old)))) ;-> "{:x {:y 10, :z 3}}"
(println)

(def test-lens (-> (printing-lens "right")
                   (lcomp x-lens)
                   (lcomp (printing-lens "left"))))
(println (lreset! test-lens {} 5))
;-> right :got {}
;-> left :set nil 5
;-> right :got {}
;-> right :set {} {:x 5}
;-> {:x 5}
(println)
(println (lderef test-lens {:x 5 :y 6}))
;-> right :got {:x 5, :y 6}
;-> left :got 5
;-> 5
(println)
(println (lderef test-lens {}))
;-> right :got {}
;-> left :got nil
;-> nil
(println)
(println (lreset! test-lens nil 5))
;-> right :got nil
;-> left :set nil 5
;-> right :got nil
;-> right :set nil {:x 5}
;-> {:x 5}
(println)
(println (lswap! test-lens {:x 5 :y 6}
                 (fn [old] (* 2 old))))
;-> right :got {:x 5, :y 6}
;-> left :got 5
;-> right :got {:x 5, :y 6}
;-> left :set 5 10
;-> right :got {:x 5, :y 6}
;-> right :set {:x 5, :y 6} {:x 10, :y 6}
;-> {:x 10, :y 6}
(println)

(def test-nil-lens (-> (printing-lens "right")
                       (lcomp (printing-lens "left"))))
(println (lreset! test-nil-lens :old :new))
;-> right :got :old
;-> left :set :old :new
;-> right :set :old :new
;-> :new
(println)
(println (lderef test-nil-lens :old))
;-> right :got :old
;-> left :got :old
;-> :old
(println)
(println (lswap! test-nil-lens 21
                 (fn [old] (* 2 old))))
;-> right :got 21
;-> left :got 21
;-> right :got 21
;-> left :set 21 42
;-> right :set 21 42
;-> 42
(println)
