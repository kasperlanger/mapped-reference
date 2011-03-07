(ns mapped-reference.float
  (:use mapped-reference.core))

(defn affine-atom [atom-ref a b]
  "atom-ref should contain a floating point value. Maps x to x*a + b: @affine-atom -> (+ (* @atom-ref a) b))"
  (mapped-atom atom-ref
	       (fn [x] (float (+ (* x a) b)))
	       (fn [_ y] (float (/ (- y b) a)))))
