document.addEventListener("DOMContentLoaded", function () {
    // Lấy phần tử HTML để hiển thị tin nhắn và tên người dùng chat
    const messageList = document.getElementById("chatMessages");
    const chatusername = document.getElementById("chatuser");
    const countMessageNotSeen = document.getElementById("countmessageseen");

    const messageFrame = $('#contact-list');
    const messageContent = $('.content');
    if (messageFrame.length > 0 && messageContent.length > 0) {
        fetch('/messagermobile', {
            method: 'GET'
        })
            .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
            .then(data => {
                loadMessagesFrame(data);
                const idUser = $('#active').val();
                fetch('/chat?id=' + idUser, {
                    method: "GET"
                }).then(response => response.json()) // Chuyển đổi phản hồi thành JSON
                    .then(data => {
                        loadMessage(data);
                    })
            })

        console.log(1);

    } else if (messageFrame.length > 0) {
        console.log(2);
        fetch('/messagermobile', {
            method: 'GET'
        })
            .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
            .then(data => {
                loadMessagesFrame(data);
            })

    } else {
        console.log(3);
        const currentUrl = window.location.href;

        // Tạo đối tượng URLSearchParams từ URL
        const urlParams = new URLSearchParams(window.location.search);

        // Lấy giá trị của tham số 'name'
        const id = urlParams.get('id');
        if (id != null) {
            fetch('/chat?id=' + id, {
                method: "GET"
            }).then(response => response.json()) // Chuyển đổi phản hồi thành JSON
                .then(data => {
                    loadMessage(data);
                })
        }
    }

    function loadMessagesFrame(data) {
        const frame = $('#contact-list');
        if (frame.length > 0) {
            var userActive;
            if (data.relantionships[0].userOne.id == data.user.id) {
                userActive = data.relantionships[0].userTwo;
            } else {
                userActive = data.relantionships[0].userOne;
            }
            const input = $(`
             <input type="hidden" value="${userActive.id}" id="active" >
            `)
            frame.append(input);
            data.relantionships.forEach(relantionships => {

                var user;
                if (relantionships.userOne.id == data.user.id) {
                    user = relantionships.userTwo;
                } else {
                    user = relantionships.userOne;
                }
                data.unseenMessages.forEach(unseenMessage => {
                    if (unseenMessage[0] == user.id) {
                        const display = $(`
            <li data-relantionships-id="${relantionships.id}" data-user-id="${user.id}" class="contact" >
                                <a href="/chatmobile?id=${user.id}" style="text-decoration: none;">
                                    <div class="wrap">
                                        <span class="contact-status online"></span>
                                        <img src="${user.image ? `${user.image.urlImage}` : `/images/users/DefaultAvtUser.png`}" alt="Conversation user" />
                                        <div class="meta">
                                            <p class="name" >${user.lastName} ${user.firstName}</p>
                                           <p class="preview">${unseenMessage[2]}</p>
                                        </div>
                                    </div>
                                </a>
                            </li>
            `)
                        frame.append(display);
                    }
                })
            })
            data.countMessNotSeen.forEach(element => {
                const span = $('li[data-user-id="' + element[0] + '"]').find('.wrap')
                if (element[1] > 0) {
                    const display = $(`
                     <span  class="unread-messages" >${element[1]}</span>
                    `)
                    span.append(display);
                }
            })
        }
    }
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
                const avtChatUser = chatUser.image ? chatUser.image.urlImage : '/images/users/DefaultAvtUser.png';
                const chatheader = `
                <img src="${avtChatUser}" class="messenger-user" alt="Conversation user image" />
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
                        <img src="${message.userFrom.image ? `${message.userFrom.image.urlImage}` : `/images/users/DefaultAvtUser.png`}" alt="Conversation user image" />
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


    function loadMessage(data) {
        const message = $('.content');
        if (message.length > 0) {
            const display = $(`
             
                    <div class="row">
                        <div class="col-md-12 messenger-top-section">
                            <div class="contact-profile d-flex align-items-center justify-content-between">
                                <div class="messenger-top-luser df-aic">
                                    <img src="${data.userTo.image ? `${data.userTo.image.urlImage}` : `/images/users/DefaultAvtUser.png`}" class="messenger-user" alt="Convarsation user image" />
                                    <a href="/profile?id=${data.userTo.id}" class="message-profile-name" style="text-decoration: none;"> ${data.userTo.lastName} ${data.userTo.firstName}</a>
                                </div>
                                <div class="social-media messenger-top-ricon df-aic">
                                    <img src="/images/messenger/phone.png" data-toggle="modal" data-target="#callModal" class="msg-top-more-info" alt="Messenger icons">
                                    <img src="/images/messenger/videocam.png" class="msg-top-more-info" alt="Messenger icons">
                                    <img src="/images/messenger/info.png" class="msg-top-more-info" alt="Messenger icons">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12" style="max-height: 532px;">
                            <input type="hidden" value="${data.userTo.id}" id="id" data-messages-user="${data.userTo.id}">
                            <input type="hidden" id="idRelationship" value="${data.relation.id}" >
                            <div class="messages">
                                <ul class="messages-content" id="chatMessages">
                                    
                                </ul>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="message-input">
                                <div class="wrap">
                                    <form class="d-inline form-inline">
                                        <div class="d-flex align-items-center justify-content-between messenger-icons">
                                            <div class="input-group messenger-input">
                                                <input id="content" type="text" class="form-control search-input" placeholder="Type a message..." aria-label="Type a message..." aria-describedby="button-addon2">
                                            </div>
                                            <button onclick="sendMessage()" type="button" class="btn search-button" id="send-message">
                                                <img src="/images/messenger/m-send.png" alt="Messenger icons">
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                
                `)
            message.append(display);
            const content = $('#chatMessages');
            if (content.length > 0) {
                data.messages.forEach(element => {
                    const display = $(`
                            <li class="contentmessage ${element.userFrom.id == data.currentUser.id ? 'message-reply' : 'message-receive'}" >
                                            
                                            <p >${element.content}</p>
                                        </li>
                            `)
                    if (element.userFrom.id != data.currentUser.id) {
                        const img = $(`
                                  <img 
                                    src="${element.userFrom.image ? `${element.userFrom.image.urlImage}` : `/images/users/DefaultAvtUser.png`}" 
                                    alt="Conversation user image" 
                                  />
                                `);

                        // Thêm thẻ <img> vào phần tử <li>
                        display.append(img);
                    }
                    content.append(display);
                })

            }
        }
    }

});