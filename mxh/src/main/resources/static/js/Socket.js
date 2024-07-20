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
            seenMessage(input.val());
        }
    }
    // Đăng ký để nhận tin nhắn mới từ hàng đợi `/user/queue/messages`
    stompClient.subscribe('/user/queue/messages', function (message) {
        try {
            var chatMessage = JSON.parse(message.body);
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
    stompClient.subscribe('/user/queue/addfriend', function(notification) {
        var addFriendNotification = JSON.parse(notification.body);
        displayAddFriendNotification(addFriendNotification); // Call your display function with the parsed notification
    });
});
function seenMessage(messages) {
    var message = {
        message: {
            id: messages
        }
    };
    stompClient.send("/app/chat.seen", {}, JSON.stringify(message))
    var contact = $('li.contact[data-relantionships-id="' + messages + '"]');
    if (contact.length > 0) {
        var delete1=contact.find('.unread-messages');
        delete1.remove();
    }
}

// Gửi tin nhắn tới WebSocket server
function sendMessage() {
    var content = document.getElementById('content').value;
    let contentt = content.trimStart().trimEnd();
    // Kiểm tra nếu nội dung chỉ chứa khoảng trắng
    if (contentt === '') {
        return;
    }
    var id = document.getElementById('id').value;
    var message = {
        message: {
            content: contentt,
            id: id
        }
    };
    // Hiển thị tin nhắn gửi trong khung chat trước khi gửi
    var chatContent = $('<li class="contentmessage message-reply">');
    var messageText = $('<p>').text(message.message.content);
    chatContent.append(messageText);
    $('#chatMessages').append(chatContent);
    var contact = $('li.contact[data-user-id="' + message.message.id + '"]');
    if (contact.length > 0) {
        contact.remove();
        contact.find('p.preview').text(message.message.content);
        $('ul.conversations').prepend(contact);
    }
    // Gửi tin nhắn tới `/app/chat.send` trên WebSocket
    stompClient.send("/app/chat.send", {}, JSON.stringify(message));
    content.value = '';
}


function sendAddFriend(){
    var idUserValue = document.getElementById('idUser').value;
    var isUser = {
        idUser: idUserValue
    };
    stompClient.send("/app/friend.add", {}, JSON.stringify(isUser));
}

function displayAddFriendNotification(addFriendNotification){
    const quantityNotification = $('span#quantityNotification');
    if(quantityNotification.length>0){
        let messageCount = parseInt(quantityNotification.text(), 10);
        if (isNaN(messageCount)) {
            // Set the message count to 0 if parsing failed
            messageCount = 0;
        }
        // Increment the message count
        messageCount += 1;
        quantityNotification.text(messageCount);
    }else {
        // Create the badge element and append it to the parent element
        const badge = $('<span>', {
            id: 'quantityNotification',
            class: 'badge-pill badge-primary unread-messages',
            text: 1
        });
        // Find the parent element and append the badge to it
        const parentElement = $('a[data-title="Notifications"]');
        parentElement.append(badge);
    }
    // Create the new notification element
    const newNotification = `
    <li>
        <a href="/profile?id=${addFriendNotification.userSend.id}" class="notification-link">
                        <div class="col-md-2 col-sm-2 col-xs-2">
                            <div class="notify-img">
                                <img src="${addFriendNotification.userSend.image ? `${addFriendNotification.userSend.image.urlImage}` : `/images/users/DefaultAvtUser.png`}" alt="notification user image" style="width: 40px;">
                            </div>
                        </div>
                        <div class="col-md-10 col-sm-10 col-xs-10">
                            <span class="notification-type">${addFriendNotification.message}</span>
                            <span class="notify-right-icon">
                                <i class='bx bx-radio-circle-marked'></i>
                            </span>
                            <p class="time">
                                Just Now
                            </p>
                        </div>
                    </a>
    </li>
    `;

    // Prepend the new notification to the notification list
    $('.drop-content').prepend(newNotification);
}


// Hiển thị tin nhắn nhận được trong khung chat
function displayChatMessage(message) {
    var inputElement = $('input[type="hidden"][data-messages-user="' + message.userFrom.id + '"]');

    $('input[type="hidden"]').each(function () {
    });
    if (inputElement.length) {

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
    const countMessageNotSeen = $('li.contact[data-relantionships-id="' + message.id + '"]').find('.unread-messages');
    if (countMessageNotSeen.length > 0) {
        // Retrieve the current text content and try to parse it as an integer
        let messageCount = parseInt(countMessageNotSeen.text(), 10);
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
            var contact = $('li.contact[data-relantionships-id="' + message.id + '"]').find('.wrap');
            var unreadMessageSpan = $('<span class="unread-messages">1</span>');

            // Thêm span vào trong li.contact
            contact.append(unreadMessageSpan);
        }
    }
    var contact = $('li.contact[data-user-id="' + message.userFrom.id+ '"]');
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

