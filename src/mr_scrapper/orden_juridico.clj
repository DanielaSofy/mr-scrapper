(ns mr-scrapper.orden-juridico
  (:require [clojure.string :as str]
            [mr-scrapper.core :refer :all]
            [formaterr.core :refer :all]
            [net.cgrand.enlive-html :as html]
            [stencil.core :refer :all]))

(def ^:dynamic *base-url* "http://ordenjuridico.gob.mx/leyes.php")

(defn tables [] (html/select (fetch-url *base-url*) [:table]))

;(def je (html/select (fetch-url *base-url*) [:table#myTable]))
(defn leyes-table  []
  (-> (filter #(and (= "myTable" (:id (:attrs %)))
                    (= "600" (:width (:attrs %))))
              (tables))
      first
      :content
      second
      :content))

(defn reglamentos-table  []
  (-> (filter #(and (= "myTabla" (:id (:attrs %)))
                    (= "600" (:width (:attrs %))))
              (tables))
      first
      :content
      second
      :content))

(defn file-id [s]
  (re-find #"[^.]+" (last (re-seq #"[^/]+" s))))

(defn orden-juridico-html-url [id]
  (render-string "http://ordenjuridico.gob.mx/Documentos/Federal/html/{{id}}.html"
                 {:id id}))

(defn ley-entry [m]
  (let [data (html/select m [:td])
        url-id (-> data second :content first :attrs :id file-id)]
    {:nombre (-> data second :content first :content first str/trim)
     :id (-> data first :content first)
     :fecha-publicacion (-> data (nth 2) :content first)
     :fecha-actualizacion (-> data (nth 3) :content first)
     :url-id url-id
     :url (orden-juridico-html-url url-id)}))

(defn reglamento-entry [m]
  (let [data (html/select m [:td])
        url-id (-> data second :content first :attrs :id file-id)]
    {:nombre (-> data second :content first :content first str/trim)
     :id (-> data first :content first)
     :fecha-publicacion (-> data (nth 2) :content first)
     :fecha-actualizacion (-> data (nth 3) :content first)
     :fecha-fe-de-erratas (-> data (nth 4) :content first)
     :url-id url-id
     :url (orden-juridico-html-url url-id)}))

(defn catalogo [data] (csv "catalogo-ordenjuridico.csv" data))

(defn download [file remote]
  (try (spit file (slurp remote))
       (catch Exception e (spit "errors.log" (str remote "\n") :append true))))

(defn bulk-ordenjuridico []
  (let [data-leyes (map ley-entry (leyes-table))
        data-reglamentos (map reglamento-entry (reglamentos-table))]
    (csv "catalogo-leyes-federales.csv" data-leyes)
    (csv "catalogo-reglamentos-federales.csv" data-reglamentos)
    (doall (map #(download (str "resources/" (% :url-id) ".html")
                           (% :url))
                (concat data-leyes data-reglamentos)))))
