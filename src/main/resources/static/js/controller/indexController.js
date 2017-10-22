'use strict';

App.controller('indexController', ['$scope', 'TaskService', '$interval', function($scope, TaskService, $interval) {

    $scope.pageSize = 8;
    $scope.currentPage = 1;

    $scope.totalItems = 1;
    $scope.totalPages = 1;
    $scope.tasks = [];

    $scope.startTask = function (expression) {
        TaskService.createTask(expression).then(function (results) {
            console.log("Start task for expression: " + expression);
            $scope.pageChanged();
        }, function (error) {
            console.log(error.message);
        });
    };

    var loadPages = function () {
        TaskService.loadAllTasks($scope.currentPage-1, $scope.pageSize).then(function (results) {
            console.log(results);

            $scope.totalItems = results.totalElements;
            $scope.totalPages = results.totalPages;

            $scope.tasks = results.content;

        }, function (error) {
            console.log(error.message);
        });
    };

    $scope.deleteTask = function(taskId) {
        console.log($scope.selection);
        TaskService.deleteTask(taskId).then(function (results) {
            console.log(results);
            console.log('deleted');
            $scope.pageChanged();
        }, function (error) {
            console.log(error.message);
        });
    };

    $scope.stopTask = function(taskId) {
        console.log($scope.selection);
        TaskService.stopTask(taskId).then(function (results) {
            console.log(results);
            console.log('stopped');
            $scope.pageChanged();
        }, function (error) {
            console.log(error.message);
        });
    };

    loadPages();

    $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo;
    };

    $scope.pageChanged = function () {
        loadPages();
    };

    $scope.reloadRoute = function () {
        $route.reload();
    };

    $interval(function(){
        $scope.pageChanged();
    }.bind(this), 2000);

}]);
