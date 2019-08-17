(ns spacefinder.db
  (:require
   [re-frame.core :as rf]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(def default-db
  {:name "spacefinder"})


(rf/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            default-db))
