var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({},
            function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/user/queue/reply', function(notification) {
                        console.log(notification);

                showNotifications(JSON.parse(notification.body).content);
            });
        });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function showNotifications(notification) {
    $("#notifications").append("<tr><td>" + notification + "</td></tr>");
}

$(function () {
});