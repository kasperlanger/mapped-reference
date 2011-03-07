(ns mapped-reference.example-associative
  (:use mapped-reference.associative
	mapped-reference.core))

(def m (atom {:x 3, :y {:a 42 :b 101}}))

(def ref-x (sub-atom m :x))

(def ref-b (sub-atom (sub-atom m :y) :b))

(println @m) ;; {:x 3, :y {:a 42, :b 101}}

(mapped-swap! ref-x inc)
(println @m) ;; {:x 4, :y {:a 42, :b 101}}

(mapped-swap! ref-b inc)
(println @m) ;; {:x 4, :y {:a 42, :b 102}}

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def v (atom [1 2 3]))

(def second-entry (sub-atom v 1))

(mapped-swap! second-entry dec)
(println @v) ;; [1 1 3]

(mapped-swap! second-entry dec)
(println @v) ;; [1 0 3]