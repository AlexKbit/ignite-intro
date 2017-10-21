'use strict';

App.factory('StatisticsService', ['$http', '$q', function($http, $q){

    var HOST = "/stat/";

    return {

        getNodeStat: function() {
            return $http.get(HOST + 'node')
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while load statistics about nodes');
                        return $q.reject(errResponse);
                    }
                );
        },

        getStatusStat: function() {
            return $http.get(HOST + 'status')
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while load statistics about status');
                        return $q.reject(errResponse);
                    }
                );
        }

    };

}]);
