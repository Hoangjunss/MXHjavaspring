document.addEventListener("DOMContentLoaded", function () {
    // Lấy phần tử HTML để hiển thị tin nhắn và tên người dùng chat
    const messageList = document.getElementById("chatMessages");
    const chatusername = document.getElementById("chatuser");
    const countMessageNotSeen = document.getElementById("countmessageseen");
    fetch('/messagermobile',{
        method:'GET'
    })
    .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
    .then(data => {
  data.relationship.forEach(relantionship=>{
    if(relantionship.userOne.id==data.user.id){

    }
    
  })
    })
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
                alert(data.relationship.id)
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
        contact.addEventListener('click', function (event) {
            event.preventDefault(); // Ngăn chặn hành vi mặc định của sự kiện
            const userId = this.querySelector('.user-id').value; // Lấy ID người dùng từ phần tử liên hệ
            showChat(userId); // Gọi hàm showChat với ID người dùng
        });
    });

    fetch('/m', {
        method: 'POST' // Send a POST request to the server
    })
    .then(response => response.json()) // Parse the response as JSON
    .then(data => {
        // Handle the response data
        console.log(data); // Check the data structure in the console
    
        // Example: Update the UI based on the received data
        const contactsList = document.getElementById('contact-list');
        contactsList.innerHTML = ''; // Clear previous content
    
        data.relationships.forEach(relationship => {
            const li = document.createElement('li');
            li.setAttribute('data-user-id', relationship.id);
            li.classList.add('contact');
    
            const link = document.createElement('a');
            link.href = `/chatmobile?id=${relationship.idUser}`;
            link.style.textDecoration = 'none';
    
            const wrapDiv = document.createElement('div');
            wrapDiv.classList.add('wrap');
    
            const statusSpan = document.createElement('span');
            statusSpan.classList.add('contact-status', 'online');
    
            const img = document.createElement('img');
            img.src = '/images/users/user-1.jpg'; // Update with actual image source
            img.alt = 'Conversation user';
    
            const unreadMessagesSpan = document.createElement('span');
            unreadMessagesSpan.classList.add('unread-messages');
            unreadMessagesSpan.setAttribute('data-id', relationship.id);
            unreadMessagesSpan.textContent = relationship.countMessNotSeen > 0 ? relationship.countMessNotSeen : '';
    
            const metaDiv = document.createElement('div');
            metaDiv.classList.add('meta');
    
            const nameP = document.createElement('p');
            nameP.classList.add('name');
            if(relationship.userOne.id == data.user.id){
                nameP.textContent = relationship.userTwo.lastName + relationship.userTwo.firstName;
            }
    
            const previewP = document.createElement('p');
            previewP.classList.add('preview');
            previewP.textContent = relationship.content ? relationship.content : '';
    
            // Append elements to construct the DOM structure
            metaDiv.appendChild(nameP);
            metaDiv.appendChild(previewP);
            wrapDiv.appendChild(statusSpan);
            wrapDiv.appendChild(img);
            wrapDiv.appendChild(unreadMessagesSpan);
            wrapDiv.appendChild(metaDiv);
            link.appendChild(wrapDiv);
            li.appendChild(link);
            contactsList.appendChild(li);
        });
    })
    .catch(error => {
        console.error('Error fetching data:', error);
        // Handle errors as needed
    });
    
});