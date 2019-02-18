angular.module("dashboard")
.controller("AdminUIUXController", function ($rootScope,$interval,$scope,$location,$http,$filter,$timeout,pieService,FileSaver,Blob,jobParametersService) {
    $scope.height_chart = window.innerHeight*0.4;
    var self = this;
    self.dealers={};
    var localData;
    var URL;
    var username;
  var password;
    var job1;
    var project1;
      var jobParameters;
      var startDate;
      var endDate;
    $scope.helpMessage={
  "barStackLastTenBuildChart":"This chart is displaying passed,failed, skipped status of max last ten build in percentage.",

  "LineChartLastTenBuild":"This chart is display passed status of max last ten build in percentage.",
  "gaugeChart":"This chart is displaying the passed percentage of last build test case",
  "lastBuild":"This chart is displaying each module status of last build.In header dopdown is for filtering on the basis of failure.On clicking on bar user is able to see popup which contain line chart of test module status of max last ten builds.",
  "noDataAvailable":'No module has failure more than 30%.'
};
var lastTenBuildData;

$rootScope.$on("HeaderToDashboard", function () {
        //console.log('Ctrl1 MyFunction');
        $scope.loadData();
    });

$scope.loadData=function()
{

  var configuration=jobParametersService.getProperty();
  startDate=jobParametersService.getDateRangeParamters().startDate;
  endDate=jobParametersService.getDateRangeParamters().endDate;
//  //console.log(angular.toJson(configuration));

  job1=configuration.JobName;
  project1=configuration.ProjectName;
  jobParameters=jobParametersService.getJobParameters().Parameters;
////console.log(angular.toJson(jobParameters));
  var buildCountToGet=10;

  pieService.getInstanceData().then(function (data) {
   localData=data;

   $timeout(function(){

     $scope.DataParseLastTenBuild();
     $scope.DataParseLastTenBuildPass();

    $scope.lastBuildDataParse('30');
    $scope.lastBuildPassCount();
  },1000);

   });


};

$scope.DataParseLastTenBuildPass=function()
{
var filterData=jobParameters;

  var header={
    'Content-Type': 'application/x-www-form-urlencoded',
  'jenkinURL':'http://'+localData[project1].host+':'+localData[project1].port,
  'jobName':job1,
  'userName':localData[project1].username,
  'passWord':localData[project1].password
};
//console.log("filterData");
//console.log(filterData);
//console.log("Header");
//console.log(angular.toJson(header));
    $scope.lastTenBuildDataPass=[];
  pieService.getRestApiData("http://slc16llt.us.oracle.com:8080/abacus/webapi/dashboard/buildPassStatus?start="+startDate+"&end="+endDate,filterData,header).then(function (data) {
    $scope.lastTenBuildDataPass=data;
    $timeout(function(){
      $scope.lineChart(  $scope.lastTenBuildDataPass);

    },1000);
  });

};





//linc chart to see status of pass test cases of 10 builds
$scope.DataParseLastTenBuild=function()
{var filterData=jobParameters;

  var header={
    'Content-Type': 'application/x-www-form-urlencoded',
  'jenkinURL':'http://'+localData[project1].host+':'+localData[project1].port,
  'jobName':job1,
  'userName':localData[project1].username,
  'passWord':localData[project1].password
};

$scope.lastTenBuildData=[];
pieService.getRestApiData("http://slc16llt.us.oracle.com:8080/abacus/webapi/dashboard/buildPassFailSkipStatus?start="+startDate+"&end="+endDate,filterData,header).then(function (data) {
$scope.lastTenBuildData=data;
  $timeout(function(){
  //console.log("<Ten>REST</Ten>");
    //console.log(angular.toJson($scope.lastTenBuildData));
    $scope.lineBarChart($scope.lastTenBuildData);
  },1000);
});

};

$scope.lineChart=function(lastTenBuildDataPass)
{
  console.log("data line chart");
console.log(lastTenBuildDataPass);
var passCount=lastTenBuildDataPass;
$scope.optionsLine = {
              chart: {
                  type: 'lineChart',

                  margin : {
                      top: 20,
                      right: 20,
                      bottom: 40,
                      left: 55
                  },
                  forceY:[0,100],
                  x: function(d){ return +d.x; },
                  y: function(d){ return +d.y; },

                  useInteractiveGuideline: false,
                  dispatch: {
                      tooltipShow: function(e){ console.log("tooltipShow"); },
                      tooltipHide: function(e){ console.log("tooltipHide"); }
                  },
                  xAxis: {
                      axisLabel: 'Execution Number(older -> newer)'
                  },
                  yAxis: {
                      axisLabel: 'Percentile',
                      tickFormat: function(d){
					                   return d3.format('')(d) + '%';
				                       },
                      	tickPadding: 3,
                      axisLabelDistance: -10
                  },
                  zoom: {
				//NOTE: All attributes below are optional
				enabled: true,
				scale: 1,
				scaleExtent: [1, 10],
				translate: [0, 0],
				useFixedDomain: false,
				useNiceScale: true,
				horizontalOff: false,
				verticalOff: false,
				unzoomEventType: 'dblclick.zoom'

			},
      tooltip: {
               contentGenerator: function (e) {
                 //console.log("Hello123");
                 //console.log(e);
                 var rows =
                    "<tr>" +
                      "<td class='key'>" + 'Pass: ' + "</td>" +
                      "<td class='x-value'>" + e.point.passCount + "</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td class='key'>" + 'Total: ' + "</td>" +
                      "<td class='x-value'><strong>" + e.point.totalCount + "</strong></td>" +
                    "</tr>";

                  var header =
                    "<thead>" +
                      "<tr>" +
                        "<td class='legend-color-guide'><div style='background-color:2ca02c;'></div></td>" +
                        "<td class='key'><strong>" +e.point.x+ "</strong></td>" +
                      "</tr>" +
                    "</thead>";
                 return "<table>" +
                      header +
                      "<tbody>" +
                        rows +
                      "</tbody>" +
                    "</table>";
               }},
                  callback: function(chart){
                      //console.log("!!! lineChart callback !!!");
                  }
              }
          };

          $scope.lineData = [
                  {
                      values: passCount,
                      key: 'Percentile',
                      color: '#2ca02c'
                  }
              ];

};

//To see the status of last 10 builds how many passed failed and skipped
$scope.lineBarChart=function(lastTenBuildData) {


  //console.log("<Ten>REST</Ten>");
  //console.log(angular.toJson(lastTenBuildData));
   var colors = [ "#00af00","#F7464A","#1e90ff"];

       $scope.options = {
           chart: {
               type: 'multiBarChart',
               reduceXTicks:false,
               forceY:[0,100],
               margin : {
                   top: 200,
                   right: 20,
                   bottom: 45,
                   left: 45
               },
               clipEdge: true,
               duration: 500,
               stacked: false,
            //   forceY:[0,100],
               xAxis: {
                   axisLabel: 'Execution Number(older->Newer)',
                   showMaxMin: false,
                   tickFormat: function(d){
                       return d;
                   }
               },
               yAxis: {
                   axisLabel: 'Count',
                   axisLabelDistance: -15,
                   tickFormat: function(d){
                          return d3.format('')(d) + '%';
                            },
               },

               color: function(d,i){
                 return (d.data && d.data.color) || colors[i % colors.length];
               },

               tooltip: {
                        contentGenerator: function (e) {
                        //  //console.log("eee");
                        //  //console.log(e.series[0].key);
                          var key;
                          var value;
                          var parameter=e.series[0].key;
                          if(parameter=='Passed')         {
                            key='Pass';
                            value=e.data.pass;
                          }
                            else
                              if(parameter=='Failed')
                              {
                                key='Fail';
                                value=e.data.fail;
                              }
                              else
                                if(parameter=='Skipped') {
                                key='Skip';
                                value=e.data.skip;
                              }


                          var rows =
                             "<tr>" +
                               "<td class='key'>" + key + "</td>" +
                               "<td class='x-value'>" + value + "</td>" +
                             "</tr>" +
                             "<tr>" +
                               "<td class='key'>" + 'Total ' + "</td>" +
                               "<td class='x-value'><strong>" + e.data.total + "</strong></td>" +
                             "</tr>";

                           var header =
                             "<thead>" +
                               "<tr>" +
                                 "<td class='legend-color-guide'><div style='background-color:2ca02c;'></div></td>" +
                                 "<td class='key'><strong>" +e.data.x+ "</strong></td>" +
                               "</tr>" +
                             "</thead>";
                          return "<table>" +
                               header +
                               "<tbody>" +
                                 rows +
                               "</tbody>" +
                             "</table>";
                         }},
           }
       };

       $scope.dataBarChart =lastTenBuildData;

   };
//this chart for showing the last build LastTenBuildStatus
$scope.lastBuildDataParse=function(selectedItem)
{
  var filterData=jobParameters;

    var header={
      'Content-Type': 'application/x-www-form-urlencoded',
    'jenkinURL':'http://'+localData[project1].host+':'+localData[project1].port,
    'jobName':job1,
    'userName':localData[project1].username,
    'passWord':localData[project1].password
  };
  pieService.getRestApiData("http://slc16llt.us.oracle.com:8080/abacus/webapi/dashboard/lastBuildTestInfo?start="+startDate+"&end="+endDate,filterData,header).then(function (dataLastBuild) {
    //console.log(dataLastBuild);
  $timeout(function(){
    $scope.barLineChartLastBuild(dataLastBuild,selectedItem);

  },1000);
  });




};

$scope.barLineChartLastBuild=function(dataLastBuild,selectedItem)
{


  var filterData={
            "abc":"aaa",
            "abcq":"aaa",

            };

    var header={
      'Content-Type': 'application/x-www-form-urlencoded',
    'jenkinURL':'http://'+localData[project1].host+':'+localData[project1].port,
    'jobName':job1,
    'userName':localData[project1].username,
    'passWord':localData[project1].password
  };

        pieService.getRestApiData("http://slc16llt.us.oracle.com:8080/abacus/webapi/dashboard/timeStamp?start="+startDate+"&end="+endDate,filterData,header).then(function (data) {
          //console.log("timestamp");
          //console.log(data);
          $scope.lastBuiildDate=data;

        });


if(selectedItem=='ALL')
  selectedItem=0;
  else
  selectedItem=parseInt(selectedItem.replace(/%/g,""));

var lineBarChartData=dataLastBuild;

var sizeOfModule=0;


$scope.dataLastBuild =lineBarChartData;
//console.log("Last Build Data");
//console.log($scope.dataLastBuild);
var colors = [ "#00af00","#F7464A","#1e90ff"];
 $scope.optionsLastBuild = {
           chart: {

             tooltip: {
                      contentGenerator: function (e) {
                      //  //console.log("Hello123");
                        ////console.log(e);
                        var key;
                        var value;
                        var parameter=e.series[0].key;
                        if(parameter=='Passed')        {
                          key='Pass';
                          value=e.data.pass;
                        }
                          else
                            if(parameter=='Failed'){
                              key='Fail';
                              value=e.data.fail;
                            }
                            else
                            if(parameter=='Skipped') {
                              key='Skip';
                              value=e.data.skip;
                            }


                        var rows =
                           "<tr>" +
                             "<td class='key'>" + key + "</td>" +
                             "<td class='x-value'>" + value + "</td>" +
                           "</tr>" +
                           "<tr>" +
                             "<td class='key'>" + 'Total ' + "</td>" +
                             "<td class='x-value'><strong>" + e.data.total + "</strong></td>" +
                           "</tr>";

                         var header =
                           "<thead>" +
                             "<tr>" +
                               "<td class='legend-color-guide'><div style='background-color:2ca02c;'></div></td>" +
                               "<td class='key'><strong>" +e.data.x+ "</strong></td>" +
                             "</tr>" +
                           "</thead>";
                        return "<table>" +
                             header +
                             "<tbody>" +
                               rows +
                             "</tbody>" +
                           "</table>";
                      }},
               type: 'multiBarChart',
               noData:$scope.helpMessage.noDataAvailable,
               forceY:[0,100],
               margin : {
                   top: 20,
                   right: 20,
                   bottom: 65,
                   left: 75
               },
               clipEdge: true,
               duration: 500,
               stacked: false,
              // reduceXTicks:true,
               xAxis: {
                   axisLabel: 'Module Name',
                   showMaxMin: false,
                   //rotateLabels: -30,
                   axisLabelDistance: 10,

               },
               yAxis: {
                   axisLabel: 'Percentage',
                   axisLabelDistance: -1,
                   tickFormat: function(d){
                          return d3.format('')(d) + '%';
                            },

               },
               color: function(d,i){
                 return (d.data && d.data.color) || colors[i % colors.length];
               }


           }
       };
};



//Gauge chart

$scope.callGauge=function(lastBuildStatus)
  {
    console.log("Clock");
    console.log(angular.toJson(lastBuildStatus.build));

    var passCount=lastBuildStatus.total-lastBuildStatus.skipCount-lastBuildStatus.failCount;

    var passPercentage=(passCount/lastBuildStatus.total)*100;
  console.log(passCount+" "+passPercentage);
          //console.log("Timeout occurred");
      var name = passCount+" / "+lastBuildStatus.total;
    //  //console.log("passPercentage:"+passPercentage);
      var value=Math.round(passPercentage);
      var gaugeMaxValue = 100;

      // données à calculer
      var percentValue = value / gaugeMaxValue;

      ////////////////////////

      var needleClient;



      (function(){

      var barWidth, chart, chartInset, degToRad, repaintGauge,
          height, margin, numSections, padRad, percToDeg, percToRad,
          percent, radius, sectionIndx, svg, totalPercent, width;



        percent = percentValue;

        numSections = 1;
        sectionPerc = 1 / numSections / 2;
        padRad = 0.025;
        chartInset = 10;

        // Orientation of gauge:
        totalPercent = .75;

        el = d3.select('.chart-gauge');

        margin = {
          top: 20,
          right: 20,
          bottom: 30,
          left: 20
        };

        width = el[0][0].offsetWidth - margin.left - margin.right;
        height = width+20;
        radius = Math.min(width, height) / 2;
        barWidth = 40 * width / 300;



        //Utility methods

        percToDeg = function(perc) {
          return perc * 360;
        };

        percToRad = function(perc) {
          return degToRad(percToDeg(perc));
        };

        degToRad = function(deg) {
          return deg * Math.PI / 180;
        };

        // Create SVG element
        svg = el.append('svg').attr('width', width + margin.left + margin.right).attr('height', height + margin.top + margin.bottom);

        // Add layer for the panel
        chart = svg.append('g').attr('transform', "translate(" + ((width + margin.left) / 2) + ", " + ((height + margin.top) / 2) + ")");


        chart.append('path').attr('class', "arc chart-third");
        chart.append('path').attr('class', "arc chart-second");
        chart.append('path').attr('class', "arc chart-first");


        arc3 = d3.svg.arc().outerRadius(radius - chartInset).innerRadius(radius - chartInset - barWidth);
        arc2 = d3.svg.arc().outerRadius(radius - chartInset).innerRadius(radius - chartInset - barWidth);
        arc1 = d3.svg.arc().outerRadius(radius - chartInset).innerRadius(radius - chartInset - barWidth);

        repaintGauge = function ()
        {
          perc = 0.5;
          var next_start = totalPercent;
          arcStartRad = percToRad(next_start);
          arcEndRad = arcStartRad + percToRad(perc / 3);
          next_start += perc / 3;


          arc1.startAngle(arcStartRad).endAngle(arcEndRad);

          arcStartRad = percToRad(next_start);
          arcEndRad = arcStartRad + percToRad(perc / 3);
          next_start += perc / 3;

          arc2.startAngle(arcStartRad + padRad).endAngle(arcEndRad);

          arcStartRad = percToRad(next_start);
          arcEndRad = arcStartRad + percToRad(perc / 3);

          arc3.startAngle(arcStartRad + padRad).endAngle(arcEndRad);

          chart.select(".chart-third").attr('d', arc1);
          chart.select(".chart-second").attr('d', arc2);
          chart.select(".chart-first").attr('d', arc3);


        };
      /////////

          var dataset = [{metric:name, value: value}]

          var texts = svg.selectAll("text")
                      .data(dataset)
                      .enter();

          texts.append("text")
               .text(function(){
                    return dataset[0].metric;
               })
               .attr('id', "Name")
               .attr('transform', "translate(" + ((210 ) ) + ", " + ((200 ) ) + ")")
               .attr("font-size",20)
               .style("fill", "#000000");


         var trX = 180 - 160 * Math.cos(percToRad(percent / 2));
      var trY = 195 - 210 * Math.sin(percToRad(percent / 2));
      // (180, 195) are the coordinates of the center of the gauge.


      displayValue =  chart.append("text")
                          .attr('id', "Value")
                          .attr("font-size",16)
                          .attr("text-anchor","middle")
                          .attr("dy","0.1em")
                          .style("fill", '#000000');
        formatValue = d3.format('1%');



          texts.append("text")
              .text(function(){
                  return 0;
              })
              .attr('id', 'scale0')
              .attr('transform', "translate(" + ((width + margin.left) / 100 ) + ", " + ((height + margin.top) / 2) + ")")
              .attr("font-size", 15)
              .style("padding",10)
              .style("fill", "#000000");




          texts.append("text")
              .text(function(){
                  return gaugeMaxValue;
              })
              .attr('id', 'scale20')
              .attr('transform', "translate(" + ((width + margin.left) / 1.03 ) + ", " + ((height + margin.top) / 2) + ")")
              .attr("font-size", 15)
              .style("fill", "#000000");

        var Needle = (function() {

          //Helper function that returns the `d` value for moving the needle
          var recalcPointerPos = function(perc) {
            var centerX, centerY, leftX, leftY, rightX, rightY, thetaRad, topX, topY;
            thetaRad = percToRad(perc / 2);
            centerX = 0;
            centerY = 0;
            topX = centerX - this.len * Math.cos(thetaRad);
            topY = centerY - this.len * Math.sin(thetaRad);
            leftX = centerX - this.radius * Math.cos(thetaRad - Math.PI / 2);
            leftY = centerY - this.radius * Math.sin(thetaRad - Math.PI / 2);
            rightX = centerX - this.radius * Math.cos(thetaRad + Math.PI / 2);
            rightY = centerY - this.radius * Math.sin(thetaRad + Math.PI / 2);


              return "M " + leftX + " " + leftY + " L " + topX + " " + topY + " L " + rightX + " " + rightY;




          };

          function Needle(el) {
            this.el = el;
            this.len = width / 2.5;
            this.radius = this.len / 8;
          }

          Needle.prototype.render = function() {
            this.el.append('circle').attr('class', 'needle-center').attr('cx', 0).attr('cy', 0).attr('r', this.radius);




            return this.el.append('path').attr('class', 'needle').attr('id', 'client-needle').attr('d', recalcPointerPos.call(this, 0));


          };

          Needle.prototype.moveTo = function(perc) {
            var self,
                oldValue = this.perc || 0;

            this.perc = perc;
            self = this;

            // Reset pointer position
            this.el.transition().delay(100).ease('quad').duration(200).select('.needle').tween('reset-progress', function() {
              return function(percentOfPercent) {
                var progress = (1 - percentOfPercent) * oldValue;




                repaintGauge(progress);
                return d3.select(this).attr('d', recalcPointerPos.call(self, progress));
              };
            });

            this.el.transition().delay(300).ease('bounce').duration(1500).select('.needle').tween('progress', function() {
              return function(percentOfPercent) {
                var progress = percentOfPercent * perc;

                repaintGauge(progress);
                var thetaRad = percToRad(progress / 2);
                var textX = - (self.len + 45) * Math.cos(thetaRad);
                var textY = - (self.len + 45) * Math.sin(thetaRad);

                displayValue.text(formatValue(progress))
                  .attr('transform', "translate("+textX+","+textY+")")
                return d3.select(this).attr('d', recalcPointerPos.call(self, progress));
              };
            });

          };


          return Needle;

        })();



        needle = new Needle(chart);
        needle.render();
        needle.moveTo(percent);

        setTimeout(displayValue, 1350);



      })();

  };
  $scope.lastBuildPassCount=function()
  {
    var filterData=jobParameters;

      var header={
        'Content-Type': 'application/x-www-form-urlencoded',
      'jenkinURL':'http://'+localData[project1].host+':'+localData[project1].port,
      'jobName':job1,
      'userName':localData[project1].username,
      'passWord':localData[project1].password
    };

    pieService.getRestApiData("http://slc16llt.us.oracle.com:8080/abacus/webapi/dashboard/lastBuildPassPercentage?start="+startDate+"&end="+endDate,filterData,header).then(function (data) {
      console.log("Call gauge");
    console.log(angular.toJson(data));
      $timeout(function(){
        $scope.callGauge( data);

      },1000);
    });


  };

});
