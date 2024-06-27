var socket = new SockJS('/ws'); // Thay đổi '/your-app/ws' thành đường dẫn WebSocket của bạn
var stompClient = Stomp.over(socket);


stompClient.connect({}, function(frame) {//tao ket noi den web socket
    console.log('Connected: ' + frame);
    stompClient.subscribe('/user/queue/messages', function(message) { //lay du lieu tu kho chauqua duong dan
        var chatMessage = JSON.parse(message.body); 
        displayChatMessage(chatMessage);
        displayChatMessageFrame(chatMessage);
    });
    stompClient.subscribe('/queue/active', function(message) { //lay du lieu tu kho chauqua duong dan
    });
}
   
);
function sendMessage() {
    var content = document.getElementById('content').value;
    var id = document.getElementById('id').value;
    var message = {
        content: content,
        id: id
    };
    console.log(JSON.stringify(message));
    
    var chatContent = $('<li class="contentmessage message-reply">');
     var content=$('<p>').text(message.content);
     chatContent.append(content);
     $('#chatMessages').append(chatContent);
    stompClient.send("/app/chat.send", {}, JSON.stringify({ message })); //dua len app
}

    function displayChatMessage(message) {
        var chatContent = $('<li class="contentmessage message-receive">');
        var image = $(`<img src="images/users/user-1.jpg" alt="Conversation user image" />`)
        var content=$('<p>').text(message.content);
        chatContent.append(image);
        chatContent.append(content);
        $('#chatMessages').append(chatContent);
 }

function displayChatMessageFrame(message) {
    // Tìm thẻ li chứa data-user-id
    var contact = $('li.contact[data-user-id="' + message.id + '"]');
 
    if (contact.length > 0) {
        // Xóa thẻ li và thêm nó lên đầu danh sách
        contact.remove();
        contact.find('p.preview').text(message.content);
       
        $('ul.conversations').prepend(contact);
    }
}


 $(window).on('beforeunload', function() {
    $.ajax({
        url: '/logout',
        type: 'POST',
        async: false 
    });
});