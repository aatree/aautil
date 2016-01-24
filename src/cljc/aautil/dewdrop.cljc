(ns aautil.dewdrop)

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

(defn lcompose
  "Combine a lens with another."
  [this that]
  {:getter
   (fn [data] ((:getter that) ((:getter this) data)))
   :setter
   (fn [data item]
     (let [this-data (lderef this data)
           that-data (lreset! that this-data item)]
       (lreset! this data that-data)))})

(defn key-lens
  "A lens built using get and assoc"
  [key]
  {:getter
   (fn [data] (get data key))
   :setter
   (fn [data item] (assoc data key item))})
