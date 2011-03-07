(ns mapped-reference.examples
  (:use (mapped-reference float core)))

(def celcius (atom 0))

(def farenheit (affine-atom celcius 9/5 32))

(defn pr-status []
  (println "c:" @celcius "f:" @farenheit))

(pr-status) ;; c: 0 f: 32.0

(mapped-swap! celcius inc)

(pr-status) ;; c: 1 f: 33.8

(mapped-swap! farenheit inc)

(pr-status) ;; c: 1.5555552 f: 34.8
