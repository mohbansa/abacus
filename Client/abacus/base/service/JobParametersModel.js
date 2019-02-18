angular.module('dashboard')
    .factory('jobParametersService', function($http,$q) {

        var factory = {};
        var deferred = $q.defer();
      var project;
      var job;
      var jobParameters;
      var startDateRange;
      var endDateRange;
      //SET INSTANCE PROPERTY
        function setProperty(value1,value2)
        {
          project=value1;
          job=value2;

      //  console.log("tableService:"+project+" "+job+" "+username+" "+password);
      }
      //GET INSTANCE PROPERTY
    function  getProperty()
      {
        return{
          "ProjectName":project,
          "JobName":job

        };
      }

function setDateRangeParamters(startDate,endDate)
{
  startDateRange=startDate;
  endDateRange=endDate;
}
function getDateRangeParamters()
{
  return{
    "startDate":startDateRange,
    "endDate":endDateRange
  };
}
function setJobParameters(parameters)
{

jobParameters=parameters;
}

function getJobParameters()
{
  return{
"Parameters":jobParameters
  };
}

      return{

        setProperty:setProperty,
        getProperty:getProperty,
        setJobParameters:setJobParameters,
        getJobParameters:getJobParameters,
        setDateRangeParamters:setDateRangeParamters,
        getDateRangeParamters:getDateRangeParamters
      };



    });
