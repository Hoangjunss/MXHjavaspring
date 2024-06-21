var socket = new SockJS('/ws'); // Thay đổi '/your-app/ws' thành đường dẫn WebSocket của bạn
var stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/user/queue/messages', function(message) {
       
    });}
);
function sendMessage() {
    var message = document.getElementById('message').value;
    stompClient.send("/app/chat.send", {}, JSON.stringify({ content: message }));
}