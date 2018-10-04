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
    document.getElementById("searchByUsername").value="";
}

function bb() {
    searchByName.style.display = "none";
    searchByElpost.style.display = "initial";
    document.getElementById("searchByNickname").value="";
}

function func1(a) {
    a.style.backgroundColor="#99BECC";
}
function func2(a) {
    a.style.backgroundColor="#9fcdff";
}
