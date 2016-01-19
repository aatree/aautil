(set-env!
  :dependencies '[[adzerk/bootlaces              "0.1.13" :scope "test"]
                  [adzerk/boot-test "1.1.0" :scope "test"]
                  [adzerk/boot-cljs             "1.7.170-3"]
                  [adzerk/boot-reload            "0.4.2"]
                  [compojure                     "1.4.0"]
                  [org.clojure/clojure           "1.7.0"]
                  [org.clojure/clojurescript     "1.7.189"]
                  [pandeiro/boot-http            "0.7.0"]
                  [ring                          "1.4.0"]
                  [ring/ring-defaults            "0.1.5"]
                  [adzerk/cljs-console           "0.1.1"]
                  [org.clojure/tools.logging "0.3.1"]
                  [org.apache.logging.log4j/log4j-core "2.4.1"]
                  [org.apache.logging.log4j/log4j-slf4j-impl "2.4.1"]]
  :resource-paths #{"src/clj" "src/cljs" "src/cljc"})

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-reload    :refer [reload]]
  '[pandeiro.boot-http    :refer [serve]]
  '[adzerk.bootlaces      :refer :all]
  '[adzerk.boot-test :refer :all])

(def +version+ "0.0.1")

(bootlaces! +version+ :dont-modify-paths? true)

(task-options!
  pom {:project     'aatree/aautil
       :version     +version+
       :description "Snippets of useful code."
       :url         "https://github.com/aatree/aautil"
       :scm         {:url "https://github.com/aatree/aautil"}
       :license     {"EPL" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask dev
  "Build project for development."
  []
  (comp
   (cljs)
   (build-jar)))

(deftask testing
  "Profile setup for running tests."
  []
  (set-env! :source-paths #(conj % "test/clj"))
  (set-env! :resource-paths #(conj % "dev-resources"))
  identity)

(deftask test-it
   "Setup, compile and run the tests."
   []
   (comp
     (testing)
     (cljs)
     (run-tests)))

(deftask deploy-release
 "Build for release."
 []
 (comp
   (build-jar)
   (push-release)))