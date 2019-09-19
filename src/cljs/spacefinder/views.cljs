(ns spacefinder.views
  (:require
   [re-frame.core :as rf]
   [re-com.core :as rc :refer-macros [handler-fn]]
   ["aws-sdk" :as AWS]
   ["amazon-cognito-identity-js" :as AmazonCognitoIdentity]
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
   (get-in db [:views :selected-user-nav-id])))

(rf/reg-sub
 :username
 (fn [db _]
   (get-in db [:views :username])))

(rf/reg-sub
 :password
 (fn [db _]
   (get-in db [:views :password])))

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


(def config {
             :user-pool-id               "us-east-1_6xgfyCNPb"
             :user-pool-domain-name      "https://spacefinder-development-us-east-1-6xgfycnpb.auth.us-east-1.amazoncognito.com"
             :user-pool-domain-prefix    "spacefinder-development-us-east-1-6xgfycnpb"
             :client-id                  "2o5de073jf4dpqo8b1s9davggu"
             :identity-pool-id           "us-east-1:57924bcf-8eba-4487-8397-4d90999f3aab"
             :region                     "us-east-1",  ; Your AWS region where you setup your Cognito User Pool and Federated Identities

             :profile-images-s3-bucket   "spacefinder-development-stack-userdatabucket-11wtuhxdpzbp5"

             :api-endpoint               "https://g2drvofsu8.execute-api.us-east-1.amazonaws.com/development"

             :developer-mode             false ; enable to automatically login
             :code-version               "1.0.0"
             :default-usernames          ["user1", "admin1"] ; default users cannot change their passwords
             })

(defn get-user-pool []
  (let [pool-data (clj->js {:UserPoolId (:user-pool-id config)
                            :ClientId (:client-id config)})]
    (aset AWS/config "region" (:region config))
                                        ;(aset AmazonCognitoIdentity/config "region" (:region config))
     ;; (aset AWS/config "credentials" (AWS/CognitoIdentityCredentials
     ;;                                 (clj->js {:IdentityPoolId (:identity-pool-id config) :LoginId @(rf/subscribe [:username])})))

                                        ; Initialize AWS config object with dummy keys -
                                        ; required if unauthenticated access is not enabled for identity pool
    ;(aset AWS/config "update" (clj->js {:accessKeyId "dummyvalue" :secretAccessKey "dummyvalue"}))
    (AmazonCognitoIdentity/CognitoUserPool. pool-data)))

(defn get-cognito-user []
  (let [user-data (clj->js {:Username @(rf/subscribe [:username]) :Pool (get-user-pool)})]
    (js/console.log "user-data: " user-data)
    (AmazonCognitoIdentity/CognitoUser. user-data)))

;; TODO Complete Signin Example: https://github.com/jmglov/hello-world.se-ddraw/blob/master/src/cljs/ddraw/cognito.cljs
(defn signin []
  (let [authentication-details (AmazonCognitoIdentity/AuthenticationDetails.
                                (clj->js {:Username @(rf/subscribe [:username])
                                          :Password @(rf/subscribe [:password])}))
        cognito-user (get-cognito-user)]
    (println "Getting ready to authenticate")
    (.authenticateUser cognito-user authentication-details
                     (clj->js {:onSuccess
                               (fn [res]
                                 (let [access-token (-> res .getAccessToken .getJwtToken)
                                       id-token (-> res .getIdToken .getJwtToken)
                                       pool-path (str "cognito-idp." (:region config)
                                                      ".amazonaws.com/" (:user-pool-id config))]
                                   (println "Access token" access-token)
                                   (println "ID token" id-token)
                                   (println "pool-path" pool-path)
                                   (println "identity-pool-id" (:identity-pool-id config))
                                   ;; (aset AWS/config "region" (:region config))
                                   ;; (aset AWS/config "update" (clj->js {:accessKeyId "dummyvalue" :secretAccessKey "dummyvalue"}))
                                   (aset AWS/config "credentials"
                                         (AWS/CognitoIdentityCredentials.
                                          (clj->js {:IdentityPoolId (:identity-pool-id config)
                                                    :Logins {pool-path
                                                             id-token}})))
                                   (js/console.log "AWS/Config.credentials" AWS/config.credentials)
                                   (AWS/config.credentials.refresh
                                    (fn [err]
                                      (if err
                                        (println "Refresh error:" err)
                                        (println "Refresh success AWS/Credentials.accessKeyId" AWS/Credentials.accessKeyId))))
                                   ))
                               :onFailure
                               (fn [err]
                                 (println "error" err))}
                              ))))

(defn sign-up []
  (js/console.log "sign-up"))

(defn sign-in-custom-ui []
  (js/console.log "sign-in-custom-ui")
  (signin))

(defn sign-in-hosted-ui []
  (js/console.log "sign-in-hosted-ui")
  (js/console.log "sign-in-hosted-ui AWS/Config.credentials" AWS/config.credentials))

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
