function seeMessages(eventId, participatingUserId, num) {
    // document.getElementById("p_Messages").style.display = "none";
    $.ajax({
            url: "/seeMessages",
            method: "POST",
            data: {
                eventId: eventId,
                participatingUserId: participatingUserId
            }
        }
    ).done(function (response) {
            var parentDiv = document.getElementById("allMessages");
            var str = "";
        alert("ciklic araj");
            for (var i = 0; i < response.length; i++) {
                var messageText = response[i].messageText;
                str +=showAllMessages(messageText);
            }
            parentDiv.innerHTML=str;
        }
    )
}
function showAllMessages(messageText) {
    return `<div>
<p>${messageText}</p>
</div>`
}