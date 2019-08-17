(ns spacefinder.views
  (:require
   [re-frame.core :as rf]
   [re-com.core :as rc :refer-macros [handler-fn]]
   [breaking-point.core :as bp]
   ))

;; -------- Subscriptions
(rf/reg-sub
 :name
 (fn [db]
   (:name db)))

(rf/reg-sub
 :selected-user-nav-id
 (fn [db _]
   (get-in [:views :selected-user-nav-id] db)))

;; -------- Dispatchers
(rf/reg-event-db
 ::set-selected-user-nav-id
 (fn [db [_ select-user-nav-id]]
   (assoc-in db [:views :selected-user-nav-id] select-user-nav-id)))


;; ---------

(defn sign-up []
  (js/console.log "sign-up"))

(defn sign-in-custom-ui []
  (js/console.log "sign-in-custom-ui"))

(defn sign-in-hosted-ui []
  (js/console.log "sign-in-hosted-ui"))


(defn title []
  (let [name (rf/subscribe [:name])]
    [rc/title
     :label (str "Hello from " @name)
     :level :level1]))

(defn initial-navbar []
  [rc/h-box
   :justify :center
   :align   :center
   :height  "62px"
   :style   {:background-color "#666"}
   :children [[rc/button
               :label "Sign-Up"
               :on-click (handler-fn (sign-up))]
              [rc/button
               :label "Sign-In (Custom UI)"
               :on-click (handler-fn (sign-in-custom-ui))]
              [rc/button
               :label "Sign-In (Hosted UI)"
               :on-click (handler-fn (sign-in-hosted-ui))]
              ]])


(defn main-panel []
  [rc/v-box
   :height "100%"
   :children [[initial-navbar]
              ]])
