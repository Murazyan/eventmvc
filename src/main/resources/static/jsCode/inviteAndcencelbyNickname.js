var eventId = document.getElementById("current-eventId").value;
function f(picurl, eventId, userId, userNickname, userUsername) {
    return `
<style>
.tooltip {
    position: relative;
    display: inline-block;
    border-bottom: 1px dotted black;
    width: 100px;
}
.tooltip .tooltiptext {
    visibility: hidden;
    width: 250px;
    height: 14px;
    background-color: #b3e9ff;
    color: #0b0b0b;
    border-radius: 6px;
    padding: 5px 0;
    position: absolute;
    z-index: 1;
    opacity: 0;
    transition: opacity 5s;
}
.tooltip:hover .tooltiptext {
    visibility: visible;
    opacity:  5;
}
</style>
<article >
        <div style="width: 150px; height: 150px" class="a">
        <div style="overflow: hidden;float: left; width: 719px; vertical-align: text-top" class="b">
        <div style="overflow: hidden; vertical-align: text-top; float: left" class="c">
        <img src="/image?fileName=${picurl}" alt="" class="img-fluid mb20"
    style="height: 100px">
        </div>
        <div style="overflow: hidden; float:right; width: 151px">
        <h3 style="color:#720E9E; width: 100px">
        <div id="payman1" class="hravirel">
        <marquee behavior="alternate"><input type="submit" value="Հրավիրել"
    style="color: #720E9E; height: 26px" onclick="invite(${userId})">
        </input>
        </marquee>
        </div>
        <div id="payman2" class="chexarkel">
        <input type="submit" value="Չեղարկել հրավերը"
    style="color: #720E9E; height: 26px; width: 150px" onclick="cancelEvent(${userId})">
        <input type="hidden" id="currentEventId" name="canceledEventId" value="${eventId}">
        </div>
        </h3>
        </div>
        <div style=" overflow: hidden; float: right; width: 231px ;height: 150px" class="tooltip">
        <dl>
        <dt style="border: outset; color: #720E9E; background-color: #8CD2E5; border-style: groove; border-color: #21bfc1; margin-top: 20px">
        <i>Անուն</i></dt>
        <dd>${userNickname}</dd>
        <span class="tooltiptext">${userUsername}</span>
        </dl>
        </div>
        </div>
        </div>
        <hr style="border: dashed #d2b1b3 1px"/>
        </article>`
}
var search;
var old_ajax;
document.getElementById("resultss").style.display = "none";
function myFunction2() {
    document.getElementById("contactUserList").style.display = "none";
    var parrent = document.getElementById("resultss");
    var child = document.getElementById("child");
    if (child !== null) {
        parrent.removeChild(child);
    }
    document.getElementById("resultss").style.display = "initial";
    if (old_ajax) {
        old_ajax.abort();
    }
    clearTimeout(search);
    var results = document.getElementById("results");
    search = setTimeout(function () {
        old_ajax = $.ajax({
            url: "http://localhost:8081/search-contact-by-nickwithJson",
            data: {
                "key-nickname": document.getElementById("searchByNickname").value,
                "eventId": document.getElementById("current-eventId").value
            }
        }).done(function (body) {
            if (body.username === null) {
                document.getElementById("resultss").style.display = "none";
                document.getElementById("contactUserList").style.display = "initial";
                return;
            }
            child = document.createElement("div");
            child.id = "child";
            parrent.appendChild(child);
            var str = "";
            for (var i = 0; i < body.length; i++) {
                str += f(body[i].picUrl, eventId, body[i].userId, body[i].nickname, body[i].username) + "<br>";
            }
            results.innerHTML = str;
            for (var i = 0; i < body.length; i++) {
                if (body[i].isNull === 0 || body[i].isNull === 3) {
                    document.getElementsByClassName("hravirel")[i].style.display = "initial";
                    document.getElementsByClassName("chexarkel")[i].style.display = "none";
                }
                if (body[i].isNull === 2) {
                    document.getElementsByClassName("hravirel")[i].style.display = "none";
                    document.getElementsByClassName("chexarkel")[i].style.display = "initial";
                }
            }
        });
    }, 1000);
}
function invite(userId) {
    $.ajax({
        url: "http://localhost:8081/inviteWithAyax",
        data: {
            "userId": userId,
            "eventId": document.getElementById("current-eventId").value,
            "inputValue": document.getElementById("searchByNickname").value
        },
        method: "POST"
    }).done(function (responseJson){
            var str = "";
            for (var i = 0; i < responseJson.length; i++) {
                str += f(responseJson[i].picUrl, eventId, responseJson[i].userId, responseJson[i].nickname, responseJson[i].username) + "<br>";
            }
            results.innerHTML = str;
            for (var i = 0; i < responseJson.length; i++) {
                if (responseJson[i].isNull === 0 || responseJson[i].isNull === 3) {
                    document.getElementsByClassName("hravirel")[i].style.display = "initial";
                    document.getElementsByClassName("chexarkel")[i].style.display = "none";
                }
                if (responseJson[i].isNull === 2) {
                    document.getElementsByClassName("hravirel")[i].style.display = "none";
                    document.getElementsByClassName("chexarkel")[i].style.display = "initial";
                }
            }
        }
    )
}
function cancelEvent(userId) {
    $.ajax({
        url: "http://localhost:8081/cancelEventWithAyax",
        data: {
            "userId": userId,
            "eventId": document.getElementById("current-eventId").value,
            "inputValue": document.getElementById("searchByNickname").value
        },
        method: "POST"
    }).done(function (responseJson){
            var str = "";
            for (var i = 0; i < responseJson.length; i++) {
                str += f(responseJson[i].picUrl, eventId, responseJson[i].userId, responseJson[i].nickname, responseJson[i].username) + "<br>";
            }
            results.innerHTML = str;
            for (var i = 0; i < responseJson.length; i++) {
                if (responseJson[i].isNull === 0 || responseJson[i].isNull === 3) {
                    document.getElementsByClassName("hravirel")[i].style.display = "initial";
                    document.getElementsByClassName("chexarkel")[i].style.display = "none";
                }
                if (responseJson[i].isNull === 2) {
                    document.getElementsByClassName("hravirel")[i].style.display = "none";
                    document.getElementsByClassName("chexarkel")[i].style.display = "initial";
                }
            }
        }
    )
}