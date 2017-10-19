'use strict';

App.factory('TaskService', ['$http', '$q', function($http, $q){

    var HOST = "/tasks";

    return {

        loadAllTasks: function(page, count) {
            return $http.get(HOST + '?page=' + page + '&count=' + count)
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while load tasks');
                        return $q.reject(errResponse);
                    }
                );
        },

        createTask: function(expression){
            return $http.post(HOST + '/start', expression)
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while start task');
                        return $q.reject(errResponse);
                    }
                );
        },

        stopTask: function(taskId){
            return $http.get(HOST + '/stop?taskId=' + taskId)
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while stop task');
                        return $q.reject(errResponse);
                    }
                );
        },

        deleteTask: function(taskId){
            return $http.delete(HOST +'?taskId=' + taskId)
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while deleting task');
                        return $q.reject(errResponse);
                    }
                );
        }

    };

}]);
