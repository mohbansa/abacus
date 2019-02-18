angular.module('dashboard')
    .factory('jsonService', function($http,$q) {

        var factory = {};
        var deferred = $q.defer();



        function getJSONdata(URL)
        {
          return $http({
              method: 'GET',
              url:URL,
               dataType: 'json',
              headers: {'Content-Type': 'application/json'}
          })
              .then(function(response) {
                console.log("Response");
                console.log(response.data);
                  return response.data;
              })
              .catch(function(error) {

                  throw error;
              });
        }
      return{

        getJSONdata:getJSONdata

      };



    });
