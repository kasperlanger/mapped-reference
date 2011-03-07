(ns mapped-reference.associative
  (:use mapped-reference.core))

(defn sub-atom [atom-ref path]
  (mapped-atom atom-ref path (fn [old val] (assoc old path val))))