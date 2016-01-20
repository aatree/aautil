(ns aautil.closer
  (:require [aautil.aalog :as log]))

#?(:clj
   (set! *warn-on-reflection* true))

(defn open-component [this name f]
  (log/info (str "opening " name))
  (if-let [fsa (:closer-fsa this)]
    (do
      (swap! fsa
             (fn [fs]
               (if fs
                 (conj fs [f name])
                 (atom (list [f name])))))
      this)
    (assoc this :closer-fsa (atom (list [f name])))))

(defn- do-closer [this fs]
  (when fs
    (let [fv (first fs)
          f (nth fv 0)
          name (nth fv 1)]
      (try
        (log/info (str "closing " name))
        (f this)
        (catch #?(:clj Exception :cljs js/Object) e
          (log/warn e (str "exception on close of " name)))))
    (recur this (next fs))))

(defn close-components [this]
  (if-let [fsa (:closer-fsa this)]
    (let [fs @fsa]
      (if fs
        (if (compare-and-set! fsa fs nil)
          (do-closer this fs)
          (recur this))))))
