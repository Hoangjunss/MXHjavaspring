var socket = new SockJS('/ws'); // Thay đổi '/your-app/ws' thành đường dẫn WebSocket của bạn
var stompClient = Stomp.over(socket);


stompClient.connect({}, function(frame) {//tao ket noi den web socket
    console.log('Connected: ' + frame);
    stompClient.subscribe('/user/queue/messages', function(message) { //lay du lieu tu kho chauqua duong dan
        var chatMessage = JSON.parse(message.body); 
        displayChatMessage(chatMessage);
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

    var chatContent = $('<div class="message">').text("Message");
     var subject = $('<div class="subject">').text("Nguoi goi")
     var content=$('<p>').text("Nội dung: "+message.content);
     var name=$('<p>').text("Id: "+message.id);
     subject.append(content);
     subject.append(name);
     chatContent.append(subject);
     $('#chatMessages').append(chatContent);

    stompClient.send("/app/chat.send", {}, JSON.stringify({ message })); //dua len app
}

    function displayChatMessage(message) {
     var chatContent = $('<div class="message">').text("Message");
     var subject = $('<div class="subject">').text("Nguoi Goi"+message.userFrom.name)
     var content=$('<p>').text("Nội dung: "+message.content);
     var name=$('<p>').text("Thời gian: "+message.createAt);
     subject.append(content);
     subject.append(name);
     chatContent.append(subject);
     $('#chatMessages').append(chatContent);
 }
