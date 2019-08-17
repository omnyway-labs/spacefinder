(ns spacefinder.views
  (:require
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [breaking-point.core :as bp]
   [spacefinder.subs :as subs]
   ))

(defn title []
  (let [name (re-frame/subscribe [::subs/name])]
    [re-com/title
     :label (str "Hello from " @name)
     :level :level1]))

(defn main-panel []
  [re-com/v-box
   :height "100%"
   :children [[title]
              [:div
               [:h3 (str "screen-width: " @(re-frame/subscribe [::bp/screen-width]))]
               [:h3 (str "screen: " @(re-frame/subscribe [::bp/screen]))]]
              ]])
