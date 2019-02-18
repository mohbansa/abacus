angular.module('dashboard')
    .factory('pieService', function($http,$q) {

        var factory = {};
        var deferred = $q.defer();
      var project;
      var job;

      function  getLocalData()
        {
          return $http({
              method: 'GET',
              url:"base/data/ATM.json",
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          })
              .then(function(response) {

                  return response.data;
              })
              .catch(function(error) {

                  throw error;
              });
        }



        //GET INSTANCE DATA FROM JSON FILE
      function  getInstanceData()
        {
          return $http({
              method: 'GET',
              url:"base/data/instance.json",
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          })
              .then(function(response) {

                  return response.data;
              })
              .catch(function(error) {

                  throw error;
              });
        }

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

      //GET BUILD NUMBER FROM REST CALL
    function   getBuildNumber(URL) {
        //console.log("URL:"+URL);
          return $http({
              method: 'GET',
              url:URL,

              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          })
              .then(function(response) {
                ////console.log("response");
                  ////console.log(response.data.builds);
                  return response.data.builds;
              })
              .catch(function(error) {

                  throw error;
              });
      }


      //GET DATA OF PARTICULAR BUILD NUMBER
      function getData (URL,buildNumber) {
        ////console.log(buildNumber);

        return $http({
            method: 'GET',
            url:URL,

            headers: {'Content-Type': 'application/json'}
        })
            .then(function(response) {
                var newdata=[];

                newdata.push(
                    {
                        number:buildNumber,
                        data:response.data.package
                    }
                );
                return newdata;

            })
            .catch(function(error) {
              var newdata=[];

              newdata.push(
                  {
                      number:buildNumber,
                      data:'NO DATA'
                  }
              );
                return newdata;
            });
      }

      //GET DATA OF PARTICULAR BUILD NUMBER
      function getBuildStatus (URL,buildNumber) {
        ////console.log(buildNumber);

        return $http({
            method: 'GET',
            url:URL,

            headers: {'Content-Type': 'application/json'}
        })
            .then(function(response) {
                var newdata=[];

                newdata.push(
                    {
                        number:buildNumber,
                        data:response.data
                    }
                );
                return newdata;

            })
            .catch(function(error) {
              var newdata=[];

              newdata.push(
                  {
                      number:buildNumber,
                      data:'NO DATA'
                  }
              );
                return newdata;
            });
      }

      //GET Job Data
      function  getJobData()
        {
          return $http({
              method: 'GET',
              url:"base/data/jobData.json",
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          })
              .then(function(response) {

                  return response.data;
              })
              .catch(function(error) {

                  throw error;
              });
        }
        //GET Job Data
        function  getJobsParameters()
          {
            return $http({
                method: 'GET',
                url:"base/data/jobsParameters.json",
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            })
                .then(function(response) {

                    return response.data;
                })
                .catch(function(error) {

                    throw error;
                });
          }

          //GET <REST><API> Data</API></REST>
          function  getRestApiData(URL,sendData,header)
            {
              console.log("Parameters");
              console.log(angular.toJson(sendData));
              console.log("Header");
              console.log(angular.toJson(header));
              return $http({
                  method: 'POST',
                  url:URL,
                  headers:header,
                  params:sendData
              })
                  .then(function(response) {
                //    console.log("Rest API:"+angular.toJson(response.data));
                      return response.data;
                  })
                  .catch(function(error) {

                      throw error;
                  });
            }
            function  getQueueJobs(host)
              {

                return $http({
                    method: 'GET',
                    url:host+"/queue/api/json?tree=items[id,params,task[name,url,color],inQueueSince,why]",
                  })
                    .then(function(response) {
                  //    console.log("Rest API:"+angular.toJson(response.data));
                        return response.data;
                    })
                    .catch(function(error) {

                        throw error;
                    });
              }

              function  getRunningJobs(host,jobName)
                {
                  console.log(host+" "+jobName);
                  var URL=host+"/api/xml?tree=jobs[name,url,color,lastBuild[number,timestamp,duration]]&xpath=/hudson/job[ends-with(color/text(),'_anime')]&xpath=/hudson/job[name/text()='"+jobName+"']&wrapper=jobs";
                  console.log(URL);
                  console.log("getCompletedJobs");
                  return $http({
                      method: 'GET',
                        url:URL
                          })
                    .then(function(response) {
                            var x2js = new X2JS();
                            var aftCnv = x2js.xml_str2json(response.data);
                            console.log(aftCnv);
                            return aftCnv;

                    })
                    .catch(function(error) {

                        throw error;
                    });
                }

                function  getCompletedJobs(host,jobName)
                  {
                    console.log(host+" "+jobName);
                    return $http({
                        method: 'GET',
                        url:host+"/job/"+jobName+"/api/json?tree=builds[actions[parameters[name,value]],fullDisplayName,duration,number,timestamp,url,result]{0,10}",
                      })
                        .then(function(response) {
                          console.log("Rest API:"+angular.toJson(response.data.builds));
                            return response.data.builds;


                        })
                        .catch(function(error) {

                            throw error;
                        });
                  }
                  function  cancelJob(host,id)
                    {

                      return $http({
                          method: 'POST',
                          url:host+"/queue/cancelItem?id="+id,

                        })
                          .then(function(response) {
                        //    console.log("Rest API:"+angular.toJson(response.data));
                              return response.data;
                          })
                          .catch(function(error) {

                              throw error;
                          });
                    }

                    function  abortJob(host,credentials,jobName,number)
                      {

                        console.log(credentials+" "+host+" "+jobName+" "+number);
                        return $http({
                            method: 'POST',
                            url:host+"/job/"+jobName+"/"+number+"/stop",
                            headers : {
                                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;',
                                'Authorization' :'Basic '+btoa(credentials),


                                      }
                          })
                            .then(function(response) {
                          //    console.log("Rest API:"+angular.toJson(response.data));
                                return response.data;
                            })
                            .catch(function(error) {

                                throw error;
                            });
                      }

      return{
        getInstanceData:getInstanceData,
        setProperty:setProperty,
        getProperty:getProperty,
        getBuildNumber:getBuildNumber,
        getData:getData,
        getBuildStatus:getBuildStatus,
        getLocalData:getLocalData,
        getJobData:getJobData,
        getJobsParameters:getJobsParameters,
        getRestApiData:getRestApiData,
        getQueueJobs:getQueueJobs,
        getRunningJobs:getRunningJobs,
        getCompletedJobs:getCompletedJobs,
        abortJob:abortJob,
        cancelJob:cancelJob
      };



    });
