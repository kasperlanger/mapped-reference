(ns mapped-reference.core
  (:use clojure.contrib.generic.functor))

(defprotocol RepresentationProtocol
  (rep-swap! [this fun]))

(extend clojure.lang.Atom
  RepresentationProtocol {:rep-swap! clojure.core/swap!})
    
(defrecord Representation [reference rep-fn ref-update]
  RepresentationProtocol
  (rep-swap! [this fun]
	     (rep-fn (rep-swap! reference
				(fn [old]
				  (ref-update old (fun (rep-fn old)))))))

  clojure.lang.IRef
  (deref [this] (rep-fn @reference))
  (removeWatch [this key] (remove-watch reference [this key]))
  (addWatch [this key callback]
	    (add-watch reference
		       [this key] 
		       (fn [key ref old-val new-val]
			 (let [old-rep (rep-fn old-val)
			       new-rep (rep-fn new-val)]
			   (when (not= old-rep new-rep)
			     (callback key ref old-rep new-rep)))))))
  
(defn rep [atom-ref rep-fn ref-update]
  (Representation. atom-ref rep-fn ref-update))

(defrecord MultipleRepresentation [reference reps-map]
  clojure.lang.IRef
  (deref [this]
	 (let [val @reference]
	   (fmap #((:rep-fn %) val) reps-map)))
  (removeWatch [this key] (remove-watch reference [this key]))
  (addWatch [this key callback]
	    (add-watch reference
		       [this key]
		       (fn [key ref old-val new-val]
			 (let [old-rep (fmap #((:rep-fn %) old-val) reps-map)
			       new-rep (fmap #((:rep-fn %) new-val) reps-map)]
			   (when (not= old-rep new-rep)
			     (callback key ref old-rep new-rep)))))))
		         
(defn multi-rep [& args]
  (let [m (apply hash-map args)
	wrap (fn [ref] (rep ref identity (fn [_ val] val)))
	m (fmap (fn [v] (if (= (type v) Representation) v (wrap v))) m)
	same-ref? (apply = (map :reference (vals m)))]
    (if (not same-ref?)
      (throw (IllegalArgumentException.
	      (str
	       "arguments do not represent the same reference."
	       "foo"
	      (map :reference (vals m))
	      )))
      (MultipleRepresentation. (:reference (first (vals m))) m))))