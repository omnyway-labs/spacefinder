(defproject spacefinder "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library]]
                 [thheller/shadow-cljs "2.8.48"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.8"]
                 [re-com "2.5.0"]
                 [garden "1.3.9"]
                 [ns-tracker "0.4.0"]
                 [cljsjs/aws-sdk-js "2.421.0-0"]
                 [breaking-point "0.1.2"]]

  :plugins [[lein-garden "0.2.8"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :test-paths   ["test/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"
                                    "resources/public/css"]


  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   spacefinder.css/screen
                     :compiler     {:output-to     "resources/public/css/screen.css"
                                    :pretty-print? true}}]}

  :aliases {"dev"  ["with-profile" "dev" "run" "-m" "shadow.cljs.devtools.cli" "watch" "app"]
            "prod" ["with-profile" "prod" "run" "-m" "shadow.cljs.devtools.cli" "release" "app"]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [day8.re-frame/re-frame-10x "0.4.2"]
                   [day8.re-frame/tracing "0.5.3"]]}

   :prod { :dependencies [[day8.re-frame/tracing-stubs "0.5.3"]]}
   })
