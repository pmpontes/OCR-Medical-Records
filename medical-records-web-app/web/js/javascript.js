$("#uploadBtn").change(function () {
    $("#uploadFile").val(this.value.replace(/^.*[\\\/]/, ''));
});

$(function() {
    $('#uploadClick').click(function(){
        var filePath = $("#uploadFile").val();
        window.location.replace('/results.jsp?file_name=' + encodeURI(filePath));
        return false;
    });
});

function createTable(patientRecords) {
    var table = "<table class='table table-bordered'>"+
        "<thead>"+
        "<tr>"+
        "<th>Data</th>"+
        "<th>Idade</th>"+
        "<th>Peso</th>"+
        "<th>Estat.</th>"+
        "<th>P. Cef</th>"+
        "</tr>"+
        "</thead>"+
        "<tbody>";

    patientRecords.recordTable.forEach(function(entry) {
        table += "<tr>";

        table += "<td>" + entry.date + "</td>";
        table += "<td>" + entry.age + "</td>";
        table += "<td>" + entry.weight + "</td>";
        table += "<td>" + entry.height + "</td>";
        table += "<td>" + entry.cephalicPerimeter + "</td>";

        table += "</tr>";
    });

    table += "</table>";
    $("#tableClick").html(table);
}

function createGraph(patientRecors) {
    new Morris.Line({
        // ID of the element in which to draw the chart.
        element: 'resultsChart',
        // Chart data records -- each entry in this array corresponds to a point on
        // the chart.
        data: [
            {year: '2008', value: 20},
            {year: '2009', value: 10},
            {year: '2010', value: 5},
            {year: '2011', value: 5},
            {year: '2012', value: 20}
        ],
        // The name of the data record attribute that contains x-values.
        xkey: 'year',
        // A list of names of data record attributes that contain y-values.
        ykeys: ['value'],
        // Labels for the ykeys -- will be displayed when you hover over the
        // chart.
        labels: ['Value']
    });
}

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#img1Click')
                .attr('src', e.target.result);
        };
        reader.readAsDataURL(input.files[0]);
    }
}

$(document).ready(function () {
    createTable(results);
    // createGraph(results);
});
