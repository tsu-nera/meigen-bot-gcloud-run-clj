(ns functions.tweet
  (:require
   [cheshire.core :as json]
   [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.util.response :as response]
   [ring.middleware.params :refer [wrap-params]]
   [meigen-bot.runner :as runner]
   [meigen-bot.firebase :refer [init-firebase-app-prod!]]
   ))


(defn handler [req]
  ;; (prn req)
  (init-firebase-app-prod!)
  (let [;; params (:params req)
        ;; status (:status params)
        tweet (runner/tweet-random)]
    (response/response "OK")))


(def app
  (-> handler
      wrap-keyword-params
      wrap-json-params
      wrap-json-response
      wrap-params
      ))
