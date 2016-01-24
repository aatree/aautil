(ns aautil.closer-test
  (:require [aautil.closer :refer :all]))

(set! *warn-on-reflection* true)

(close-component {})

(defn close-a [this] (println "  close a"))
(defn close-b [this] (println "  close b"))
(defn close-c [this] (println "  close c"))

(let [this (open-trait {} "a" close-a)
      this (open-trait this "b" close-b)
      this (open-trait this "c" close-c)]
  (println "first close")
  (close-component this)
  (println "second close")
  (close-component this))
