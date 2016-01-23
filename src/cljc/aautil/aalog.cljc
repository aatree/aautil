;; From https://gist.github.com/caskolkm/39d823f5bac7051d3062
;; but extensively modified.
(ns aautil.aalog
  (:refer-clojure :exclude [time])
  (:require #?(:clj  [clojure.tools.logging :as log])))

#?(:clj
   (set! *warn-on-reflection* true))

(defn fmt [msgs]
  (apply str (interpose " " (map pr-str msgs))))

(defn info [& s]
  (let [msg (fmt s)]
    #?(:clj  (log/info msg)
       :cljs (.info js/console msg))))

;small enhancement
(defn warn [& s]
  (let [msg (fmt s)]
    #?(:clj  (log/warn msg)
       :cljs (.warn js/console msg))))

(defn debug [& s]
  (let [msg (fmt s)]
    #?(:clj  (log/debug msg)
       :cljs (.log js/console msg))))

(defn error [& s]
  (let [msg (fmt s)]
    #?(:clj (log/error msg)
       :cljs (.error js/console msg))))
