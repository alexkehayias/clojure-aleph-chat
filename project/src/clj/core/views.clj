(ns core.views
  (:use lamina.core))

(defn chat-init [ch]
  "Initialize a new chat channel"
  (receive-all ch #(println "message: " %)))

(defn chat-handler [ch room]
  "Relays messages into a chat room. If it doesn't
  exist create a new channel"
  (let [chat (named-channel room chat-init)]
    (siphon chat ch)
    (siphon ch chat)))
