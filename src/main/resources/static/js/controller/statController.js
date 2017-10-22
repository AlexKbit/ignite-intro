'use strict';

App.controller('statController', ['$scope', 'StatisticsService', '$interval', function($scope, StatisticsService, $interval) {

    $scope.statNodes = {};
    $scope.statStatus = {};

    var loadNodeStat = function () {
        StatisticsService.getNodeStat().then(function (result) {
            console.log(result);
            $scope.statNodes = result;
        }, function (error) {
            console.log(error.message);
        });
    };

    var loadStatusStat = function () {
        StatisticsService.getStatusStat().then(function (result) {
            console.log(result);
            $scope.statStatus = result;
        }, function (error) {
            console.log(error.message);
        });
    };

    loadNodeStat();
    loadStatusStat();

    $scope.pageChanged = function () {
        loadNodeStat();
        loadStatusStat();
    };

    $scope.reloadRoute = function () {
        $route.reload();
    };

    $interval(function(){
        $scope.pageChanged();
    }.bind(this), 2000);

}]);
