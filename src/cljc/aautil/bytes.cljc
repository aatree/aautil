(ns aautil.bytes)

#?(:clj
   (set! *warn-on-reflection* true))

#?(:clj
   (defn make-bytes [s]
     (byte-array s))
   :cljs
   (defn make-bytes [s]
     (js/Int8Array. s)))

#?(:clj
   (defn set-byte! [^"[B" a i v]
     (aset-byte a i v))
   :cljs
   (defn set-byte! [a i v]
     (aset a i v)))

#?(:clj
   (defn get-byte [^"[B" a i]
     (aget a i))
   :cljs
   (defn get-byte [a i]
     (aget a i)))

#?(:clj
   (defn bytes-equal [^"[B" a1 ^"[B" a2]
     (java.util.Arrays/equals a1 a2))
   :cljs
   (defn bytes-equal
     ([a1 a2]
      (let [l1 (alength a1)
            l2 (alength a2)]
        (if (= l1 l2)
          (bytes-equal a1 a2 l1)
          false)))
     ([a1 a2 i]
      (if (= i 0)
        true
        (let [i (dec i)]
          (if (not= (aget a1 i) (aget a2 i))
            false
            (recur a1 a2 i)))))))

#?(:clj
   (defn vec-bytes [a]
     (vec a))
   :cljs
   (defn vec-bytes
     ([a]
      (vec-bytes a (list) (alength a)))
     ([a ls i]
      (if (= i 0)
        (vec ls)
        (let [i (dec i)
              ls (conj ls (aget a i))]
          (recur a ls i))))))
