


angular.module("dashboard")
.controller("tableController", function ($timeout,$scope,pieService,jobParametersService) {
console.log("tableController");
  var localData;
  $scope.urlDetails=null;


  var job1;
  var project1;
  $scope.dynamicPopover = {
    content: 'Info',
    templateUrl: 'myPopoverTemplate.html',

  };
  $scope.changeLength = function(exception) {
    //console.log(exception.textLength);
  //  console.log(exception);
    exception.length = 9999;
};
  $scope.wait=function(ms){
            var start = new Date().getTime();
            var end = start;
            while(end < start + ms) {
              end = new Date().getTime();

           }
           console.log("end:"+end);
         };
$scope.tableData=function()
{
var configuration=jobParametersService.getProperty();
   job1=configuration.JobName;
   project1=configuration.ProjectName;
  pieService.getInstanceData("base/data/instance.json").then(function (data) {
    console.log("gtdata");

     localData=data;
     console.log(localData);
    $scope.urlDetails="http://"+localData[project1].host+":"+localData[project1].port+"/job/"+job1+"/";

   console.log(localData[project1].host+" "+localData[project1].port);
  });
$timeout(function(){$scope.tableChart();},1000);
};

  $scope.tableChart=function()
    {

    $scope.lastTenBuildTestdata=[];
    var URL="http://"+localData[project1].host+":"+localData[project1].port+"/job/"+job1+"/api/json?tree=builds[number,result]{0,10}";
      pieService.getBuildNumber(URL).then(function (data) {
            var actualData = data;
            var buildCount =  actualData.length;
            var endBuildNumber = actualData[0].number;
            var startBuildNumber = actualData[buildCount - 1].number;
            for ( var p = startBuildNumber; p <= endBuildNumber; p++) {
          //    console.log("buildCount:"+buildCount);
              var URLS="http://"+localData[project1].host+":"+localData[project1].port+"/job/"+job1+"/"+p+"/testngreports/api/json?tree=package[classs[name,skip,fail,totalCount,test-method[parameters,exception,name,status]]]";
            // console.log(URLS);
                pieService.getData(URLS,p).then(function (dataTable) {
                      $scope.lastTenBuildTestdata[dataTable[0].number-startBuildNumber]=dataTable[0];

                      $timeout(function(){
                        $scope.tableChartDataParse(  $scope.lastTenBuildTestdata);
                      },3000);
                    });
                  }
        });

      };


$scope.tableChartDataParse=function()
{
  var successBuildCount=10;
  var successBuild=0;
  var filterData=[];
  var dataCompleted=true;
  var testLength=0;
   console.log(angular.toJson($scope.lastTenBuildTestdata));

  $scope.testData={"testFileData":[],"header":[]};
  //filter data to remove no data build
  angular.forEach(  $scope.lastTenBuildTestdata, function(value, key) {
    //console.log(value);
    if(value.data!='NO DATA' && dataCompleted)
    {
      filterData[successBuild]=value;
      successBuild++;
    }
    if(filterData.length>=successBuildCount)
      dataCompleted=false;
  });

  //reverse filterData to take latest data and print data from left to right in decreasing build number
  filterData.reverse();
 //console.log(angular.toJson(filterData));
  //console.log(filterData.reverse(););

    //add testMethod and result node in testData
//  console.log(angular.toJson(filterData[0].data));
var sizeOfModule=0;
  angular.forEach(filterData[0].data,function(package,key){
//  console.log("key:"+key);

      angular.forEach(package.classs,function(classFile,packageKey){
      //console.log(angular.toJson(classFile['test-method']));
        $scope.testData.testFileData[sizeOfModule]={
          "moduleName":classFile.name,
          "result":[],
          "testMethod":[]
        };
        angular.forEach(classFile['test-method'],function(value,key){
        //  console.log(value);
           $scope.testData.testFileData[sizeOfModule].testMethod[key]=
            {
              "testName":value.name,
              "result":[]

            };
        });
        sizeOfModule++;
      });
    });

    angular.forEach(  filterData, function(value, key) {
        //console.log("Filterkey:"+key);
          $scope.testData.header[key]={
                "buildNumber":value.number,
                "mappingNumber":"Run#"+(filterData.length-key)
              };
                sizeOfModule=0;
          angular.forEach(value.data,function(packageInfo,packageKey){
            //console.log(angular.toJson(packageInfo));

            angular.forEach(packageInfo.classs,function(classInfo,classKey){
            //  console.log(angular.toJson(classInfo));
            //  console.log("SizeOfModule:"+sizeOfModule);
          //  console.log(classInfo.fail+": "+classInfo.skip);
              var status='FAIL';
              if(classInfo.fail==0 && classInfo.skip==0)
                status='PASS';
                //this code is useful when new node added
               angular.forEach($scope.testData.testFileData,function(actualTestFile,actualTestKey)
               {
              //   console.log(angular.toJson(actualTestFile.moduleName)+" "+angular.toJson(classInfo.name));
                 if(actualTestFile.moduleName==classInfo.name)
                 {
                 actualTestFile.result.push({
                   "buildNumber":value.number,
                   "status":status,

                 });
               }
               });


               angular.forEach(classInfo['test-method'],function(testClassInfo,testClassKey){
              //  console.log("classKey"+testClassKey+":"+angular.toJson(testClassInfo));
                 //console.log(" testClassKey:"+testClassKey);
                //console.log(" testClassInfo:"+angular.toJson($scope.testData.testFileData[sizeOfModule].testMethod[testClassKey]));

                //this code just to handle json discrepancy
                var flag=true;
                   angular.forEach($scope.testData.testFileData,function(actualValue,actualKey)
              {
                //console.log(angular.toJson($scope.testData.header));

                                   angular.forEach(actualValue.testMethod,function(test,testActualKey)
                                   {

                   if(test.testName==testClassInfo.name && flag && test.result.length<=key)
                   {
                     flag=false;
                     console.log(angular.toJson(testClassInfo.parameters));


                   test.result.push({
                        "buildNumber":value.number,
                        "status":testClassInfo.status,
                        "exception":null,//testClassInfo.exception!=null?angular.toJson(testClassInfo.exception):'',
                        "parameters": testClassInfo.parameters!=null?angular.toJson(testClassInfo.parameters):''
                      });
                 }

                 });

               });//EOC

                 });

                  sizeOfModule++;
            });

          });
        });
  console.log("testData");
  console.log(angular.toJson($scope.testData));
};
    self.expandAll = function (expanded) {
        console.log(expanded);
        // $scope is required here, hence the injection above, even though we're using "controller as" syntax
        $scope.$broadcast('onExpandAll', {expanded: expanded});
    };
});
