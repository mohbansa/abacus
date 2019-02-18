var app=angular.module("dashboard");


app.controller('aboutController',function ($scope, ngDialog) {

    $scope.openConfirmButton = function (rel) {
        console.log("click");
        ngDialog.openConfirm({
            template: 'templateRemove',
            className: 'ngdialog-theme-default dialogwidth800',
            scope: $scope
        });
    };
} );
