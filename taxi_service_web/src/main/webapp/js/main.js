var getJSON = function(url, callback) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.responseType = 'json';
        xhr.onload = function() {
          var status = xhr.status;
          if (status === 200) {
            callback(null, xhr.response);
          } else {
            callback(status, xhr.response);
          }
        };
        xhr.send();
    };


function getDepartureCoords() {
    var x = document.getElementById("depart");
    var y = document.getElementById("textarea");
    var dx = document.getElementById("dx");
    var dy = document.getElementById("dy");
    var urlApi = 'https://api.visicom.ua/data-api/5.0/uk/geocode.json?text=' + x.value + '&key=9bc097f16c0606e0fb92a8e0ac130127&limit=1';
    getJSON(urlApi,
        function(err, data) {
          if (err !== null) {
            alert('Something went wrong: ' + err);
          } else {
            y.value = data.geo_centroid.coordinates;
            dx.value = data.geo_centroid.coordinates;
          }
        });
    x.value = x.value.toUpperCase();
}




var mapOptions = {
    center: [49.9873, 36.2666],
    zoom: 12,
};

var waypoints = [L.latLng(49.991981, 36.328126), L.latLng(50.040817, 36.360948)];


var map = new L.map('map', mapOptions);
var baseLayer = L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
map.doubleClickZoom.disable();
var route = L.routing.control({
    waypoints,
    showAlternatives: false
});
route.options.show = false;
route.addTo(map);

