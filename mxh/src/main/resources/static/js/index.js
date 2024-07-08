document.addEventListener("DOMContentLoaded", function () {
    fetch('/post',{
        method:'GET'
    })  .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
    .then(data => {
        console.log(data);
        displayPosts(data)
    })

    fetch('/status',{
        method:'GET'
    })  .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
    .then(data => {
        console.log(data);
        displaystatus(data);
    })
    .catch(error => console.error('Error fetching status post:', error));

    fetch(`/countNotificationsIsCheck`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            updateNotificationsIsCheck(data);
        })
        .catch(error => console.error('Error fetching notifications:', error));

    const notificationItem = document.querySelector('.nav-item.s-nav.dropdown.notification');
    const dropdownMenu = notificationItem.querySelector('.dropdown-menu');
    const dropContent = dropdownMenu.querySelector('.drop-content');
    notificationItem.addEventListener('click', (e) => {
        e.preventDefault(); // Prevent default link behavior
        markNotificationsAsRead();
        dropdownMenu.classList.toggle('show');
        if (dropdownMenu.classList.contains('show')) {
            fetchNotificationsList(dropContent);
        }
    });
    // Ngăn chặn sự kiện click lan truyền lên phần tử cha
    dropContent.addEventListener('click', (e) => {
    e.stopPropagation();
});
});

function fetchNotificationsList(dropContent) {
    fetch('/api/notifications', { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            dropContent.innerHTML = '';
            data.notifications.forEach(notification => {
                const notificationElement = document.createElement('li');
                notificationElement.innerHTML = `
                    <a href="${notification.url}" class="notification-link">
                        <div class="col-md-2 col-sm-2 col-xs-2">
                            <div class="notify-img">
                                <img src="#" alt="notification user image">
                            </div>
                        </div>
                        <div class="col-md-10 col-sm-10 col-xs-10">
                            <span class="notification-type">${notification.message}</span>
                            <span class="notify-right-icon">
                                <i class='bx bx-radio-circle-marked'></i>
                            </span>
                            <p class="time">
                                30 minutes ago
                            </p>
                        </div>
                    </a>
                `;
                dropContent.appendChild(notificationElement);

            });
        })
        .catch(error => console.error('Error:', error));
}

function displayPosts(data){
       const postDisplay=$('#displaycontent');
       postDisplay.empty();
       data.posts.forEach(post=>{
        const imgUser = '#';
                const imgPost =  post.image!=null && post.image.urlImage != null ? post.image.urlImage : '#';
                const countComment = post.comments ? post.comments.length : 0;
         const postElement = $(`<ul class="list-unstyled" id="postuserupload"> 
                <div class="post border-bottom p-3 bg-white w-shadow">
                    <div class="media text-muted pt-3">
                        <img src="${imgUser}" alt="Online user" class="mr-3 post-user-image">
                        <form th:action="@{/hidepost}" method="post">
                            <input type="hidden" name="id" value="${post.id}">
                            <button type="submit">Ẩn</button>
                        </form>
                        <div class="media-body pb-3 mb-0 small lh-125">
                            <div class="d-flex justify-content-between align-items-center w-100">
                                <a href="/profile?id=${post.user.id}" class="text-gray-dark post-user-name">${post.user.firstName} ${post.user.lastName}</a>
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
                        <button class="post-card-buttons show-comments-btn" onclick="showComment(${post.id})" data-id="${post.id}"><i class='bx bx-message-rounded mr-2'></i>${countComment}</button>
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
            `)
            postDisplay.append(postElement);
       });
}
function showComment(id){
    fetch('/comment?id='+id,{
        method:'GET'
    })  .then(response => response.json()) 
    .then(data => {
        displayComment(data, id)
    })
}   

function displayComment(data, postId) {
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
function showNotification(){
    fetch('/notifications',{
        method:'GET'
    })  .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
    .then(data => {
        displayNotification(data)
    })
}
function displayNotification(data){
    const notification=$('.dropContent');
    data.forEach(element=>{
        const display=$(`
            <li >
                                            <div class="col-md-2 col-sm-2 col-xs-2">
                                                <div class="notify-img">
                                                    <img th:src="@{/images/users/user-10.png}" alt="notification user image">
                                                </div>
                                            </div>
                                            <div class="col-md-10 col-sm-10 col-xs-10">
                                                <a th:href="@{${element.url}}" class="notification-user"">${element.user.firstName}</a> 
                                                <span class="notification-type">${element.message}</span>
                                                <a href="#" class="notify-right-icon">
                                                    <i class='bx bx-radio-circle-marked'></i>
                                                </a>
                                                <p class="time">
                                                    <span class="badge badge-pill badge-primary"><i class='bx bxs-group'></i></span> 
                                                    <!-- You can calculate and display time here -->
                                                </p>
                                            </div>
                                        </li>
            `)
    })
}
function displaystatus(data){
    const statusSelect = document.getElementById('StatusId');
        data.status.forEach(stt => {
            const option = document.createElement('option');
            option.value = stt.id;
            option.textContent = stt.name;
            statusSelect.appendChild(option);
        })
}

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