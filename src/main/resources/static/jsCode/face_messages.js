var currentEventId;
var currentparticipatingUserId;
var currentResult;
var maxCountScroll;

function seeMessages(eventId, participatingUserId, num) {
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
            for (var i = response.messagesList.length - 1; i >= 0; i--) {
                var messageText = response.messagesList[i].messageText;
                str += showAllMessages(messageText);
            }
            currentResult = str;
            parentDiv.innerHTML = str;
            parentDiv.scrollTop = document.querySelector(".allMessages").scrollHeight;
        }
    )
}

function showAllMessages(messageText) {
    return `<div>
<p>${messageText}</p>
</div>`
}

var scrolCount = 0;

function scrollFunction() {
    if(maxCountScroll<=scrolCount){
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
                    page: scrolCount+1
                }
            }
        ).done(
            function (response) {
                scrolCount += 1;
                var parentDiv = document.getElementById("allMessages");
                var str = "";
                for (var i = response.length - 1; i >= 0; i--) {
                    var messageText = response[i].messageText;
                    console.log(messageText);
                    str += showAllMessages(messageText);
                }
                str += currentResult;
                currentResult=str;
                parentDiv.innerHTML = str;
                parentDiv.scrollTop = document.querySelector(".allMessages").scrollHeight/3;
            }
        )
    }
}
