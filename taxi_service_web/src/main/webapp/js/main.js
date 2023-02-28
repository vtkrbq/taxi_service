var mapOptions = {
    center: [49.9873, 36.2666],
    zoom: 12,
};


map = new L.map('map', mapOptions);
var baseLayer = L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
layerGroup = L.layerGroup().addTo(map);
map.doubleClickZoom.disable();

var destinCoord, departCoord;
var distance = document.getElementById("distance");

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
    var dx = document.getElementById("dx");
    var dy = document.getElementById("dy");
    var urlApi = 'https://api.visicom.ua/data-api/5.0/uk/geocode.json?text=' + x.value + '&key=9bc097f16c0606e0fb92a8e0ac130127&limit=1';
    if (x.value !== '') {
        getJSON(urlApi,
            function(err, data) {
              if (err !== null) {
                alert('Something went wrong: ' + err);
              } else {
                dx.value = data.geo_centroid.coordinates.toString().split(',')[1];
                dy.value = data.geo_centroid.coordinates.toString().split(',')[0];
                departCoord = L.latLng(dx.value, dy.value);
                var waypointDepart = L.marker([dx.value, dy.value]);
                waypointDepart.addTo(layerGroup);
                map.fitBounds(waypointDepart);
              }
        });
    }
}

function getDestinationCoords() {
    var x = document.getElementById("destin");
    var y = document.getElementById("textarea");
    var dx = document.getElementById("dsx");
    var dy = document.getElementById("dsy");
    var urlApi = 'https://api.visicom.ua/data-api/5.0/uk/geocode.json?text=' + x.value + '&key=9bc097f16c0606e0fb92a8e0ac130127&limit=1';
    if (x.value !== '') {
        getJSON(urlApi,
            function(err, data) {
              if (err !== null) {
                alert('Something went wrong: ' + err);
              } else {
                dx.value = data.geo_centroid.coordinates.toString().split(',')[1];
                dy.value = data.geo_centroid.coordinates.toString().split(',')[0];
                destinCoord = L.latLng(dx.value, dy.value);
                var waypointDestin = L.marker([dx.value, dy.value]);
                waypointDestin.addTo(layerGroup);
                var waypoints = [departCoord, destinCoord];
                var route = L.routing.control({
                        waypoints,
                        showAlternatives: false
                    });
                route.options.show = false;
                route.addTo(map);
                route.on('routesfound', function(e) {
                  var routes = e.routes;
                  var summary = routes[0].summary;
                  distance.value = (summary.totalDistance / 1000);
                });
              }
            });
    }
}



function getTripRoute(waypoints) {
    var route = L.routing.control({
        waypoints,
        showAlternatives: false
    });
    route.options.show = false;
    route.addTo(map);
};

