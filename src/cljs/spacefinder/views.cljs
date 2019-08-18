(ns spacefinder.views
  (:require
   [re-frame.core :as rf]
   [re-com.core :as rc :refer-macros [handler-fn]]
   [amazon-cognito-identity-js]
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

(rf/reg-sub
 :username
 (fn [db _]
   (get-in [:views :username] db)))

(rf/reg-sub
 :password
 (fn [db _]
   (get-in [:views :password] db)))

;; -------- Dispatchers
(rf/reg-event-db
 ::set-selected-user-nav-id
 (fn [db [_ selected-user-nav-id]]
   (assoc-in db [:views :selected-user-nav-id] selected-user-nav-id)))

(rf/reg-event-db
 ::set-username
 (fn [db [_ username]]
   (assoc-in db [:views :username] username)))

(rf/reg-event-db
 ::set-password
 (fn [db [_ password]]
   (assoc-in db [:views :password] password)))


;; ---------

(defn sign-up []
  (js/console.log "sign-up"))

(defn sign-in-custom-ui []
  (js/console.log "sign-in-custom-ui"))

(defn sign-in-hosted-ui []
  (js/console.log "sign-in-hosted-ui"))


(def config {
             :user_pool_id               "us-east-1_79D6i69td"
             :user_pool_domain_name      "spacefinder-development-us-east-1-79d6i69td.auth.us-east-1.amazoncognito.com"
             :user_pool_domain_prefix    "spacefinder-development-us-east-1-79d6i69td"
             :client_id                  "58pvvcoktlrhcng34ve68i74uf"
             :identity_pool_id           "us-east-1:aac5e9e6-ff3a-4586-b984-2cd88b693f69"
             :region                     "us-east-1",  ; Your AWS region where you setup your Cognito User Pool and Federated Identities

             :profile_images_s3_bucket   "spacefinder-development-stack-userdatabucket-11wtuhxdpzbp5"

             :api_endpoint               "https://g2drvofsu8.execute-api.us-east-1.amazonaws.com/development"

             :developer_mode             false ; enable to automatically login
             :code_version               "1.0.0"
             :default_usernames          ["user1", "admin1"] ; default users cannot change their passwords
             })

(defn get-user-pool []
  (let [pool-data (clj->js {:UserPoolId (:user_pool_id config)
                            :ClientId (:client_id config)})]
    (aset js/AWSCognito.config "region" (:region config))
    (aset js/AWSCognito.config "credentials" (js/AWS.CognitoIdentityCredentials.
                                              (clj->js {:IdentityPoolId (:identity-pool-id config)})))

                                        ; Initialize AWS config object with dummy keys -
                                        ; required if unauthenticated access is not enabled for identity pool
    (aset js/AWSCognito.config "update" (clj->js {:accessKeyId "dummyvalue" :secretAccessKey "dummyvalue"}))
    (js/AWSCognito.CognitoIdentityServiceProvider.CognitoUserPool pool-data)))

(defn get-congito-user []
  (let [user-data (clj->js {:Username @(rf/subscribe [:username]) :Pool (get-user-pool)})]
    (js/AWSCognito.CognitoIdentityServiceProvider.CognitoUser user-data)))

;; TODO Complete Signin Example: https://github.com/jmglov/hello-world.se-ddraw/blob/master/src/cljs/ddraw/cognito.cljs


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
