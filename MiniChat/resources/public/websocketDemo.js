
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/socket/");
webSocket.onmessage = function (msg) {
    updateChat(msg);
};
webSocket.onclose = function () {
    alert("WebSocket connection closed");
};


id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});


id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) {
        sendMessage(e.target.value);
    }
});


function sendMessage(message) {
    if (message !== "") {
        var json = '{"user":"' + id("sec_usuario").value + '","message":"' + message + '"}';
        webSocket.send(json);
        id("message").value = "";
    }
}


function updateChat(msg) {
    var data = JSON.parse(msg.data);
    insert("chat", data.userMessage);

}


function insert(targetId, message) {
    var $div = $('#chat');
    $div.animate({scrollTop: $div.prop('scrollHeight')}, 300);
    $div.append(message);
}


function id(id) {
    return document.getElementById(id);
}

