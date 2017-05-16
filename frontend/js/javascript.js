$("#uploadBtn").change(function () {
    $("#uploadFile").val(this.value.replace(/^.*[\\\/]/, ''));
});

$(function() {
 $('#uploadClick').click(function(){
   $("#icon1Click").attr('class',"fa fa-check fa-5x");
   $("#msg1Click").text("Success!");
   $("#icon2Click").attr('class',"fa fa-check fa-5x");
   $("#msg2Click").text("Success!");
   createTable([[1, 1, 1, 1, 1], [1, 1, 1, 1, 1], [1, 1, 1, 1, 1], [1, 1, 1, 1, 1], [1, 1, 1, 1, 1], [1, 1, 1, 1, 1]]);
   return false;
 });
});

function createTable(tableData) {
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

  tableData.forEach(function(rowData) {
    table += "<tr>";

    rowData.forEach(function(cellData) {
      table += "<td>" + cellData + "</td>";
    });

    table += "</tr>";
  });
  table += "</table>";
  console.log(table);
  $("#tableClick").html(table);
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

new Morris.Line({
  // ID of the element in which to draw the chart.
  element: 'myfirstchart',
  // Chart data records -- each entry in this array corresponds to a point on
  // the chart.
  data: [
    { year: '2008', value: 20 },
    { year: '2009', value: 10 },
    { year: '2010', value: 5 },
    { year: '2011', value: 5 },
    { year: '2012', value: 20 }
  ],
  // The name of the data record attribute that contains x-values.
  xkey: 'year',
  // A list of names of data record attributes that contain y-values.
  ykeys: ['value'],
  // Labels for the ykeys -- will be displayed when you hover over the
  // chart.
  labels: ['Value']
});
