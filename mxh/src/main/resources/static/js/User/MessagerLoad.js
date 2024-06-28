

document.addEventListener("DOMContentLoaded", function() {
    // Lấy phần tử HTML để hiển thị tin nhắn và tên người dùng chat
    const messageList = document.getElementById("chatMessages");
    const chatusername = document.getElementById("chatuser");
    // Hàm tải tin nhắn từ server
    function loadMessages(userId) {
        fetch('/chat?id=' + userId, {
            method: 'POST' // Gửi request POST đến server
        })
        .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
        .then(data => {
            messageList.innerHTML = ""; // Xóa nội dung cũ
            chatusername.innerHTML = ""; // Xóa tên người dùng chat cũ
            // Lấy thông tin người dùng chat từ dữ liệu phản hồi
            const chatUser = data.chatUser;
            // Tạo nội dung tiêu đề chat mới
            const chatheader = `
                <img src="/images/users/user-1.jpg" class="messenger-user" alt="Conversation user image" />
                <a href="#" class="message-profile-name">${chatUser.firstName} ${chatUser.lastName}</a>
            `;
            var inputElement = $('#id');

            // Thay đổi giá trị (value)
            inputElement.val(chatUser.id);

            // Thay đổi thuộc tính data-messages-user
            inputElement.attr('data-messages-user', chatUser.id);
            // Cập nhật tiêu đề chat với nội dung mới
            chatusername.innerHTML = chatheader;
            // Kiểm tra nếu không có tin nhắn
            if (data.messages.length === 0) {
                messageList.textContent = "No messages found.";
            } else {
                // Duyệt qua từng tin nhắn và tạo phần tử HTML cho mỗi tin nhắn
                data.messages.forEach(message => {
                    const messageElement = document.createElement("li");
                    // Xác định loại tin nhắn (gửi hoặc nhận) dựa trên ID người gửi
                    messageElement.className = message.userFrom.id === data.currentUser.id ? 'message-reply' : 'message-receive';
                    // Tạo nội dung tin nhắn
                    messageElement.innerHTML = `
                        <img src="/images/users/user-1.jpg" alt="Conversation user image" />
                        <p>${message.content}</p>
                    `;
                    // Thêm tin nhắn vào danh sách
                    messageList.appendChild(messageElement);
                });
            }
            seenMessage(data.relationship.id);
        })
        .catch(error => {
            console.error("Error:", error); // In lỗi ra console nếu có
        });
    }


    // Hàm hiển thị chat cho người dùng với ID cụ thể
    function showChat(userId) {
        console.log("Show chat for user with ID: " + userId);
        loadMessages(userId); // Gọi hàm loadMessages với ID người dùng
    }

    // Thêm sự kiện click cho mỗi liên hệ để hiển thị chat
    const contacts = document.querySelectorAll('.contact');
    contacts.forEach(contact => {
        contact.addEventListener('click', function(event) {
            event.preventDefault(); // Ngăn chặn hành vi mặc định của sự kiện
            const userId = this.querySelector('.user-id').value; // Lấy ID người dùng từ phần tử liên hệ
            showChat(userId); // Gọi hàm showChat với ID người dùng
        });
    });
});
