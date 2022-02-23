(ns meigen-bot.core
  (:gen-class)
  (:import (java.time Instant Duration))
  (:require
   [taoensso.timbre :as log]
   [meigen-bot.runner :as runner]
   [meigen-bot.firebase :refer [init-firebase-app!]]
   [chime.core :as chime]))


(def timbre-config {:timestamp-opts {:pattern  "yyyy-MM-dd HH:mm:ss,SSS"
                                     :locale   (java.util.Locale. "ja_JP")
                                     :tiemzone (java.util.TimeZone/getTimeZone "Asia/Tokyo")}})

(log/merge-config! timbre-config)
(init-firebase-app!)

(defn -main [& args]
  (println "======================================")
  (println "Started up Twitter Bot.")
  (chime/chime-at (chime/periodic-seq
                   (Instant/now)
                   (Duration/ofHours 1)
                   ;;(Duration/ofMinutes 3)
                   )
                  (fn [_]
                    (runner/tweet-random))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; (def status (make-status (pick-random)))
;; (def response (private/update-status status))

;; (tweet-random)

;; (def status (guest/get-status "1477034578875277316"))
;; (def status (private/get-status "1477034578875277316"))

;; status
