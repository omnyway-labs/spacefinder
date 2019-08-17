(ns spacefinder.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [spacefinder.core-test]))

(doo-tests 'spacefinder.core-test)
