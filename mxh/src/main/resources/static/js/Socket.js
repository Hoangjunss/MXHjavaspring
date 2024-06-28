var socket = new SockJS('/ws'); // Thay đổi '/ws' thành đường dẫn WebSocket của bạn
var stompClient = Stomp.over(socket);

// Kết nối tới WebSocket server
stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);

    // Đăng ký để nhận tin nhắn mới từ hàng đợi `/user/queue/messages`
    stompClient.subscribe('/user/queue/messages', function(message) {
        var chatMessage = JSON.parse(message.body); // Parse dữ liệu JSON từ tin nhắn
        displayChatMessage(chatMessage); // Hiển thị tin nhắn trong khung chat
        displayChatMessageFrame(chatMessage); // Cập nhật liên hệ trong danh sách liên hệ
    });

    // Đăng ký để nhận thông báo từ hàng đợi `/queue/active` (nếu cần)
    stompClient.subscribe('/queue/active', function(message) {
        // Xử lý thông báo từ hàng đợi active nếu có
    });
});

// Gửi tin nhắn tới WebSocket server
function sendMessage() {
    var content = document.getElementById('content').value;
    var id = document.getElementById('id').value;
    var message = {
        content: content,
        id: id
    };

    // Hiển thị tin nhắn gửi trong khung chat trước khi gửi
    var chatContent = $('<li class="contentmessage message-reply">');
    var messageText = $('<p>').text(message.content);
    chatContent.append(messageText);
    $('#chatMessages').append(chatContent);

    // Gửi tin nhắn tới `/app/chat.send` trên WebSocket
    stompClient.send("/app/chat.send", {}, JSON.stringify(message));
}

// Hiển thị tin nhắn nhận được trong khung chat
function displayChatMessage(message) {
    var inputElement = $('input[type="hidden"][data-messages-user="' + message.userFrom.id + '"]');
    if(inputElement.length){
        var chatContent = $('<li class="contentmessage message-receive">');
    var image = $('<img src="images/users/user-1.jpg" alt="Conversation user image" />');
    var content = $('<p>').text(message.content);
    chatContent.append(image);
    chatContent.append(content);
    $('#chatMessages').append(chatContent);
    }

  
}

// Cập nhật liên hệ trong danh sách liên hệ khi có tin nhắn mới
function displayChatMessageFrame(message) {
    // Tìm thẻ li có data-user-id tương ứng
    var contact = $('li.contact[data-user-id="' + message.id + '"]');
 
    if (contact.length > 0) {
        // Xóa thẻ li hiện tại và thêm vào đầu danh sách
        contact.remove();
        contact.find('p.preview').text(message.content); // Cập nhật nội dung preview (nếu có)
        $('ul.conversations').prepend(contact);
    }
}

// Xử lý sự kiện khi người dùng rời khỏi trang (logout)
$(window).on('beforeunload', function() {
    $.ajax({
        url: '/logout', // Đường dẫn xử lý logout
        type: 'POST',
        async: false // Đồng bộ hóa yêu cầu
    });
});
