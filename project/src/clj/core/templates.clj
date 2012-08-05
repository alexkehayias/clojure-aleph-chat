(ns core.templates
  (:use (hiccup core page)))

(defn page []
  "HTML page rendered using Hiccup. Includes the css and js for websockets."
  (html5
   [:head
    (include-css "/static/stylesheets/master.css")]
   [:body
    [:div.container
     [:div.row
      [:div.columns.twelve
        [:p [:h1#headline "Chat"]]
        [:form
          [:input#message {:type "text"}]
          [:input.nice.large.blue.button {:type "submit"}]]
        [:div#messages]]]]
    (include-js "http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js")
    (include-js "/static/javascripts/web_socket.js")
    (include-js "/static/javascripts/app.js")]))
