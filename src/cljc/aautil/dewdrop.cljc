(ns aautil.dewdrop
  #?(:clj (:require [clojure.string :as str]
                    [clojure.edn :refer [read-string]])
     :cljs (:require [clojure.string :as str]
             [cljs.reader :refer [read-string]]))
  #?(:clj (:refer-clojure :exclude [read-string])))

(defn new-lens
  "Create a new lens."
  [getter setter]
  {:getter getter :setter setter})

(defn lderef
  "Extract an item from some data.
  Returns the extracted item."
  [this data]
  ((:getter this) data))

(defn lreset!
  "Revise some data with an item.
  Returns the revised data."
  [this data item]
  ((:setter this) data item))

(defn lswap!
  "Update an item in some data.
  Returns the revised data."
  [this data f]
  (let [old (lderef this data)]
    (lreset! this data (f old))))

(defn lcomp
  "Combine a lens with another."
  [right left]
  {:getter
   (fn [data] ((:getter left) ((:getter right) data)))
   :setter
   (fn [data item]
     (let [right-data (lderef right data)
           left-data (lreset! left right-data item)]
       (lreset! right data left-data)))})

(defn key-lens
  "Builds a lens using get and assoc"
  [key]
  {:getter (fn [data] (get data key))
   :setter (fn [data item] (assoc data key item))})

(defn key-atom-lens
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