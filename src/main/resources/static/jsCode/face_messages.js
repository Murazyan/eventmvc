var currentEventId;
var currentUserId;
var messageText;
var containerMessages;
$(document).ready(
    function () {
        containerMessages = document.getElementById("containerMessages");
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


window.onbeforeunload = function () {
    closeWebSocket();
};

webSocket.onopen = function () {
    $(document).ready(
        function () {
            var userIdObject = {
                CURRENT_USER_ID: currentUserId
            };
            var userIdString = JSON.stringify(userIdObject);
            webSocket.send(userIdString);
        }
    )


};

webSocket.onclose = function () {
    alert("Socket closed")

};

webSocket.onmessage = function (messageInfo) {
    var row = containerMessages.children;
    var messageText ;
    var toUserId;
    var createrUserId ;
    var statusNumber ;
    var createrSendStatus = true;
    var CurrentEventId ;
    if ((messageInfo.data).includes("_:_")) {
        var info = (messageInfo.data).split("_:_");
         messageText = info[0];
         toUserId = info[1];
        createrUserId = info[2];
         statusNumber = info[3];

         CurrentEventId = info[4];
        if (statusNumber == 0) {
            createrSendStatus = false;
        }
        if(currentparticipatingUserId == toUserId && currentEventId ==CurrentEventId){
            document.getElementById("allMessages").innerHTML += showAllMessages(messageText, createrUserId, toUserId, createrSendStatus);
            var parentDiv = document.getElementById("allMessages");
            parentDiv.scrollTop = document.querySelector(".allMessages").scrollHeight;
        }
    }
    if(messageInfo.data.includes("^_^")){
        // alert(messageInfo.data);
        var m_Info = (messageInfo.data).split("^_^");
        var m_eventId = m_Info[0];
        var m_ToUserId = m_Info[1];
        for (var i = 0; i < row.length; i++) {
            if (row[i].children[0].value == m_eventId && row[i].children[1].value == m_ToUserId) {
                if ((row[i].children[5]).children[0].innerHTML == "") {
                    (row[i].children[5]).children[0].innerHTML += "1";
                }else{
                    (row[i].children[5]).children[0].innerHTML = parseInt((row[i].children[5]).children[0].innerHTML)+1;
                }
            }
        }
 }
};


webSocket.onerror = function () {
    alert("error");
};

function closeWebSocket() {
    webSocket.close();
}

var messageToUserId;
var messageEventCreaterUserId;

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
    var str;
    if (currentUserId == messageEventCreaterUserId) {
        str = showAllMessages(messageText, messageEventCreaterUserId, messageToUserId, true);

    } else {
        str = showAllMessages(messageText, messageEventCreaterUserId, messageToUserId, false);
    }
    document.getElementById("allMessages").innerHTML += str;
    var parentDiv = document.getElementById("allMessages");
    parentDiv.scrollTop = document.querySelector(".allMessages").scrollHeight;
}


function seeMessages(eventId, participatingUserId, num, eventCreaterUserId) {
    messageEventCreaterUserId = eventCreaterUserId;
    messageToUserId = participatingUserId;
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
<div style="width: 55%; background: #c2e5d9; text-align: right; float: right;  border: 1px solid;
    border-radius: 25px">
<p style="word-break: break-all">${messageText}</p>
</div>
</div>`;
    }
    if (CurrentUserId == messageToUserId && CreaterSendStatus == false) { // երբ  նամակները ընթացիկ օգտատերին է (ցույց ենք տալու աջ կողմում)
        return `<div style="width: 100%;" >
<div style="width: 55%; background: #c2e5d9; text-align: right; float: right; border: 1px solid;
    border-radius: 25px;">
<p style="word-break: break-all">${messageText}</p>
</div>
</div>`;
    }
    if (CurrentUserId == messageToUserId && CreaterSendStatus == true) { // երբ  նամակները ընթացիկ օգտատերինը չեն (ցույց ենք տալու ձախ կողմում)
        return `<div style="width: 100%">
<div style="width: 55%; background: #bce5c0; text-align: left; float: left; border: 1px solid;
    border-radius: 25px;" > 
<p style="word-break: break-all">${messageText}</p>
</div >
</div>`;
    }
    if (CurrentUserId == MessageEventCreaterUserId && CreaterSendStatus == false) { // երբ  նամակները ընթացիկ օգտատերինը չեն (ցույց ենք տալու ձախ կողմում)
        return `<div style="width: 100%">
<div style="width: 55%; background: #bce5c0; text-align: left; float: left; border: 1px solid;
    border-radius: 25px;">
<p style="word-break: break-all">${messageText}</p>
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
                    str += showAllMessages(messageText, MessageEventCreaterUserId, messageToUserId, CreaterSendStatus);
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
    if (par) {
        par.innerHTML = "";
    }
}