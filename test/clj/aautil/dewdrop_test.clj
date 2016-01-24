(ns aautil.dewdrop-test
  (:require [aautil.dewdrop :refer :all]))

(def x-lens (key-lens :x))
(println (lreset! x-lens {} 5))
;-> {:x 5}
