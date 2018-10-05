function data(notificationText, notificationDate, eventPicUrl, eventName) {
    return `<li class="clearfix">
                <a href="blog-lg-post-grid" class="float-left">
                    <img src="/image?fileName=${eventPicUrl}" class="img-fluid" alt="" width="60"></a>
                <div class="oHidden">
                    <span class="close"><i class="ti-close"></i></span>
                    <h4><a href="blog-lg-post-grid">${notificationText}</a></h4>
                    <p class="text-white-gray"><strong>${notificationDate}</strong></p>
                </div>
            </li>`
}

function seeAllNotification() {
    $.ajax({
        url: "http://localhost:8081/seeAllNotification",
        method: "POST"
    }).done(function (response) {
        // var notReadingNotificationCount = response.notReadingNotificationCount;
        var str = ""
        var notificationText;
        var notificationDate;
        var eventPicUrl;
        var eventName;
        for (var i = 0; i < response.length; i++) {
            notificationDate = (response[i].notificationDate).split(".")[0];
            eventPicUrl = response[i].eventPicUrl
            eventName = response[i].eventName;
            switch (response[i].notificationNumber) {
                case 1:
                    notificationText = "Հասանելի է նոր " + "<b style='color: #ccc614'>" + eventName + "</b>" + " իրադարձությունը";
                    break;
                case 2:
                    notificationText = "Խմբագրված է " + "<b style='color: #ccc614'>" + eventName + "</b>" + " իրադարձությունը";
                    break;
                case 3:
                    notificationText = "Հեռացված է " + "<b style='color: #ccc614'>" + eventName + "</b>" + " իրադարձությունը և այն հավանաբար տեղի չի ունենա";
                    break;
                case 4:
                    notificationText = "Դուք հրավիրված եք " + "<b style='color: #ccc614'>" + eventName + "</b>" + " իրադարձությանը";
                    break;
                case 5:
                    notificationText = "Հիշեցում․ Այսօր ժամը " + (notificationDate.split(" ")[1]).substring(0, 5) + " -ին տեղի է ունենալու " + "<b style='color: #ccc614'>" + eventName + "</b>" + " իրադարձությունը";
                    break;
                case 6:
                    notificationText = "Ձեր հրավերը չեղարկված է " + "<b style='color: #ccc614'>" + eventName + "</b>" + " իրադարձության համար։ Եթե ցանկանում եք մասնակցել իրադարձությանը, ապա հնարավորության դեպքում կարող ենք գրանցել";
                    break;
                case 7:
                    notificationText = "Դուք հրավիրված եք " + "<b style='color: #ccc614'>" + eventName + "</b>" + " իրադարձությանը: Սույ հաղորդագրությունը ստանալուց հետո 1 օրվա ընթացքում հրավերը չընդունելու դեպքում այն ավտոմատացված չեղարկվելու է համակարգի կողմից: Ցանկության դեպքում Դուք կարող եք նորից գրանցվել սույն իրադարձությանը մասնակցելու համար՝ ազատ տեղերի առկայության դեքում:";
                    break;
            }
            str += data(notificationText, notificationDate, eventPicUrl, eventName);
        }
        document.getElementById("ul_notifications").innerHTML = str;
    })
}

var notReadingNotificationCount = document.getElementById("notReadingNotificationCount");

function ayax() {
    $.ajax({
            url: "/notReadingNotificationCount"
        }
    ).done(
        function (response) {
            if (response.notReadingNotificationCount > 0) {
                notReadingNotificationCount.innerHTML = response.notReadingNotificationCount;
            }
            if(response.notReadingNotificationCount ==0){
                notReadingNotificationCount.innerHTML ="";
            }
        }
    )
}
$(document).ready(function () {
    setInterval(function () {
        ayax();
    }, 4000)
});