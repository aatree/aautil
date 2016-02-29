(ns aautil.cs256
  (:require [aautil.bytes :as bytes]
            [octet.core :as buf]))

(defn make-cs256 []
  (bytes/make-bytes 33))

(defn cs256-equal [cs1 cs2]
  (bytes/bytes-equal cs1 cs2))

(defn getu [ba i]
  (+ 128 (bytes/get-byte ba i)))

(defn setu! [ba i v]
  (bytes/set-byte! ba i (- (bit-and 255 v) 128)))

(def exp [1 2 4 8 16 32 64 128])

(defn digest-byte! [cs b]
  (let [i (getu cs 32)
        j (rem (+ i b) 256)
        k (quot j 8)
        l (rem j 8)
        msk (exp l)
        v (getu cs k)]
    (setu! cs k (bit-xor v msk))
    (setu! cs 32 (inc i))))

(defn digest!
  ([cs ba] (digest! cs ba 0))
  ([cs ba i]
   (when (< i (alength ba))
     (digest-byte! cs (getu ba i))
     (recur cs ba (inc i)))))

(def cs256-spec (buf/bytes 33))
