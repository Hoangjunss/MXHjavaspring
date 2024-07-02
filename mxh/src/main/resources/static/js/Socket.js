var socket = new SockJS('/ws'); // Thay đổi '/ws' thành đường dẫn WebSocket của bạn
var stompClient = Stomp.over(socket);
var isConnect;
document.addEventListener("DOMContentLoaded", function () {
    isConnect = true;
});

// Kết nối tới WebSocket server
stompClient.connect({}, function (frame) {
    if (isConnect) {
        var input = $('#idRelationship');
        if (input.length > 0) {
            alert(input.val());
            seenMessage(input.val());
        }
    }
    console.log('Connected: ' + frame);
    // Đăng ký để nhận tin nhắn mới từ hàng đợi `/user/queue/messages`
    stompClient.subscribe('/user/queue/messages', function (message) {
        try {
            console.log("Received message: ", message.body); // In toàn bộ thông điệp nhận được
            var chatMessage = JSON.parse(message.body);
            console.log("Parsed message userFrom id: ", chatMessage.userFrom.id);
            console.log("Message userFrom id: ", chatMessage.userFrom.id);// Parse dữ liệu JSON từ tin nhắn
            displayChatMessage(chatMessage); // Hiển thị tin nhắn trong khung chat
            displayChatMessageFrame(chatMessage);
        } catch (e) {
            console.error("Error parsing message body: ", e);
        }// Cập nhật liên hệ trong danh sách liên hệ
    });
    // Đăng ký để nhận thông báo từ hàng đợi `/queue/active` (nếu cần)
    stompClient.subscribe('/queue/active', function (message) {
        // Xử lý thông báo từ hàng đợi active nếu có
    });
    stompClient.subscribe('/user/queue/seen', function (message) {
    })
});
function seenMessage(messages) {
    var message = {
        message: {
            id: messages
        }
    };
    alert(messages);
    stompClient.send("/app/chat.seen", {}, JSON.stringify(message))
    const countMessageNotSeen = $('span.unread-messages[data-id="' + messages + '"]');
    if (countMessageNotSeen.length > 0) {
        countMessageNotSeen.remove();
    }
}

// Gửi tin nhắn tới WebSocket server
function sendMessage() {
    var content = document.getElementById('content').value;
    var id = document.getElementById('id').value;
    var message = {
        message: {
            content: content,
            id: id
        }
    };
    // Hiển thị tin nhắn gửi trong khung chat trước khi gửi
    var chatContent = $('<li class="contentmessage message-reply">');
    var messageText = $('<p>').text(message.message.content);
    chatContent.append(messageText);
    $('#chatMessages').append(chatContent);
    // Gửi tin nhắn tới `/app/chat.send` trên WebSocket
    stompClient.send("/app/chat.send", {}, JSON.stringify(message));
}



// Hiển thị tin nhắn nhận được trong khung chat
function displayChatMessage(message) {
    var inputElement = $('input[type="hidden"][data-messages-user="' + message.userFrom.id + '"]');

    $('input[type="hidden"]').each(function () {
        console.log("Existing input element with data-messages-user: ", $(this).attr('data-messages-user'));
    });
    console.log("Message userFrom id: ", message.userFrom.id);
    console.log(inputElement);
    if (inputElement.length) {
        console.log("11");

        seenMessage(message.id);
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
    const countMessageNotSeen = $('span.unread-messages[data-id="' + message.id + '"]');
    if (countMessageNotSeen.length > 0) {
        // Retrieve the current text content and try to parse it as an integer
        let messageCount = parseInt(countMessageNotSeen.text(), 10);
        console.log('hello');
        // Debugging: Log the initial value and the result of parseInt
        // Check if the parsing resulted in NaN
        if (isNaN(messageCount)) {
            // Set the message count to 0 if parsing failed
            messageCount = 0;
        }
        // Increment the message count
        messageCount += 1;
        countMessageNotSeen.text(messageCount);
    } else {
        var inputElement = $('input[type="hidden"][data-messages-user="' + message.userFrom.id + '"]');
        if (inputElement.length == 0) {
            console.log('Element not found for message.id:', message.id);
            var contact = $('li.contact[data-user-id="' + message.id + '"]').find('.wrap');
            console.log(contact);
            var unreadMessageSpan = $('<span class="unread-messages" data-id="' + message.id + '">1</span>');

            // Thêm span vào trong li.contact
            contact.append(unreadMessageSpan);
            console.log(contact);
        }
    }
    var contact = $('li.contact[data-user-id="' + message.id + '"]');
    if (contact.length > 0) {
        contact.remove();
        contact.find('p.preview').text(message.content);
        $('ul.conversations').prepend(contact);
    }
}

// Xử lý sự kiện khi người dùng rời khỏi trang (logout)
$(window).on('beforeunload', function () {
    $.ajax({
        url: '/logout', // Đường dẫn xử lý logout
        type: 'POST',
        async: false // Đồng bộ hóa yêu cầu
    });
});

