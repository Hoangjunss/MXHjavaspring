document.addEventListener("DOMContentLoaded", function () {
    // Lấy phần tử HTML để hiển thị tin nhắn và tên người dùng chat
    const messageList = document.getElementById("chatMessages");
    const chatusername = document.getElementById("chatuser");
    const countMessageNotSeen = document.getElementById("countmessageseen");

    const messageFrame = $('#contact-list');
    const messageContent = $('.content');
    if (messageFrame.length > 0 && messageContent.length > 0) {
        fetch('/api/messagermobile', {
            method: 'GET'
        })
            .then(response => {
                if (!response.ok) {
                    // Nếu response không OK, ném lỗi với message từ server
                    return response.json().then(errorData => {
                        throw new Error(errorData.message || 'An unexpected error occurred');
                    });
                }
                return response.json();
            }) // Chuyển đổi phản hồi thành JSON
            .then(data => {
                loadMessagesFrame(data);
                const idUser = $('#active').val();
                fetch('/api/chat?id=' + idUser, {
                    method: "GET"
                }).then(response => {
                    if (!response.ok) {
                        // Nếu response không OK, ném lỗi với message từ server
                        return response.json().then(errorData => {
                            throw new Error(errorData.message || 'An unexpected error occurred');
                        });
                    }
                    return response.json();
                }) // Chuyển đổi phản hồi thành JSON
                    .then(data => {
                        loadMessage(data);
                    })
                    .catch(error => {
                        console.error('Error fetching chat messages:', error.message);
                        // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
                    });    
            })
            .catch(error => {
                console.error('Error fetching chat messages:', error.message);
                // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
            });


    } else if (messageFrame.length > 0) {
        fetch('/api/messagermobile', {
            method: 'GET'
        })
            .then(response => {
                if (!response.ok) {
                    // Nếu response không OK, ném lỗi với message từ server
                    return response.json().then(errorData => {
                        throw new Error(errorData.message || 'An unexpected error occurred');
                    });
                }
                return response.json();
            }) // Chuyển đổi phản hồi thành JSON
            .then(data => {
                loadMessagesFrame(data);
            })
            .catch(error => {
                console.error('Error fetching chat messages:', error.message);
                // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
            });

    } else {
        const currentUrl = window.location.href;

        // Tạo đối tượng URLSearchParams từ URL
        const urlParams = new URLSearchParams(window.location.search);

        // Lấy giá trị của tham số 'name'
        const id = urlParams.get('id');
        if (id != null) {
            fetch('/api/chat?id=' + id, {
                method: "GET"
            }).then(response => {
                if (!response.ok) {
                    setRelationshipStatus(id);
                    // Nếu response không OK, ném lỗi với message từ server
                    return response.json().then(errorData => {
                        throw new Error(errorData.message || 'An unexpected error occurred');
                    });
                }
                return response.json();
            }) // Chuyển đổi phản hồi thành JSON
                .then(data => {
                    loadMessage(data);
                })
                .catch(error => {
                    console.error('Error fetching chat messages:', error.message);
                    // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
                });
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
            <li data-relantionships-id="${relantionships.id}" data-user-id="${user.id}" class="contact">
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
                     <span  class="unread-messages" style="background-color: blue !important;">${element[1]}</span>
                    `)
                    span.append(display);
                }
            })
        }
    }
    // Hàm tải tin nhắn từ server

    // Hàm hiển thị chat cho người dùng với ID cụ thể

    // Thêm sự kiện click cho mỗi liên hệ để hiển thị chat






});
function loadMessage(data) {
    const message = $('.content');
    if (message.length > 0) {
        message.empty();
        const display = $(`
            <div class="row">
                <div class="col-md-12 messenger-top-section">
                    <div class="contact-profile d-flex align-items-center justify-content-between">
                        <div class="messenger-top-luser df-aic">
                            <img src="${data.userTo && data.userTo.image ? data.userTo.image.urlImage : '/images/users/DefaultAvtUser.png'}" class="messenger-user" alt="Conversation user image" />
                            <a href="/profile?id=${data.userTo.id}" class="message-profile-name" style="text-decoration: none;">${data.userTo.lastName} ${data.userTo.firstName}</a>
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
                    <input type="hidden" id="idRelationship" value="${data.relation.id}">
                    <div class="messages">
                        <ul class="messages-content" id="chatMessages"></ul>
                    </div>
                </div>
                <div class="col-md-12 messageContetFrame">
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
        `);
        seenMessage(data.relation.id);

        message.append(display);
        scrollToBottom(message);
        const content = $('#chatMessages');
        if (content.length > 0) {
            data.messages.forEach(element => {
                const display = $(`
                    <li class="contentmessage ${element.userFrom.id == data.currentUser.id ? 'message-reply' : 'message-receive'}">
                        <p>${element.content}</p>
                    </li>
                `);

                if (element.userFrom.id != data.currentUser.id && element.userFrom.image) {
                    const img = $(`
                        <img src="${element.userFrom.image.urlImage}" alt="Conversation user image" />
                    `);
                    display.append(img);
                }

                content.append(display);
            });
        }
    }
}

function scrollToBottom(element) {
    element.scrollTop = element.scrollHeight;
}

function showChat(userId) {
    loadMessages(userId); // Gọi hàm loadMessages với ID người dùng
}
function loadMessages(userId) {
    fetch('/api/chat?id=' + userId, {
        method: 'GET' // Gửi request POST đến server
    })
        .then(response => {
            if (!response.ok) {
                // Nếu response không OK, ném lỗi với message từ server
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        }) // Chuyển đổi phản hồi thành JSON
        .then(data => {
            loadMessage(data);
        })
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        });

}
function setRelationshipStatus(userId) {
    const formData = {
        userId: userId,
        status: 1
    };
    $.ajax({
        url: '/api/relationship', // Update with the new URL
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(data) {
            if (data.success) {
                fetch('/api/chat?id=' + userId, {
                    method: "GET"
                }).then(response => {
                    if (!response.ok) {
                        // Nếu response không OK, ném lỗi với message từ server
                        return response.json().then(errorData => {
                            throw new Error(errorData.message || 'An unexpected error occurred');
                        });
                    }
                    return response.json();
                }) // Chuyển đổi phản hồi thành JSON
                    .then(data => {
                        loadMessage(data);
                    })
                    .catch(error => {
                        console.error('Error fetching chat messages:', error.message);
                        // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
                    });    
            }
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
}
