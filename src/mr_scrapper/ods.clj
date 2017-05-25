(ns mr-scrapper.ods
  (:require [clojure.set :refer :all]
            [clojure.string :as str]))

(def testurl "http://agenda2030.mx/indicadores.html?objetivo=1.&meta=ODS00100010&indicador=1&codigo=N%20%C2%A0%C2%A0%C2%A0&obj=ODS0010")

(def testurl& "http://104.236.211.149/ods/indicadores.html?objetivo=1.&meta=ODS00100010&indicador=1&codigo=N%20%C2%A0%C2%A0%C2%A0&obj=ODS0010")
