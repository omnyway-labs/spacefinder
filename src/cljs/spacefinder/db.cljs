(ns spacefinder.db
  (:require
   [re-frame.core :as rf]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(def default-db
  {:name "spacefinder"
   :views {
           :username "user1"
           :password "Test123!"
           }})


(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
            default-db))
