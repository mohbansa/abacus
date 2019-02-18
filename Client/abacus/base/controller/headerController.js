angular.module("dashboard")
    .controller('headerController', function (cfpLoadingBar,$scope,$filter,$http,$location,jsonService,pieService,jobParametersService,$timeout) {

      var date = new Date();
      date.setDate(date.getDate() - 1);
      $scope.startDate=date;
        $scope.endDate=new Date();


    $scope.start = function() {
      cfpLoadingBar.start();
    };

    $scope.complete = function () {
      cfpLoadingBar.complete();
    };


    // fake the initial load so first time users can see it right away:
    $scope.start();
    $scope.fakeIntro = true;
    $timeout(function() {
      $scope.complete();
      $scope.fakeIntro = false;
    }, 750);


      $scope.project = {};
      $scope.job = {};
      $scope.projects={};
      var allJobs;
      $scope.currentPage="AdminUIUX";
      $scope.active = true;
      var localData;
$scope.getJob=function (project,page)
      {
    //console.log("first function");
    //console.log("/"+project+"/"+page);
          $location.path("/"+project+"/"+page);
          jobParametersService.setProperty(project,page);
        //$scope.loadData();
      };

$scope.getDateParameter=function(project,job,startDate,endDate)
{
  console.log("getDate");

  startDate =   $filter('date')(startDate, "yyyy-MM-dd");
    endDate =   $filter('date')(endDate, "yyyy-MM-dd");
  console.log(project+" "+job+" "+startDate+" "+endDate);
  pieService.getInstanceData().then(function (data) {
   localData=data;

   $timeout(function(){
     jobParametersService.setProperty(project.ProjectName,job.JobName);


     $scope.passFilterDateRangeToMainController(project.ProjectName,job.JobName,startDate,endDate);
  },1000);
  });

};

$scope.passFilterDateRangeToMainController=function(project,job,startDate,endDate)
      {
        if($scope.enabledDateRange==false)
            jobParametersService.setDateRangeParamters();
        else
        {
          jobParametersService.setJobParameters();
          jobParametersService.setDateRangeParamters(startDate,endDate);
        }
          $scope.$emit("HeaderToDashboard");
      };

$scope.getJobsParameters=function(project,job,dataCenter, sanityType)
      {

//console.log("Project Name");
        //console.log(angular.toJson(job));
        pieService.getInstanceData().then(function (data) {
         localData=data;
         //console.log("localData");
         //console.log(angular.toJson(localData));
         $timeout(function(){
           jobParametersService.setProperty(project.ProjectName,job.JobName);
           //console.log(jobParametersService.getProperty());
           $scope.fetchParameters(project.ProjectName,job.JobName);
           $scope.passFilterDataToMainController(project.ProjectName,job.JobName,dataCenter,sanityType);
        },1000);
        });


      };
$scope.passFilterDataToMainController=function(project,job,dataCenter,sanityType)
      {
        var parameters;
        //console.log(dataCenter+" "+sanityType);
        if(job=='Production_CloudPortalTests-ScheduledRun')
        {
          parameters={
            "DC":dataCenter,
            "Sanity_Type":sanityType
          };
        }

          jobParametersService.setJobParameters(parameters);
        //    console.log("enabled:"+$scope.enabledDateRange);
          if($scope.enabledDateRange==false)
            jobParametersService.setDateRangeParamters();
          $scope.$emit("HeaderToDashboard");
      };
$scope.fetchParameters=function(project,job)
      {

        var header={
        'Content-Type': 'application/x-www-form-urlencoded',
      'jenkinURL':'http://'+localData[project].host+':'+localData[project].port,
      'jobName':job,
      'userName':localData[project].username,
      'passWord':localData[project].password
    };
  /*    pieService.getRestApiData("http://localhost:8080/abacus/webapi/dashboard/jobParameters",header).then(function (data) {
console.log("parameters in header controller");
      console.log("parameters:"+data);
    });*/
    };


jsonService.getJSONdata("base/data/ProjectList.json").then(function(allProjects){
    $scope.projects=allProjects;
});
$scope.$watch('project', function () {
    jsonService.getJSONdata("base/data/jobsList.json").then(function(data){
      allJobs=data;
      $scope.jobs = allJobs.filter(function (s) {
          return s.ProjectId == $scope.project.Id;
      });
    });
$scope.state = {};

    });



    });
