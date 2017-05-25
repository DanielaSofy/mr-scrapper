(defproject mr-scrapper "0.1.0-SNAPSHOT"
  :description "FIXME"
  :url "https://github.com/fractalLabs/mr-scrapper"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [digitalize "0.1.0-SNAPSHOT"]
                 [enlive "1.1.6"]   ; Scrapper
                 [formaterr "0.1.0-SNAPSHOT"]
                 [stencil "0.5.0"]  ; Mustache templates
                 ]
  :repl-options {:init-ns mr-scrapper.diputados})
