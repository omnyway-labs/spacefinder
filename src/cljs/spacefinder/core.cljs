(ns spacefinder.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [breaking-point.core :as bp]
   [spacefinder.db :as db]
   [spacefinder.views :as views]
   [spacefinder.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (re-frame/dispatch-sync [::db/initialize-db])
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::db/initialize-db])
  ;; (re-frame/dispatch-sync [::bp/set-breakpoints
  ;;                          {:breakpoints [:mobile
  ;;                                         768
  ;;                                         :tablet
  ;;                                         992
  ;;                                         :small-monitor
  ;;                                         1200
  ;;                                         :large-monitor]
  ;;                           :debounce-ms 166}])
  (dev-setup)
  (mount-root))
