(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'tsu-nera/meingen-bot-firebase)
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
