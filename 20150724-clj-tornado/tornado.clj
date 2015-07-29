
;;haversine stolen from gist: https://gist.github.com/shayanjm/39418c8425c2a66d480f
(defn haversine
  "Implementation of Haversine formula. Takes two sets of latitude/longitude pairs and returns the shortest great circle distance between them (in km)"
  [{lon1 :lng lat1 :lat} {lon2 :lng lat2 :lat}]
  (let [R 3963.19 ; Radius of Earth in miles
        dlat (Math/toRadians (- lat2 lat1))
        dlon (Math/toRadians (- lon2 lon1))
        lat1 (Math/toRadians lat1)
        lat2 (Math/toRadians lat2)
        a (+ (* (Math/sin (/ dlat 2)) (Math/sin (/ dlat 2))) (* (Math/sin (/ dlon 2)) (Math/sin (/ dlon 2)) (Math/cos lat1) (Math/cos lat2)))]
    (* R 2 (Math/asin (Math/sqrt a)))))

(use 'clojure.xml)

;;A location in the relative viscinity of my highly classified location
(def whereIBe {:lat 39.636908 :lng -104.9044565 }) 

(defn attr
  [tag field]
  (field (:attrs tag)))

(sort-by (juxt :distance) 
  (map 
    (fn [x] { :distance (haversine whereIBe {:lng (Double/parseDouble (attr x :T_StartLon)) :lat (Double/parseDouble (attr x :T_StartLat))} )
          :location (attr x :T_Location)
          :state (attr x :T_State)})
    (filter
      (fn [x] (= (:tag x) :tornado))
      (:content
        (parse "http://www.tornadopaths.org/XML/date/latest.xml")
      )
    )
  )
)
