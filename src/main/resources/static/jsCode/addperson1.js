var x = document.getElementsByClassName("tooltiptext2");
for (var i = 0; i < x.length; i++) {
    x[i].style.display = "inline";
}
function show() {
    for (var i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }
}
function hide() {
    for (var i = 0; i < x.length; i++) {
        x[i].style.display = "initial";
    }
}
var searchByName = document.getElementsByClassName("searchByName")[0];
var searchByElpost = document.getElementsByClassName("searchByElpost")[0];
searchByElpost.style.display = "none";

function aa() {
    searchByName.style.display = "initial";
    searchByElpost.style.display = "none";
}

function bb() {
    searchByName.style.display = "none";
    searchByElpost.style.display = "initial";
}
