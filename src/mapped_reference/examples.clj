(ns mapped-reference.examples
  (:use (mapped-reference float core)))

(def celcius (atom 0))

(def farenheit (affine-atom celcius 9/5 32))

(defn pr-status []
  (println "c:" @celcius "f:" @farenheit))

(pr-status) ;; c: 0 f: 32.0

(rep-swap! celcius inc)

(pr-status) ;; c: 1 f: 33.8

(rep-swap! farenheit inc)

(pr-status) ;; c: 1.5555552 f: 34.8

(def cv (multi-rep :celcius celcius :farenheit farenheit))

(def a (atom 0))
; (def aev (compound-view :celcius a :farenheit farenheit))
					; => throws IllegalArgumentException

(remove-watch cv :foo)
(add-watch cv :foo #(println %4))