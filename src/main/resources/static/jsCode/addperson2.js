var search;
var old_ajax;

function myFunction() {
    if (old_ajax) {
        old_ajax.abort();
    }
    clearTimeout(search);
    search = setTimeout(function () {
        old_ajax = $.ajax({
            url: "http://localhost:8081/search-contact-by-username",
            data: {"key-username": document.getElementById("searchByUsername").value}
        }).done(function (body) {
            $("#results").html(body)
        });
    }, 1000)
    // if(document.getElementById("searchByUsername").value==""){
    //     alert("sada")
    //     document.getElementById("results").style.display="none";
    // }

}