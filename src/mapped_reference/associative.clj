(ns mapped-reference.associative
  (:use mapped-reference.core))

(defn sub-atom [atom-ref key]
  (mapped-atom atom-ref #(get % key) (fn [old val] (assoc old key val))))