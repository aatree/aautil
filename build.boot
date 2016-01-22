(set-env!
  :dependencies '[[org.clojure/clojure                       "1.8.0"  :scope "provided"]
                  [org.clojure/clojurescript                 "1.7.228" :scope "provided"]
                  [org.clojure/tools.logging                 "0.3.1"]
                  [org.apache.logging.log4j/log4j-core       "2.5" :scope "test"]
                  [org.apache.logging.log4j/log4j-slf4j-impl "2.5" :scope "test"]
                  [adzerk/bootlaces                          "0.1.13" :scope "test"]
                  [adzerk/boot-test                          "1.1.0" :scope "test"]
                  [adzerk/boot-cljs                          "1.7.228-1" :scope "test"]
                  [crisptrutski/boot-cljs-test               "0.2.1" :scope "test"]]
  :source-paths #{"test/cljs"} ; "test/clj" "dev-resources"}
;  :resource-paths #{"src/clj" "src/cljs" "src/cljc"}
)

(require
  '[adzerk.boot-cljs            :refer [cljs]]
  '[adzerk.bootlaces            :refer :all]
  '[adzerk.boot-test            :refer :all]
  '[crisptrutski.boot-cljs-test :refer [test-cljs]])

(def +version+ "0.0.1")

(bootlaces! +version+ :dont-modify-paths? true)

(task-options!
  pom {:project     'aatree/aautil
       :version     +version+
       :description "Snippets of useful code."
       :url         "https://github.com/aatree/aautil"
       :scm         {:url "https://github.com/aatree/aautil"}
       :license     {"EPL" "http://www.eclipse.org/legal/epl-v10.html"}}
  test-cljs {:js-env :phantom
             :namespaces '#{aautil.simple-test}
             :update-fs? true
             :optimizations :none})

(deftask dev
  "Build project for development."
  []
  (comp
   (build-jar)))

(deftask test-it
   "Setup, compile and run the tests."
   []
   (comp
;     (cljs :source-map true)
     (test-cljs)
;     (run-tests)
     ))

(deftask deploy-release
 "Build for release."
 []
 (comp
   (build-jar)
   (push-release)))