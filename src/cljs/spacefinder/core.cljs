(ns spacefinder.core
  (:require
   [reagent.core :as reagent]
   ["aws-amplify-react" :refer (withAuthenticator)]
   [re-frame.core :as re-frame]
   [breaking-point.core :as bp]
   [spacefinder.db :as db]
   [spacefinder.views :as views]
   [spacefinder.config :as config]
   ["aws-amplify" :default Amplify :as amp]
   ))

(def amplify-config
  {:Auth {:identityPoolId "us-east-1:4768a1d8-9cbd-4cfd-8d2a-f5cd2ad5d6a8"
          :region "us-east-1"
          :userPoolId "us-east-1_6xgfyCNPb"
          :userPoolWebClientId "2o5de073jf4dpqo8b1s9davggu"
          }})


(defn configure-amplify []
  (.configure Amplify (clj->js amplify-config)))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(def root-view
  (reagent/adapt-react-class
   (withAuthenticator
    (reagent/reactify-component views/main-panel) true)))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (configure-amplify)
  (re-frame/dispatch-sync [::db/initialize-db])
  (reagent/render [root-view]
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
