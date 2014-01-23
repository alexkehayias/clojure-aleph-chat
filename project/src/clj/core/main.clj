(ns core.main
  (:use lamina.core
        aleph.http
        compojure.core
        ; This sets the correct file type for js includes used by hiccup
        (ring.middleware resource file-info)
        core.views
        core.templates)
  (:require [compojure.route :as route]))

(defn sync-app [request]
  "Rendered response of the chat page"
  {:status 200
   :headers {"content-type" "text/html"}
   :body (page)})

(def wrapped-sync-app
  "Wraps the response with static files"
  (-> sync-app
      (wrap-resource "public")
      (wrap-file-info)))

(defn chat [ch request]
  "View handler that handles a chat room. If it's not
  a websocket request then return a rendered html response."
  (let [params (:route-params request)
        room (:room params)]
    (if (:websocket request)
      (chat-handler ch room)
      (enqueue ch (wrapped-sync-app request)))))

(defroutes app-routes
  "Routes requests to their handler function. Captures dynamic variables."
  (GET ["/chat/:room", :room #"[a-zA-Z]+"] {}
       (wrap-aleph-handler chat))
  (GET ["/"] {} "Hello world!")
  ;;Route our public resources like css and js to the static url
  (route/resources "/static")
  ;;Any url without a route handler will be served this response
  (route/not-found "Page not found"))

(defn -main [& args]
  "Main thread for the server which starts an async server with
  all the routes we specified and is websocket ready."
  (start-http-server (wrap-ring-handler app-routes)
                     {:host "localhost" :port 8080 :websocket true}))
