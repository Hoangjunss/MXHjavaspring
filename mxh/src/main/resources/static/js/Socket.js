var socket = new SockJS('/ws'); // Thay đổi '/your-app/ws' thành đường dẫn WebSocket của bạn
var stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {//tao ket noi den web socket
    console.log('Connected: ' + frame);
    stompClient.subscribe('/user/queue/messages', function(message) { //lay du lieu tu kho chauqua duong dan
       
    });}
);
function sendMessage() {
    var content = document.getElementById('content').value;
    var id = document.getElementById('id').value;
    var message = {
        content: content,
        id: id
    };
    console.log(JSON.stringify(message));
    stompClient.send("/app/chat.send", {}, JSON.stringify({ message })); //dua len app
}