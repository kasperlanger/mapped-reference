(ns mapped-reference.associative
  (:use mapped-reference.core))

(defn sub-mapping [key]
  (mapping
   (fn [val] (get val key))
   (fn [old new] (assoc old key new))))
    
     


