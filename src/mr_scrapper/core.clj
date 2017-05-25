(ns mr-scrapper.core
  (:require [clojure.set :refer :all]
            [clojure.string :as str]
            [net.cgrand.enlive-html :as html]))

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))
