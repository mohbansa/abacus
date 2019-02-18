
angular.module("dashboard")
.controller("atmController", function ($scope,pieService) {
  console.log("Hello");
$scope.ATM=function()
   {
     
      pieService.getLocalData().then(function (data) {

     $scope.tableManualData=data;
      });

   };
   $scope.sort = {
        column: '',
        descending: true
    };

    $scope.selectedCls = function(column) {
        return column == $scope.sort.column && 'sort-' + $scope.sort.descending;
    };

    $scope.changeSorting = function(column) {

        var sort = $scope.sort;
        if (sort.column == column) {
            sort.descending = !sort.descending;
        } else {
            sort.column = column;
            sort.descending = false;
        }
  //console.log(sort.column+" "+  sort.descending);
    };
    $scope.columnSort=function(columnValue)
    {

     if( $scope.sort.column =='Module Name')
        return columnValue[$scope.sort.column].toString();
    else
  return parseInt(columnValue[$scope.sort.column]);

    };

    $scope.getIcon = function(column) {

  var sort = $scope.sort;

  if (sort.column == column) {
    return sort.descending   ? 'fa-sort-desc'    : 'fa-sort-asc';
    }

  return 'fa-sort';
};

 });
