$("#uploadBtn").change(function () {
    $("#uploadFile").val(this.value.replace(/^.*[\\\/]/, ''));
});

$(function() {
 $('#uploadClick').click(function(){
   $("#img1Click").attr('src',"img/phones.png");
   $("#img2Click").attr('src',"img/phones.png");
   $("#icon1Click").attr('class',"fa fa-check fa-5x");
   $("#msg1Click").text("Success!");
   $("#img3Click").attr('src',"img/phones.png");
   $("#icon2Click").attr('class',"fa fa-check fa-5x");
   $("#msg2Click").text("Success!");
   return false;
 });
});
