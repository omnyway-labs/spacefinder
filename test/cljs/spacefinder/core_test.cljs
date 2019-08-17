(ns spacefinder.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [spacefinder.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
