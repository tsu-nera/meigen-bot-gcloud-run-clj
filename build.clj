(ns build
  (:require [clojure.tools.build.api :as b]
            [tools-pom.tasks :as pom]))

(def lib 'tsu-nera/meingen-bot-gcloud-run)
(def version (format "0.1.%s" (b/git-count-revs nil)))

(def target-dir "target")
(def class-dir (str target-dir "/" "classes"))
(def artifacts-dir (str target-dir "/" "artifacts"))
(def jar-file (format "%s/%s-%s-application.jar" artifacts-dir (name lib) version))
(def uber-file (format "%s/application.jar" artifacts-dir))

(def src-clj ["src/main/clojure"])
(def src-java ["src/main/java"])
(def src [src-clj src-java])
(def basis (b/create-basis {:project "deps.edn"}))

;; clj -T:build clean
(defn clean [_]
  "Delete the build target directory"
  (println (str "Cleaning... " target-dir))
  (b/delete {:path "target"}))

(defn compile-java [_]
  (println (str "Compiling... " src-java))
  (b/javac {:src-dirs   src-java
            :class-dir  class-dir
            :basis      basis
            :javac-opts ["-source" "11" "-target" "11"]
            }))

;; clj -T:build jar
(defn jar [_]
  (b/write-pom {:class-dir class-dir
                :lib       lib
                :version   version
                :basis     basis
                :src-dirs  src-clj})
  (b/copy-dir {:src-dirs   src-clj
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file  jar-file}))

;; clj -T:build uber
(defn uber [_]
  (clean nil)
  (compile-java nil)
  (b/copy-dir {:src-dirs   src-clj
               :target-dir class-dir})
  (b/compile-clj {:basis     basis
                  :src-dirs  src-clj
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis     basis
           }))

(defn- set-opts [opts]
  (assoc opts
         :lib          lib
         :version      version
         :write-pom    true
         ;; :validate-pom true
                                        ; Note: this EDN can come from anywhere - you could externalise it into a separate edn file (e.g. pom.edn), synthesise it from information elsewhere in your project, or whatever other scheme you like
         ;; :pom
         ;; {:description      "Description of your project e.g. your project's GitHub \"short description\"."
         ;;  :url              "https://github.com/yourusername/yourproject"
         ;;  :licenses         [:license   {:name "Apache License 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}] ; Note first element is a tag
         ;;  :developers       [:developer {:id "yourusername" :name "yourname" :email "youremail@emailservice.com"}]           ; And here
         ;;  :scm              {:url                  "https://github.com/yourusername/yourproject"
         ;;                     :connection           "scm:git:git://github.com/yourusername/yourproject.git"
         ;;                     :developer-connection "scm:git:ssh://git@github.com/yourusername/yourproject.git"}
         ;;  :issue-management {:system "github" :url "https://github.com/yourusername/yourproject/issues"}}
         ))

(defn pom
  "Construct a comprehensive pom.xml file for this project"
  [opts]
  (-> opts
      (set-opts)
      (pom/pom)))
