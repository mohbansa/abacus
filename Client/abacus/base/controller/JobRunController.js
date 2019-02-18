angular.module('dashboard').controller('closeJobController',function ($timeout,$http,$scope,pieService, $uibModalInstance,jobNameSelected) {

   $timeout( function(){ $scope.assignData(); }, 1000);
var jobDataParameters;
var credentials;
var hostName;

$scope.assignData=function()
{
  //console.log("data");


  pieService.getJobsParameters().then(function (data) {
    //console.log("Data angular");
    jobDataParameters=data[0];

    angular.forEach( jobDataParameters, function(value, key) {
      if(key==jobNameSelected)
      {
        $scope.jobParameter=value;
      //console.log(value);
    }
   });

  });

  pieService.getJobData().then(function (data) {
  var jobData=data;
   angular.forEach( jobData, function(value, key) {
     //console.log(value.JobName);
     if(value.JobName==jobNameSelected)
     {
       hostName=value.Host;
       credentials=value.Credentials;
     }
   });
});
};


$scope.runJob = function () {

  //console.log("under function");
    var query;
    if(jobNameSelected=='Automation_BlacklistedSMS'|| jobNameSelected=='Automation_ConfigMgmt')
           query="?Commonsearch_FilePath="+$scope.selectFilePath+"&&refreshView=true&&"+"ExecutionType="+$scope.selectExecType+"&&Lab="+$scope.selectLab;
     else
    if(jobNameSelected=='Production_CloudPortalTests')
           query="?DC="+$scope.onDemandDC+"&&Sanity_Type="+$scope.onDemandSanityType+"&&email="+$scope.email;
             var config = {
               headers : {
                   'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;',
                   'Authorization' :'Basic '+btoa(credentials)

                         }
           };

           function genericSuccess (res) {
              $scope.PostDataResponse = res;
              }
var data={};

 $http.post(hostName+'/job/'+jobNameSelected+'/buildWithParameters'+query,angular.toJson(data) , config)
 .then(function(success) {
   //console.log("success:"+angular.toJson(success));
   //console.log(success.status);
    $uibModalInstance.dismiss('submit');
     return genericSuccess(success.data);


   })
   .then(function(error){
      //console.log("error:"+error);
   return error;
   });
       };

$scope.close = function () {
  //console.log("cancel called123");
$uibModalInstance.dismiss('cancel');
};
});

angular.module("dashboard")
.controller("JobRunController", function ($scope,$http,pieService,$uibModal,$timeout) {
  //console.log("Hello <job>run</job>");
  $scope.queueJobs=[];
  $scope.runningJobs=[];
  $scope.completedJobs=[];
  $scope.getALlJobs=function(jobName)
  {
    $scope.queueJobs=[];
    $scope.runningJobs=[];
    $scope.completedJobs=[];
    $scope.getQueueJobs(jobName);
    $scope.getRunningJobs(jobName);
    $scope.getCompletedJobs(jobName);
  };
  $scope.open=function(jobName)
  {
    //console.log("Hello "+jobName);
    var  template='base/view/popuptemplate/'+jobName+'.html';
    var uibModalInstance = $uibModal.open({
    templateUrl:template ,
    controller: 'closeJobController',
    resolve: {
      jobNameSelected: function () {

        return jobName;
     }

}
  });
  };
  $scope.getQueueJobs=function(jobName)
  {

    //console.log("JobName:"+jobName);

     angular.forEach(   $scope.jobData, function(value, key) {
       //console.log("JobNamename"+value.JobName);
         pieService.getQueueJobs(value.Host).then(function (data) {
           angular.forEach( data.items, function(queueJob, key) {
             //console.log("queueJob:"+queueJob);
             var exists = false;
		           angular.forEach($scope.queueJobs, function(val2, key) {
			              if(angular.equals(queueJob, val2))
                     exists = true ;
		                });
		                if(exists == false)
             $scope.queueJobs.push(queueJob);
           });
    //    angular.extend(  $scope.queueJobs,data.items);
          //console.log($scope.queueJobs);
          });

     });

  };

  $scope.getRunningJobs=function(jobName)
  {


        //console.log("JobName:"+jobName);

         angular.forEach(   $scope.jobData, function(value, key) {
           //console.log("JobNamename"+value.JobName);

             pieService.getRunningJobs(value.Host,jobName).then(function (data) {
            console.log(angular.toJson(data));
            var runningJobsJson;

            if(data.jobs.job.length>1)
              runningJobsJson=data.jobs.job;
            else
              runningJobsJson=data.jobs;
            console.log("Running Job count");
            console.log(data.jobs);
        //    console.log(data.jobs.job.length);
               angular.forEach( runningJobsJson, function(runJob, key) {
              console.log("running");
              console.log(runJob);
                 var exists = false;
    		           angular.forEach($scope.runningJobs, function(val2, key) {
                     console.log(runJob.name);
                     console.log(val2.name);
    			              if(angular.equals(runJob.name, val2.name))
                         exists = true ;
    		                });
    		        if(exists == false)
                 $scope.runningJobs.push(runJob);
                 console.log($scope.runningJobs);
               });
        //    angular.extend(  $scope.queueJobs,data.items);

              });

         });
  };

  $scope.getCompletedJobs=function(jobName)
  {

  //console.log("JobName:"+jobName);

         angular.forEach(   $scope.jobData, function(value, key) {
           //console.log("JobNamename"+value.JobName);
           var temp={};
             pieService.getCompletedJobs(value.Host,jobName).then(function (data) {
               temp=data.length;
                //console.log(temp);
              if(temp>0)

                {
                  angular.forEach(data, function(val2, key) {
                    console.log("Result:"+angular.toJson(val2.result));

                    if(val2.result!=null)
                    {
                      var exists = false;
                        angular.forEach($scope.completedJobs, function(runJob, key) {
                          console.log(runJob.fullDisplayName);
                          console.log(val2.fullDisplayName);
                             if(angular.equals(runJob.fullDisplayName, val2.fullDisplayName))
                              exists = true ;
                             });
                     if(exists == false)
                       $scope.completedJobs.push(val2);
                    }

              });
                }
                console.log("Completed Data:");
                console.log($scope.completedJobs);




         });
       });
   };

$scope.cancelJob=function(jobName,id)
{
  console.log("cancel job");
console.log(jobName+" "+id+" ");
angular.forEach(   $scope.jobData, function(value, key) {
  //console.log("JobNamename"+value.JobName);
    pieService.cancelJob(value.Host,id).then(function (data) {
      console.log("cancel job successfully");
    });
});
};

$scope.abortJob=function(jobName,number)
{
  console.log("abortJob");
console.log(jobName+" "+number+" ");
angular.forEach(   $scope.jobData, function(value, key) {
  //console.log("JobNamename"+value.JobName);
    pieService.abortJob(value.Host,value.Credentials,jobName,number).then(function (data) {
      console.log("abortJob successfully");
    });
});
};


         $scope.getJobData=function()
         {
           //console.log("getJobData run");
           pieService.getJobData().then(function (data) {
            $scope.jobData=data;

         });
       };

});
