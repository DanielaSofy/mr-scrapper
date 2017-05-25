(ns mr-scrapper.diputados
  (:require [clojure.string :as str]
            [mr-scrapper.core :refer :all]
            [digitalize.core :refer :all]
            [formaterr.core :refer :all]
            [net.cgrand.enlive-html :as html]
            [stencil.core :refer :all]))

(def ^:dynamic *diputados-base-url* "http://www.diputados.gob.mx/LeyesBiblio/index.htm")

(comment (defn tables []
  (-> (html/select (fetch-url *diputados-base-url*) [:table#table3])
      first
      :content)))
(defn tables []
  (html/select (fetch-url *diputados-base-url*) [:table]))

(defn la-table []
  (:content (first (:content (nth (tables) 20)))))

(def ^:dynamic *base-diputados-url* "http://www.diputados.gob.mx/LeyesBiblio/")

(defn ley-entry [data]
  (try
    (let [coll (html/select (:content data) [:a])]
      {:pdf  (str *base-diputados-url* (-> coll second :attrs :href))
       :doc (str *base-diputados-url* (-> coll last :attrs :href ))
       :name (-> coll first :content last :content first)})
    (catch Exception e (println data))))


(defn download [file remote]
  (try (spit file (slurp remote))
       (catch Exception e (spit "errors.log" (str remote "\n") :append true))))

(defn scrap-leyes []
  (let [data (remove #(nil? (:name %))
                     (map ley-entry (la-table)))]
    (csv "leyes/catalogo.csv" data)
    (doall
     (map #(download (str "leyes/" (standard-name (:name %)) ".doc")
                     (:doc %))
          data))))

(comment
(defn bulk-ordenjuridico []
  (let [data-leyes (map ley-entry (leyes-table))
        data-reglamentos (map reglamento-entry (reglamentos-table))]
    (csv "catalogo-leyes-federales.csv" data-leyes)
    (csv "catalogo-reglamentos-federales.csv" data-reglamentos)
    (doall (map #(download (str "resources/" (% :url-id) ".html")
                           (% :url))
                (concat data-leyes data-reglamentos)))))
)
