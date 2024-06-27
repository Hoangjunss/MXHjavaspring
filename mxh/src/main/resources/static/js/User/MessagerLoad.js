document.addEventListener("DOMContentLoaded", function() {
    const messageList = document.getElementById("messages-container");
    const chatusername = document.getElementById("chatuser");
    function loadMessages(userId) {
        fetch('/chat?id=' + userId, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            messageList.innerHTML = ""; // Xóa nội dung cũ
            chatusername.innerHTML ="";
            const chatUser = data.chatUser;
            const chatheader = `
                <img src="/images/users/user-1.jpg" class="messenger-user" alt="Conversation user image" />
                <a href="#" class="message-profile-name">${chatUser.firstName} ${chatUser.lastName}</a>
            `;
            chatusername.innerHTML = chatheader;
            if (data.messages.length === 0) {
                messageList.textContent = "No messages found.";
            } else {
                data.messages.forEach(message => {
                    const messageElement = document.createElement("li");
                    messageElement.className = message.userFrom.id === data.currentUser.id ? 'message-reply' : 'message-receive';
                    messageElement.innerHTML = `
                        <img src="/images/users/user-1.jpg" alt="Conversation user image" />
                        <p>${message.content}</p>
                    `;
                    messageList.appendChild(messageElement);
                });
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
    }

    function showChat(userId) {
        console.log("Show chat for user with ID: " + userId);
        loadMessages(userId);
    }

    // Tải tin nhắn gần đây nhất khi trang được tải
    fetch('/messager')
        .then(response => response.json())
        .then(data => {
            // Cập nhật danh sách liên hệ
            const contactList = document.querySelector('.conversations');
            data.relationships.forEach(relationship => {
                const contactElement = document.createElement('li');
                contactElement.className = 'contact';
                contactElement.dataset.userId = relationship.id;
                contactElement.innerHTML = `
                    <div class="wrap">
                        <input type="hidden" value="${relationship.id}" class="user-id"/>
                        <span class="contact-status online"></span>
                        <img src="/images/users/user-1.jpg" alt="Conversation user" />
                        <div class="meta">
                            <p class="name">${relationship.name}</p>
                            <p class="preview">${relationship.content}</p>
                        </div>
                    </div>
                `;
                contactList.appendChild(contactElement);

                contactElement.addEventListener('click', function(event) {
                    event.preventDefault();
                    const userId = this.querySelector('.user-id').value;
                    showChat(userId);
                });
            });

            // Hiển thị tin nhắn gần đây nhất
            if (data.latestMessage) {
                loadMessages(data.latestMessage.userFrom.id);
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });

    // Gọi hàm showChat khi người dùng nhấn vào từng liên hệ
    const contacts = document.querySelectorAll('.contact');
    contacts.forEach(contact => {
        contact.addEventListener('click', function(event) {
            event.preventDefault();
            const userId = this.querySelector('.user-id').value;
            showChat(userId);
        });
    });
});
