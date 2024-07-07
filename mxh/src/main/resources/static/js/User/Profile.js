document.addEventListener("DOMContentLoaded", function () {
    /* Notification */
    // Lấy userId từ URL
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    let isUserLogged;
    // Kiểm tra xem userId có tồn tại không
    if (userId) {
        fetchNotificationsIsCheck(userId);
        fetchUser(userId);
        fetchRelationship(userId);
        fetchUserAbouts(userId);
        fetchPostUser(userId);
    }

    // Hàm để lấy thông tin thông báo từ server
    function fetchNotificationsIsCheck(userId) {
        fetch(`/countNotificationsIsCheck?userId=${userId}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                updateNotificationsIsCheck(data);
            })
            .catch(error => console.error('Error fetching notifications:', error));
    }

    function fetchUser(userId) {
        fetch(`/api/getuser?id=${userId}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                updateProfile(data);
            })
            .catch(error => console.error('Error fetching user:', error));
    }

    function fetchRelationship(userId) {
        fetch(`/api/getrelationship?userId=${userId}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                updateRelationship(data);
                fetchStatusPost();
            })
            .catch(error => console.error('Error fetching relationship:', error));
    }

    function fetchUserAbouts(userId) {
        fetch(`/api/getabouts?userId=${userId}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                updateUserAbouts(data);
            })
            .catch(error => console.error('Error fetching user abouts:', error));
    }

    function fetchStatusPost() {
        fetch(`/status`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                updateStatusPost(data);
            })
            .catch(error => console.error('Error fetching status post:', error));
    }

    function fetchPostUser(userId) {
        alert(userId);
        fetch(`/post?id=${userId}`, {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                updatePostPage(data);
            })
            .catch(error => console.error('Error fetching status post:', error));
    }

    /* END API */

    document.getElementById('startChatButton').addEventListener('click', function () {
        if (window.innerWidth <= 768) { // Kích thước màn hình mobile (có thể điều chỉnh theo ý bạn)
            window.location.href = '/messagermobile';
        } else {
            window.location.href = '/messager';
        }
    });

    function previewImagePost(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                const imagePreview = document.getElementById('imagePreview');
                imagePreview.src = e.target.result;
                imagePreview.style.display = 'block';
            }
            reader.readAsDataURL(file);
        }
    }
    function previewImage(event) {
        var reader = new FileReader();
        reader.onload = function () {
            const previewBox = document.getElementById('preview_box');
            let showImage = document.createElement('img');
            showImage.src = reader.result;
            showImage.classList.add('preview');
            let deleteButton = document.createElement('span');

            let icon = document.createElement('i');
            icon.className = "fa-solid fa-xmark";
            deleteButton.appendChild(icon);
            let imageBox = document.createElement('div');
            imageBox.classList.add('imageBox');
            imageBox.appendChild(showImage);
            imageBox.appendChild(deleteButton);
            previewBox.appendChild(imageBox);

            document.getElementById('imageInput').style.display = 'none';
            const ImageBox = document.getElementById('image_box_item');

            removeMessage(ImageBox);

            deleteButton.addEventListener('click', () => {
                imageBox.remove();
                document.getElementById('imageInput').value = '';
                document.getElementById('imageInput').style.display = 'block';
            });

        };
        reader.readAsDataURL(event.target.files[0]);
    }

    const relationshipForms = document.querySelectorAll('.relationship-form');
    relationshipForms.forEach(form => {
        const buttons = form.querySelectorAll('.relationship-btn');
        buttons.forEach(button => {
            button.addEventListener('click', function () {
                const userId = form.querySelector('input[name="id"]').value;
                const status = button.getAttribute('data-status');
                alert(status + " " + userId);
                fetch('/relationship', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        userId: userId,
                        status: status
                    })
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            updateButtons(form, data.newStatus);
                        } else {
                            alert('Failed to update relationship status');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            });
        });
    });

    function updateButtons(form, newStatus) {
        const buttons = form.querySelectorAll('.relationship-btn');
        buttons.forEach(button => {
            switch (newStatus) {
                case 1:
                    if (button.getAttribute('data-status') == '1') {
                        button.textContent = 'Send to';
                        const recallButton = document.createElement('button');
                        recallButton.type = 'button';
                        recallButton.classList.add('btn', 'btn-follow', 'mr-3', 'relationship-btn');
                        recallButton.setAttribute('data-status', '4');
                        recallButton.innerHTML = "<i class='bx bx-plus'></i>Recall";
                        recallButton.addEventListener('click', function () {
                            updateButtons(form, 4);
                        });
                        form.appendChild(recallButton);
                    } else {
                        button.remove();
                    }
                    break;
                case 2:
                    if (button.getAttribute('data-status') == '2') {
                        button.textContent = 'UnFriend';
                        button.setAttribute('data-status', '4');
                    } else {
                        button.remove();
                    }
                    break;
                case 4:
                    if (button.getAttribute('data-status') == '4') {
                        button.textContent = 'Add Friend';
                        button.setAttribute('data-status', '1');
                        const recallButton = form.querySelector('button[data-status="4"]');
                        if (recallButton) recallButton.remove();
                    } else {
                        button.remove();
                    }
                    break;
            }
        });
    }
    /* GET API */
    const notificationItem = document.querySelector('.nav-item.s-nav.dropdown.notification');
    const dropdownMenu = notificationItem.querySelector('.dropdown-menu');
    const dropContent = dropdownMenu.querySelector('.drop-content');

    notificationItem.addEventListener('click', (e) => {
        alert("CLICK")
        e.preventDefault(); // Prevent default link behavior
        markNotificationsAsRead();
        dropdownMenu.classList.toggle('show');
        if (dropdownMenu.classList.contains('show')) {
            fetchNotificationsList();
        }
    });

    // Close the dropdown when clicking outside
    document.addEventListener('click', (e) => {
        if (!notificationItem.contains(e.target)) {
            dropdownMenu.classList.remove('show');
        }
    });

    function fetchNotificationsList() {
        fetch('/api/notifications', { method: 'GET' })
            .then(response => response.json())
            .then(data => {
                dropContent.innerHTML = '';
                data.notifications.forEach(notification => {
                    const notificationElement = document.createElement('li');
                    notificationElement.innerHTML = `
                        <div class="col-md-2 col-sm-2 col-xs-2">
                            <div class="notify-img">
                                <img src="#" alt="notification user image">
                            </div>
                        </div>
                        <div class="col-md-10 col-sm-10 col-xs-10">
                            <a href="${notification.url}" class="notification-user">${notification.user.firstName}</a>
                            <span class="notification-type">${notification.message}</span>
                            <a href="#" class="notify-right-icon">
                                <i class='bx bx-radio-circle-marked'></i>
                            </a>
                            <p class="time">
                                <span class="badge badge-pill badge-primary"><i class='bx bxs-group'></i></span>
                                30minutes ago
                            </p>
                        </div>
                    `;
                    dropContent.appendChild(notificationElement);

                });
            })
            .catch(error => console.error('Error:', error));
    }

    function markNotificationsAsRead() {
        fetch('/notificationsischecked', {
            method: 'POST'
        })
            .then(response => {
                if (response.ok) {
                    // Remove the unread messages count badge
                    const unreadMessagesBadge = document.querySelectorAll('.unread-messages');
                    unreadMessagesBadge.forEach(badge => badge.remove());
                } else {
                    console.error('Failed to mark notifications as read.');
                }
            })
            .catch(error => console.error('Error:', error));
    }
});

function fetchCommentsPost(id){
    fetch(`/comment?id=${id}`, {
        method: 'GET'
    })
       .then(response => response.json())
       .then(data => {
            console.log(data, id);
            updateCommentsPost(data, id);
        })
       .catch(error => console.error('Error fetching comments post:', error));
}

function displayEditProfileDetails() {
    const overlayAdd = document.getElementById('edit_profile');
    // Fetch dữ liệu từ endpoint
    fetch('/api/getabouts')
        .then(response => response.json())
        .then(data => {
            // Xử lý dữ liệu nhận được
            const abouts = data.abouts;
            const userAbouts = data.userAbouts;

            // Tạo HTML cho form dựa trên dữ liệu nhận được
            const formEditProfile = document.getElementById('form_edit_profile');
            formEditProfile.innerHTML = ''; // Xóa bỏ các phần tử trong form trước khi thêm mới
            let inputField = ''; // Sử dụng let thay vì const

            abouts.forEach((about, index) => {
                const userAbout = userAbouts.find(ua => ua.about.id === about.id);
                const description = userAbout ? userAbout.description : '';

                inputField = `
        <div class="name_box">
            <input type="hidden" name="userAboutDTOs[${index}].aboutId" value="${about.id}">
            <label>${about.name}</label>
            <input type="text" placeholder="${about.name}" name="userAboutDTOs[${index}].description" value="${description}">
        </div>
    `;
                formEditProfile.insertAdjacentHTML('beforeend', inputField);
            });

            const submitButton = `<button type="submit" id="submitBtn">Save</button>`;
            formEditProfile.insertAdjacentHTML('beforeend', submitButton);

            const overlayAdd = document.getElementById('edit_profile');
            overlayAdd.style.display = "flex";
        })
        .catch(error => console.error('Error fetching abouts:', error));
}

// Hàm đóng modal
function closeEditProfileDetails() {
    const overlayAdd = document.getElementById('edit_profile');
    overlayAdd.style.display = "none";
}

// Hàm để cập nhật thông tin thông báo đã đ��c
function updateNotificationsIsCheck(data) {
    const unreadCount = data.unreadCount;
    const quantityNotification = document.getElementById('quantityNotification');
    if (unreadCount > 0) {
        quantityNotification.textContent = unreadCount;
        quantityNotification.style.display = 'inline';
    } else {
        quantityNotification.style.display = 'none';
    }
}

function updateProfile(data) {
    const fullNameElement = document.querySelector('.profile-fullname');
    fullNameElement.textContent = `${data.user.firstName} ${data.user.lastName}`;
}

function updateRelationship(data) {
    isUserLogged = data.isOwnUser;
    const divsetfriend = document.getElementById('setfriend');
    const fromSetFriend = document.getElementById('setfrienduser');
    if (data.isOwnUser) {
        fromSetFriend.remove();
        divsetfriend.innerHTML += `
            <button type="button" class="btn btn-follow mr-3" id="editProfile"><i class='bx bx-plus'></i> Edit Profile</button>
        `;
    } else if (!data.isOwnUser) {
        if (data.relationship == null) {
            fromSetFriend.innerHTML += `
                <input type="hidden" name="id" th:value="${data.user.id}">
                <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="1"><i class='bx bx-plus'></i>Add Friend</button>
            `;
        } else if (data.relationship != null && data.relationship.status.id == 4) {
            fromSetFriend.innerHTML += ` 
                <input type="hidden" name="id" th:value="${data.user.id}">
                <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="1"><i class='bx bx-plus'></i>Add Friend</button>
                                           
            `;
        } else if (data.relationship != null && data.relationship.status.id != 4) {
            if (data.relationship.status.id == 1 && data.relationship.userOne.id == user.id) {
                fromSetFriend.innerHTML += `
                <input type="hidden" name="id" th:value="${data.user.id}">
                <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="2"><i class='bx bx-plus'></i>Accept</button>
                <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>Reject</button>
                                           
            `;
            } else if (data.relationship.status.id == 1 && data.relationship.userOne.id != user.id) {
                fromSetFriend.innerHTML += `
                    <input type="hidden" name="id" th:value="${data.user.id}">
                    <button type="button" class="btn btn-follow mr-3 relationship-btn"><i class='bx bx-plus'></i>Send to</button>
                    <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>Recall</button>                
                `;
            } else if (data.relationship.status.id == 2) {
                fromSetFriend.innerHTML += `
                    <input type="hidden" name="id" th:value="${data.user.id}">
                    <button type="button" class="btn btn-follow mr-3 relationship-btn" data-status="4"><i class='bx bx-plus'></i>UnFriend</button>
                                           
                `;
            }
        }
    }
}

function updateUserAbouts(data) {
    const aboutUserUI = document.getElementById('aboutuser');
    if (data.userAbouts != null) {
        data.userAbouts.forEach(userAbout => {
            data.abouts.forEach(about => {
                if (userAbout.about.id == about.id) {
                    aboutUserUI.innerHTML += `
                    <div class="intro-item d-flex justify-content-between align-items-center">
                                        <p class="intro-title text-muted"><i class='bx bx-briefcase text-primary'></i> 
                                                <strong>${about.name}: ${userAbout.description}</strong> 
                                            </p>
                                    </div>
                    `;
                }
            })
        })
        if (isUserLogged) {
            aboutUserUI.innerHTML += `<div class="intro-item d-flex justify-content-between align-items-center">
                                <button href="#" onclick="displayEditProfileDetails()" class="btn btn-quick-link join-group-btn border w-100" id="editProfileDetails">Edit Profile Details</button>
                            </div>
            `;
        }
    }
}

function updateStatusPost(data) {
    if (isUserLogged) {
        const statusSelect = document.getElementById('StatusId');
        data.status.forEach(stt => {
            const option = document.createElement('option');
            option.value = stt.id;
            option.textContent = stt.name;
            statusSelect.appendChild(option);
        })
    } else if (!isUserLogged) {
        document.getElementById('uploadpost').remove();
    }
}

function updatePostPage(data) {
    const divContainerPost = document.getElementById('containerpost');
    if (data.posts == null) {
        divContainerPost.innerHTML += `
        <h2 class="complex-effect-text">
            There are currently no posts. Let's share wonderful moments together
        </h2>
        `;
    } else {
        data.posts.forEach(post => {
            const imgUser = '#';
            const imgPost = post.image != null && post.image.urlImage != null ? post.image.urlImage : '#';
            const countComment = post.comments ? post.comments.length : 0;
            divContainerPost.innerHTML += `
            <ul class="list-unstyled" id="postuserupload"> 
                <div class="post border-bottom p-3 bg-white w-shadow">
                    <div class="media text-muted pt-3">
                        <img src="${imgUser}" alt="Online user" class="mr-3 post-user-image">
                        <form th:action="@{/hidepost}" method="post">
                            <input type="hidden" name="id" value="${post.id}">
                            <button type="submit">Ẩn</button>
                        </form>
                        <div class="media-body pb-3 mb-0 small lh-125">
                            <div class="d-flex justify-content-between align-items-center w-100">
                                <a href="${post.user.id}" class="text-gray-dark post-user-name">${post.user.firstName} ${post.user.lastName}</a>
                            </div>
                            <span class="d-block">3 hours ago <i class='bx bx-globe ml-3'></i></span>
                        </div>
                    </div>
                    <div id="displaycontent">
                        <div class="mt-3 ">
                            <p>${post.content}</p>
                            <div class="d-block mt-3">
                                <img src="${imgPost}" class="post-content" alt="post image">
                            </div>
                        </div>
                    </div>
                    <div class="mb-3">
                        <div class="argon-reaction">
                            <span class="like-btn">
                                <a href="#" class="post-card-buttons" id="reactions"><i class='bx bxs-like mr-2'></i></a>
                                <ul class="reactions-box dropdown-shadow">
                                    <li class="reaction reaction-like" data-reaction="Like"><button class="reaction-button" data-reaction-id="1" data-post-id="${post.id}"></button></li>
                                    <li class="reaction reaction-love" data-reaction="Love"><button class="reaction-button" data-reaction-id="2" data-post-id="${post.id}"></button></li>
                                    <li class="reaction reaction-haha" data-reaction="HaHa"><button class="reaction-button" data-reaction-id="3" data-post-id="${post.id}"></button></li>
                                    <li class="reaction reaction-wow" data-reaction="Wow"><button class="reaction-button" data-reaction-id="4" data-post-id="${post.id}"></button></li>
                                    <li class="reaction reaction-sad" data-reaction="Sad"><button class="reaction-button" data-reaction-id="5" data-post-id="${post.id}"></button></li>
                                    <li class="reaction reaction-angry" data-reaction="Angry"><button class="reaction-button" data-reaction-id="6" data-post-id="${post.id}"></button></li>
                                </ul>
                            </span>
                        </div>
                        <button class="post-card-buttons show-comments-btn" onclick="fetchCommentsPost(${post.id})" data-id="${post.id}"><i class='bx bx-message-rounded mr-2'></i>${countComment}</button>
                        <div class="dropdown dropup share-dropup">
                            <a href="#" class="post-card-buttons" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class='bx bx-share-alt mr-2'></i> Share
                            </a>
                            <div class="dropdown-menu post-dropdown-menu">
                                <a href="#" class="dropdown-item">
                                    <div class="row">
                                        <div class="col-md-2">
                                            <i class='bx bx-share-alt'></i>
                                        </div>
                                        <div class="col-md-10">
                                            <span>Share Now (Public)</span>
                                        </div>
                                    </div>
                                </a>
                                <a href="#" class="dropdown-item">
                                    <div class="row">
                                        <div class="col-md-2">
                                            <i class='bx bx-share-alt'></i>
                                        </div>
                                        <div class="col-md-10">
                                            <span>Share...</span>
                                        </div>
                                    </div>
                                </a>
                                <a href="#" class="dropdown-item">
                                    <div class="row">
                                        <div class="col-md-2">
                                            <i class='bx bx-message'></i>
                                        </div>
                                        <div class="col-md-10">
                                            <span>Send as Message</span>
                                        </div>
                                    </div>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="hide-comments" id="commentslist-${post.id}" data-id="${post.id}">
                        <!-- Comments will be loaded here -->
                    </div>
                </div>
            </ul>
            `;
        });
    }
}

function updateCommentsPost(data, postId) {
    const divContainerComment = document.getElementById(`commentslist-${postId}`);
    if (divContainerComment.classList.contains('show-comments')) {
        divContainerComment.classList.remove('show-comments');
        divContainerComment.classList.add('hide-comments');
        divContainerComment.style.maxHeight = 0;
        return; // Không thực hiện các hành động khác nếu đã ẩn thẻ
    }

    let commentsFrom = `
        <div class="row bootstrap snippets">
            <div class="col-md-12">
                <div class="comment-wrapper">
                    <div class="panel panel-info">
                        <div class="panel-body">
                            <ul class="media-list comments-list" id="comments-list" style="list-style:none;">
                                <li class="media comment-form">
                                    <a href="#" class="pull-left">
                                        <img th:src="@{/images/users/user-4.jpg}" alt="" class="img-circle">
                                    </a>
                                    <div class="media-body" data-post-id="${postId}">
                                        <form id="commentForm-${postId}" class="commentForm">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="input-group">
                                                        <input type="hidden" id="postId-${postId}" name="postId" value="${postId}">
                                                        <input type="text" id="content-${postId}" name="content" class="form-control comment-input" placeholder="Write a comment...">
                                                        <div class="input-group-btn">
                                                            <button type="submit" class="btn btn-primary">Comment</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </li>
    `;

    let displayComment = '';
    if (data.comments != null) {
        data.comments.forEach(comment => {
            displayComment += `
                <li class="media">
                    <a href="#" class="pull-left">
                        <img th:src="@{/images/users/user-2.jpg}" alt="" class="img-circle">
                    </a>
                    <div class="media-body">
                        <div class="d-flex justify-content-between align-items-center w-100">
                            <strong class="text-gray-dark"><a href="#" class="fs-8">${comment.userSend.firstName} ${comment.userSend.lastName}</a></strong>
                            <a href="#"><i class='bx bx-dots-horizontal-rounded'></i></a>
                        </div>
                        <span class="d-block comment-created-time">30 min ago</span>
                        <p class="fs-8 pt-2">${comment.content}</p>
                        <div class="commentLR">
                            <button type="button" class="btn btn-link fs-8">Like</button>
                            <button type="button" class="btn btn-link fs-8">Reply</button>
                        </div>
                    </div>
                </li>
            `;
        });

        displayComment += `
            <li class="media">
                <div class="media-body">
                    <div class="comment-see-more text-center">
                        <button type="button" class="btn btn-link fs-8">See More</button>
                    </div>
                </div>
            </li>
            </ul>
            </div>
            </div>
            </div>
            </div>
            </div>
        `;
    }

    divContainerComment.innerHTML = commentsFrom + displayComment;
    divContainerComment.classList.remove('hide-comments');
    divContainerComment.classList.add('show-comments');
    divContainerComment.style.maxHeight = `${divContainerComment.scrollHeight}px`;
}
