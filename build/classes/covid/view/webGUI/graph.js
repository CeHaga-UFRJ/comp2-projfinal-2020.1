var chart = document.getElementById('chart').getContext('2d');
var myChart;
var canvasWrapper = document.getElementById('canvasWrapper');

//requestChartData();

document.getElementById("form").onsubmit = submitAndLoad;

function submitAndLoad(){
    console.log("submitted");
    var rankType = document.getElementById("rankType").value;
    var startDate = document.getElementById("startDate").value;
    var endDate = document.getElementById("endDate").value;
    var exportType = document.getElementById("exportType").value;

    createExampleChart(rankType, startDate, endDate, exportType);
}

function createExampleChart(rankType, startDate, endDate, exportType){
    var path = "http://localhost:8080/projFinal/Servlet?rankType=" + rankType + "&startDate=" + startDate + "&endDate=" + endDate + "&exportType=" + exportType;
    fetch(path)
    .then(data => data.json())
    .then(json => createBarChartFromJson(json));
}

function createBarChartFromJson(json){
    if(myChart != null) myChart.destroy();
    var countryNames = [];
    var countryValues = [];
    json.forEach(element => {
        countryNames.push(element.Pais);
        countryValues.push(element.Casos);
    })
    myChart = newBarChart(countryNames);
    var dataset = createBarDataSet(countryValues);
    myChart.data.datasets.push(dataset);

    myChart.update();
}

function newBarChart(xLabels){
    myChart = new Chart(chart, {
        
        type: 'bar',
        data: {
            labels: xLabels,
            datasets: []
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });    

    canvasWrapper.style.width = xLabels.length * 50;
    return myChart;
}

function createBarDataSet(data){
    var dataset = {
        label: "Cases by Country",
        data: data,
        backgroundColor: [],
        borderColor: [],
        borderWidth: 1
    }

    //random colors
    data.forEach(element => {
        var r = Math.floor(Math.random() * 255);
        var g = Math.floor(Math.random() * 255);
        var b = Math.floor(Math.random() * 255);
        dataset.backgroundColor.push("rgba(" + r + "," + g + "," + b + "," + 0.4 +")");
        dataset.borderColor.push("rgba(" + r + "," + g + "," + b + "," + 0.6 +")");
    });

    return dataset;
}

function requestChartData(countrySlug, dataType, startDate, endDate){
    var request = new XMLHttpRequest();
    request.open('GET', "http://localhost:8080/projetoFinal/Servlet");
    request.onload = function(){
        var dataJson = JSON.parse(request.responseText);
        console.log(dataJson);
        createBarChartFromJson(dataJson);
    }
    request.send();
}


function updateDistancia(){
    if(document.getElementById("rankType").value == "MAIOR_PROXIMIDADE_DO_EPICENTRO"){
        document.getElementById("distancia").classList.remove("invisible");
        document.getElementById("labelDistancia").classList.remove("invisible");
    }
    else {
        document.getElementById("distancia").classList.add("invisible");
        document.getElementById("labelDistancia").classList.add("invisible");
    }
}