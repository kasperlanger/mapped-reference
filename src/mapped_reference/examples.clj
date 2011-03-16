(ns mapped-reference.examples
  (:use (mapped-reference float core associative)))

(def c-to-f (affine-mapping 9/5 32))

(c-to-f 0) ;; -> 32
(c-to-f 10) ;; -> 50

(def c (atom 0))
(def f (c-to-f c))

@c ;; -> 0
@f ;; -> 32
(rep-swap! c inc) ;; -> 1
@f ;; -> 169/5
(float @f) ;;-> 33.8

(def f-str (-> c c-to-f float-to-str))
@f-str ;;-> 33.8
(rep-swap! f-str (constantly "32")) ;; -> "32.0"
@c ;;-> 0.0

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def m (atom {:x 5 :y 7}))

(let [doubler (bijective-mapping (partial * 2) (partial * 0.5))
      select-x (sub-mapping :x)]
  (def x (-> m select-x doubler)))

@x ;; -> 10
(rep-swap! x inc) ;; -> 11.0
@m ;; -> {:x 5.5, :y 7}

