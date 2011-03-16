(ns mapped-reference.float
  (:use mapped-reference.core))

(defn bijective-mapping
  [f f-inv]
  (mapping
   (fn [x] (f x))
   (fn [old y] (f-inv y))))

(defn affine-mapping
  [a b]
  (bijective-mapping
   #(+ (* % a) b)
   #(/ (- % b) a)))

(def float-to-str
     (bijective-mapping (comp str float) #(Float/parseFloat %)))

(def str-to-float
     (bijective-mapping #(Float/parseFloat %) (comp str float)))