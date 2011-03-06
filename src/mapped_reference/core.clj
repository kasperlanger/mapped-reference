(ns mapped-reference.core)

(defprotocol MappedAtomProtocol
  (mapped-swap! [this fn]))

(extend clojure.lang.Atom
  MappedAtomProtocol
  {:mapped-swap! clojure.core/swap!})
    
(deftype MappedAtom [atom-ref map-fn update-fn]
  MappedAtomProtocol
  (mapped-swap! [this fun]
		(map-fn (mapped-swap!
				atom-ref 
				(fn [old] (update-fn old (fun (map-fn old)))))))

  clojure.lang.IRef
  (deref [this] (map-fn @atom-ref))
  (removeWatch [this key] (remove-watch atom-ref [this key]))
  (addWatch [this key callback]
	    (add-watch atom-ref
		       [this key] 
		       (fn [key ref old-val new-val]
			 (let [mapped-old (map-fn old-val)
			       mapped-new (map-fn new-val)]
			   (when (not= mapped-old mapped-new)
			     (callback key ref mapped-old mapped-new)))))))
  
(defn mapped-atom [atom-ref map-fn update-fn]
  (MappedAtom. atom-ref map-fn update-fn))
