(ns aautil.dewdrop
  #?(:clj (:require [clojure.string :as str]
                    [clojure.edn :refer [read-string]])
     :cljs (:require [clojure.string :as str]
             [cljs.reader :refer [read-string]]))
  #?(:clj (:refer-clojure :exclude [read-string]))
  #?(:clj (:import (clojure.lang IDeref IAtom))))

(defn new-lens
  "Create a new lens."
  [getter setter]
  {:getter getter :setter setter})

(defn lget
  "Extract an item from some data.
  Returns the extracted item."
  [lens data]
  ((:getter lens) data))

(defn lderef
  "Extract an item from data held by an atom.
  Returns the extracted item."
  [lens data-atom]
  (lget lens @data-atom))

(defn lset!
  "Revise some data with an item.
  Returns the revised data."
  [lens data item]
  ((:setter lens) data item))

(defn lreset!
  "Revise some data held by an atom with an item.
  Returns the revised data."
  [lens data-atom item]
  (reset! data-atom (lset! lens @data-atom item)))

(defn lupd!
  "Update an item in some data.
  Returns the revised data."
  [lens data f]
  (lset! lens data (f (lget lens data))))

(defn lswap!
  "Update an item in some data held by an atom.
  Returns the revised data."
  [lens data-atom f]
  (swap! data-atom
         (fn [data]
           (lupd! lens data f))))

(defn lcomp
  "Combine a lens with another."
  [left right]
  {:getter
   (fn [data] (lget left (lget right data)))
   :setter
   (fn [data item]
     (let [right-data (lget right data)
           left-data (lset! left right-data item)]
       (lset! right data left-data)))})

(defn key-lens
  "Builds a lens using get and assoc"
  [key]
  {:getter (fn [data] (get data key))
   :setter (fn [data item] (assoc data key item))})

(defn atom-key-lens
  "Builds a lens using get and assoc"
  [key-atom]
  {:getter (fn [data] (get data @key-atom))
   :setter (fn [data item] (assoc data @key-atom item))})

;A lens built using read-string and pr-str.
(def edn-lens
  {:getter read-string
   :setter (fn [_ item] (pr-str item))})

(defn printing-lens
  "A lens for debugging"
  [id]
  {:getter (fn [item]
             (println id :got item)
             item)
   :setter (fn [data item]
             (println id :set data item)
             item)})

(defrecord lens-view [lens data-atom]
  IDeref
  (deref [this] (lderef lens data-atom))
  IAtom
  (reset [this item] (lreset! lens data-atom item))
  (swap [this f] (lswap! lens data-atom f))
  (swap [this f arg]
    (swap! data-atom
           (fn [data]
             (lset! lens data (f (lget lens data) arg)))))
  (swap [this f arg1 arg2]
    (swap! data-atom
           (fn [data]
             (lset! lens data (f (lget lens data) arg1 arg2)))))
  (swap [this f x y args]
    (swap! data-atom
           (fn [data]
             (lset! lens data (apply f (lget lens data) x y args)))))
  (^boolean compareAndSet [this oldv newv]
    (swap! data-atom
           (fn [data]
             (let [v (lget lens data)]
               (if (= oldv v)
                 (lset! lens data newv)
                 data)))))
  )

(defn lview [lens data-atom]
  (->lens-view lens data-atom))