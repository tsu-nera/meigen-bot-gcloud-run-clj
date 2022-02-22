(defproject meigen-bot "0.1.0-SNAPSHOT"
  :description "Twitter Bot written in Clojure."
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [environ "1.2.0"]
                 [cheshire "5.10.2"]
                 [clj-http "3.12.3"]
                 [jarohen/chime "0.3.3"]
                 [com.taoensso/timbre "5.1.2"]
                 ;; Goole Cloud
                 [com.google.auth/google-auth-library-oauth2-http "1.4.0"]
                 [com.google.cloud/google-cloud-firestore "3.0.2"]
                 [com.google.firebase/firebase-admin "8.1.0"]
                 [com.google.cloud.functions/functions-framework-api "1.0.4"]
                 ]
  :plugins [[com.google.cloud.functions/function-maven-plugin "0.10.0"]]
  :main meigen-bot.core
  :repl-options {:init-ns meigen-bot.core}
  :profiles {:uberjar {:aot :all}}
  )
