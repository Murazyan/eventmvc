
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
    search = setTimeout(function () {
        old_ajax = $.ajax({
            url: "http://localhost:8081/search-contact-by-nickwithJson",
            data: {"key-nickname": document.getElementById("searchByNickname").value, "eventId": document.getElementById("current-eventId").value}
        }).done(function (body) {
            if (body.username === null) {
                document.getElementById("resultss").style.display = "none";
                document.getElementById("contactUserList").style.display = "initial";
                return;
            }
            child = document.createElement("div");
            child.id = "child";
            parrent.appendChild(child);
            for (var i = 0; i < body.length; i++) {
                var box = document.createElement("div");// div-որը պարունակում է նկարը անունը և հրավիրելու button@
                box.style.overflow = "hidden";
                var imageDiv = document.createElement("div");
                child.appendChild(box);
                box.appendChild(imageDiv);
                var infoDiv = document.createElement("div");
                box.appendChild(infoDiv);
                imageDiv.style.cssFloat = "left";
                imageDiv.style.width = "20%";
                imageDiv.style.height = "20%";
                infoDiv.style.width = "80%";
                infoDiv.style.height = "20%";
                infoDiv.style.cssFloat = "right";
                var img = document.createElement("img");
                img.src = "/image?fileName=" + body[i].picUrl;
                img.style.width = "90%";
                imageDiv.appendChild(img);
                var dl = document.createElement("dl");
                infoDiv.appendChild(dl);
                var dt = document.createElement("dt");
                dl.appendChild(dt);
                var dtNode = document.createTextNode("Անուն");
                dt.appendChild(dtNode);
                var dd = document.createElement("dd");
                dl.appendChild(dd);
                var ddNode = document.createTextNode(body[i].nickname);
                dd.appendChild(ddNode);
                var span = document.createElement("span");
                var stanNode = document.createTextNode(body[i].username);
                span.appendChild(stanNode);
                dl.appendChild(span);
                var invitation = document.createElement("div");
                box.appendChild(invitation);

                if(body[i].isNull===0 || body[i].isNull===3){
                    var picUrl = body[0].picUrl;
                    var f = document.createElement("form");
                    f.setAttribute('method',"post");
                    f.setAttribute('action',"/invitet");
                    invitation.appendChild(f);

                    var i = document.createElement("input"); //input element, submit
                    i.setAttribute('type',"submit");
                    i.setAttribute('value',"Հրավիրել");
                    f.appendChild(i);

                    var i1 = document.createElement("input"); //input element, hidden for current-Event-Id
                    i1.setAttribute('type', "hidden");
                    i1.setAttribute('value',document.getElementById("current-eventId").value);
                    i1.setAttribute('name',"invitedEventId");
                    f.appendChild(i1);
                    var i2 = document.createElement("input"); //input element, hidden for User-Id
                    i2.id="inputForUserId";
                    i2.value=picUrl;
                    i2.setAttribute('type', "hidden");
                    // i2.setAttribute('value',picUrl);
                    i2.setAttribute('name',"userPicUrl");
                    f.appendChild(i2);
                }
            }
        });
    }, 1000);
}

