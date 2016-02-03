(ns aautil.dewdrop-test
  (:require [aautil.dewdrop :refer :all]))

(def x-lens (key-lens :x))
(println (lset! x-lens {} 5))                               ;-> {:x 5}
(println (lget x-lens {:x 5 :y 6}))                         ;-> 5
(println (lget x-lens {}))                                  ;-> nil
(println (lset! x-lens nil 5))                              ;-> {:x 5}
(println (lupd! x-lens {:x 5 :y 6}
                (fn [old] (* 2 old))))                      ;-> {:x 10, :y 6}

(def my-atom (atom {:x 1 :y 2}))
(def my-x (lview x-lens my-atom))
(println (lderef x-lens my-atom))                           ;-> 1
(println @my-x)                                             ;-> 1
(println (lreset! x-lens my-atom 3) @my-atom)               ;-> {:x 3, :y 2} {:x 3, :y 2}
(println (reset! my-x 4) @my-atom)                          ;-> {:x 4, :y 2} {:x 4, :y 2}
(println (lswap! x-lens my-atom
                 (fn [data] (* 2 data))) @my-atom)          ;-> {:x 8, :y 2} {:x 8, :y 2}
(println (swap! my-x
                (fn [data] (* 2 data))) @my-atom)          ;-> {:x 16, :y 2} {:x 16, :y 2}

(def my-key (atom :w))
(def my-key-lens (atom-key-lens my-key))
(println (lset! my-key-lens {} 5))                          ;-> {:w 5}
(println (lget my-key-lens {:w 5 :y 6}))                    ;-> 5
(println (lget my-key-lens {}))                             ;-> nil
(reset! my-key :n)
(println (lset! my-key-lens nil 5))                         ;-> {:n 5}
(println (lupd! my-key-lens {:n 5 :y 6}
                (fn [old] (* 2 old))))                      ;-> {:n 10, :y 6}

(def y-lens (key-lens :y))
(def xy-lens (lcomp y-lens x-lens))
(println (lset! xy-lens nil 5))                           ;-> {:x {:y 5}}
(println (lget xy-lens {:x {:y 5 :z 3}}))                 ;-> 5
(println (lupd! xy-lens {:x {:y 5 :z 3}}
                 (fn [old] (* 2 old))))                     ;-> {:x {:y 10, :z 3}}

(println (lset! edn-lens nil 5))                          ;-> "5"
(def edn-xy-lens (lcomp xy-lens edn-lens))
(println (lset! edn-xy-lens nil 5))                       ;-> "{:x {:y 5}}"
(println (lget edn-xy-lens "{:x {:y 5 :z 3}}"))           ;-> 5
(println (lupd! edn-xy-lens "{:x {:y 5 :z 3}}"
                 (fn [old] (* 2 old))))                     ;-> "{:x {:y 10, :z 3}}"
(println)

(def test-lens (-> (printing-lens "left")
                   (lcomp x-lens)
                   (lcomp (printing-lens "right"))))
(println (lset! test-lens {} 5))
;-> right :got {}
;-> left :set nil 5
;-> right :got {}
;-> right :set {} {:x 5}
;-> {:x 5}
(println)
(println (lget test-lens {:x 5 :y 6}))
;-> right :got {:x 5, :y 6}
;-> left :got 5
;-> 5
(println)
(println (lget test-lens {}))
;-> right :got {}
;-> left :got nil
;-> nil
(println)
(println (lset! test-lens nil 5))
;-> right :got nil
;-> left :set nil 5
;-> right :got nil
;-> right :set nil {:x 5}
;-> {:x 5}
(println)
(println (lupd! test-lens {:x 5 :y 6}
                 (fn [old] (* 2 old))))
;-> right :got {:x 5, :y 6}
;-> left :got 5
;-> right :got {:x 5, :y 6}
;-> left :set 5 10
;-> right :got {:x 5, :y 6}
;-> right :set {:x 5, :y 6} {:x 10, :y 6}
;-> {:x 10, :y 6}
(println)

(def test-nil-lens (-> (printing-lens "left")
                       (lcomp (printing-lens "right"))))
(println (lset! test-nil-lens :old :new))
;-> right :got :old
;-> left :set :old :new
;-> right :set :old :new
;-> :new
(println)
(println (lget test-nil-lens :old))
;-> right :got :old
;-> left :got :old
;-> :old
(println)
(println (lupd! test-nil-lens 21
                 (fn [old] (* 2 old))))
;-> right :got 21
;-> left :got 21
;-> right :got 21
;-> left :set 21 42
;-> right :set 21 42
;-> 42
(println)
