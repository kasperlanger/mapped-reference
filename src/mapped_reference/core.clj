(ns mapped-reference.core
  (:use clojure.contrib.generic.functor))

(defprotocol RepresentationProtocol
  (rep-swap! [this fun]))

(extend clojure.lang.Atom
  RepresentationProtocol {:rep-swap! clojure.core/swap!})
    
(defrecord Representation [reference rep-fn ref-update secret-key]
  RepresentationProtocol
  (rep-swap! [this fun]
	     (rep-fn (rep-swap! reference
				(fn [old]
				  (ref-update old (fun (rep-fn old)))))))

  clojure.lang.IRef
  (deref [this] (rep-fn @reference))
  (removeWatch [this key] (remove-watch reference [secret-key key]))
  (addWatch [this key callback]
	    (add-watch reference
		       [secret-key key] 
		       (fn [_ _ old-val new-val]
			 (let [old-rep (rep-fn old-val)
			       new-rep (rep-fn new-val)]
			   (when (not= old-rep new-rep)
			     (callback key this old-rep new-rep)))))))
  
(defn rep [atom-ref rep-fn ref-update]
  (Representation. atom-ref rep-fn ref-update (gensym)))

(defn mapping
  [rep-fn ref-update]
  (fn [ref-kw-val]
    (condp = ref-kw-val
	:rep-fn rep-fn
	:ref-update ref-update
	(if (satisfies? RepresentationProtocol ref-kw-val)
	  (rep ref-kw-val rep-fn ref-update)
	  (rep-fn ref-kw-val)))))