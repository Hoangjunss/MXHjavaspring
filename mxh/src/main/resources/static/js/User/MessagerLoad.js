document.addEventListener("DOMContentLoaded", function() {
    const messageList = document.getElementById("chatMessages");
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
