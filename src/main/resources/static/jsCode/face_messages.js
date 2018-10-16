var currentEventId;
var currentUserId;
var messageText;
$(document).ready(
    function () {
        currentUserId = document.getElementById("currentUserId").value;
        messageText = document.getElementById("messageText").value;

    }
);
var currentparticipatingUserId;
var currentResult;
var maxCountScroll;
var webSocket;
if ('WebSocket' in window) {
    webSocket = new WebSocket("ws://" + window.location.host + "/saveAndSendMessages");
}
else {
    alert("socket chka")
}
const userIdObject = {
    currentUserId: currentUserId
};
const userIdString = JSON.stringify(userIdObject);

window.onbeforeunload = function () {
    closeWebSocket();
};

webSocket.onopen = function () {
    alert("open");
    webSocket.send(userIdString)
};

webSocket.onclose = function () {
    alert("Socket closed")

};

webSocket.onmessage = function (data) {
    alert("uxarkvel e namak` " + data.data);

};
webSocket.onerror = function () {
    alert("error");
};

function closeWebSocket() {
    webSocket.close();
}

function sendMessage() {
    messageText = document.getElementById("messageText").value;
    var messageObj = {
        currentEventId: currentEventId,
        currentUserId: currentUserId,
        messageText: messageText,
        participantUserId: currentparticipatingUserId

    };
    var messageString = JSON.stringify(messageObj);
    webSocket.send(messageString);
    document.getElementById("messageText").value = "";
    document.getElementById("allMessages").innerHTML += messageText;


}


function seeMessages(eventId, participatingUserId, num, eventCreaterUserId) {
    var messageInfo = {
        participantUserId: participatingUserId,
        eventCreaterUserId: eventCreaterUserId,
        currentUserId: currentUserId
    };
    var messageInfoString = JSON.stringify(messageInfo);
    webSocket.send(messageInfoString);
    currentEventId = eventId;
    currentparticipatingUserId = participatingUserId;
    $.ajax({
            url: "/seeMessages",
            method: "POST",
            data: {
                eventId: eventId,
                participatingUserId: participatingUserId
            }
        }
    ).done(function (response) {
            maxCountScroll = response.maxCountScroll;
            var parentDiv = document.getElementById("allMessages");
            var str = "";
            // alert("andhanur namakneri qanak"+ response.messagesList.length);
            var sovorakannamakneriqanak = 0;
            for (var i = response.messagesList.length - 1; i >= 0; i--) {
                // alert("mtav");
                var messageText = response.messagesList[i].messageText;
                var MessageEventCreaterUserId = response.messagesList[i].eventCreaterUserId;
                var messageToUserId = response.messagesList[i].participatingUserId;
                var CreaterSendStatus = response.messagesList[i].createrSendStatus;

                str += showAllMessages(messageText, MessageEventCreaterUserId, messageToUserId, CreaterSendStatus);
            }
            currentResult = str;
            parentDiv.innerHTML = str;
            parentDiv.scrollTop = document.querySelector(".allMessages").scrollHeight;
        }
    )
}
function showAllMessages(messageText, MessageEventCreaterUserId, messageToUserId, CreaterSendStatus) {
    var CurrentUserId = currentUserId;
    if (CurrentUserId == MessageEventCreaterUserId && CreaterSendStatus == true) { // երբ նամակները ընթացիկ օգտատերին է (ցույց ենք տալու աջ կողմում)
        return `<div  style="width: 100%; text-align: right;">
<div style="width: 70%; background: #c2e5d9; text-align: right; float: right;  border: 1px solid;
    border-radius: 25px">
<p style="overflow-wrap: break-word">${messageText}</p>
</div>
</div>`;
    }
    if (CurrentUserId == messageToUserId && CreaterSendStatus == false) { // երբ  նամակները ընթացիկ օգտատերին է (ցույց ենք տալու աջ կողմում)
        return `<div style="width: 100%;" >
<div style="width: 70%; background: #c2e5d9; text-align: right; float: right; border: 1px solid;
    border-radius: 25px;">
<p style="overflow-wrap: break-word">${messageText}</p>
</div>
</div>`;
    }
    if (CurrentUserId == messageToUserId && CreaterSendStatus == true) { // երբ  նամակները ընթացիկ օգտատերինը չեն (ցույց ենք տալու ձախ կողմում)
        return `<div style="width: 100%">
<div style="width: 70%; background: #bce5c0; text-align: left; float: left; border: 1px solid;
    border-radius: 25px;" > 
<p style="overflow-wrap: break-word">${messageText}</p>
</div >
</div>`;
    }
    if (CurrentUserId == MessageEventCreaterUserId && CreaterSendStatus == false) { // երբ  նամակները ընթացիկ օգտատերինը չեն (ցույց ենք տալու ձախ կողմում)
        return `<div style="width: 100%">
<div style="width: 70%; background: #bce5c0; text-align: left; float: left; border: 1px solid;
    border-radius: 25px;">
<p style="overflow-wrap: break-word">${messageText}</p>
</div >
</div>`;
    }
}

var scrolCount = 0;

function scrollFunction() {
    if (maxCountScroll <= scrolCount) {
        return;
    }
    var elmnt = document.getElementById("allMessages");
    var y = elmnt.scrollTop;
    if (y == 0) {
        $.ajax({
                url: "/plus45Messages",
                method: "POST",
                data: {
                    eventId: currentEventId,
                    participatingUserId: currentparticipatingUserId,
                    page: scrolCount + 1
                }
            }
        ).done(
            function (response) {
                scrolCount += 1;
                var parentDiv = document.getElementById("allMessages");
                var str = "";
                for (var i = response.length - 1; i >= 0; i--) {
                    var messageText = response[i].messageText;
                    var MessageEventCreaterUserId = response[i].eventCreaterUserId;
                    var messageToUserId = response[i].participatingUserId;
                    var CreaterSendStatus = response[i].createrSendStatus;
                    str += showAllMessages(messageText,MessageEventCreaterUserId,messageToUserId,CreaterSendStatus);
                }
                str += currentResult;
                currentResult = str;
                parentDiv.innerHTML = str;
                parentDiv.scrollTop = document.querySelector(".allMessages").scrollHeight / 3;
            }
        )
    }
}
function count(par) {
    par.innerHTML="";
}