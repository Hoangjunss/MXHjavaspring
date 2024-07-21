document.addEventListener("DOMContentLoaded", function () {
    fetch('/api/usercurrent', {
        method: 'GET'
    }).then(response=>{
            if (!response.ok) {
                // Nếu response không OK, ném lỗi với message từ server
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        })
    .then(data => {
        const profileLink = document.getElementById('profileLink');
        const avatarImage = document.getElementById('avartaruser');
        profileLink.href = '/profile?id='+ data.id;
        if (data.image) {
            avatarImage.src = data.image.urlImage;
        } else {
            avatarImage.src = '/images/users/DefaultAvtUser.png';
        }
    })
    .catch(error => {
        console.error('Error fetching chat messages:', error.message);
        // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
    });

    fetch(`/api/countNotificationsIsCheck`, {
        method: 'GET'
    })
        .then(response =>{
            if (!response.ok) {
                // Nếu response không OK, ném lỗi với message từ server
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        })
        .then(data => {
            updateNotificationsIsCheck(data);
        })
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        });

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
    
        // Close the dropdown when clicking outside
        document.addEventListener('click', (e) => {
            if (!notificationItem.contains(e.target)) {
                dropdownMenu.classList.remove('show');
            }
        });
        dropContent.addEventListener('click', (e) => {
            e.stopPropagation();
        });

        fetchFriendRequestsCount().then(count => {
            const friendRequestCountElement = document.getElementById('friend-request-count');
            if (count > 0) {
                friendRequestCountElement.textContent = `(${count})`;
            } else {
                friendRequestCountElement.textContent = ''; // Hide count if zero
            }
        });
});



function updateNotificationsIsCheck(data) {
    console.log('updateNotificationsIsCheck');
    const unreadCount = data.unreadCount;
    const quantityNotification = document.getElementById('quantityNotification');
    if (unreadCount > 0) {
        quantityNotification.textContent = unreadCount;
        quantityNotification.style.display = 'inline';
    } else {
        quantityNotification.style.display = 'none';
    }
}

function fetchNotificationsList(dropContent) {
    console.log('fetchNotificationsList');
    fetch('/api/notifications', { method: 'GET' })
        .then(response =>{
            if (!response.ok) {
                // Nếu response không OK, ném lỗi với message từ server
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        })
        .then(data => {
            dropContent.innerHTML = '';
            data.notifications.forEach(notification => {
                const timeAgo = formatTimeAgo(notification.createAt);
                const notificationElement = document.createElement('li');
                notificationElement.innerHTML = `
                    <a href="${notification.url}" class="notification-link">
                        <div class="col-md-2 col-sm-2 col-xs-2">
                            <div class="notify-img">
                                <img src="${notification.userSend.image ? `${notification.userSend.image.urlImage}` : `/images/users/DefaultAvtUser.png`}" alt="notification user image">
                            </div>
                        </div>
                        <div class="col-md-10 col-sm-10 col-xs-10">
                            <span class="notification-type">${notification.message}</span>
                            <span class="notify-right-icon">
                                <i class='bx bx-radio-circle-marked'></i>
                            </span>
                            <p class="time">
                                ${timeAgo}
                            </p>
                        </div>
                    </a>
                `;
                dropContent.appendChild(notificationElement);

            });
        })
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        });
}

function markNotificationsAsRead() {
    console.log('markNotificationsAsRead');
    fetch('/api/notificationsischecked', {
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
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        });
}

// Đếm số lượng lời mời kết bạn chưa xác nhận
function fetchFriendRequestsCount() {
    console.log('fetchFriendRequestsCount');
    return fetch('/api/countfriend')
        .then(response =>{
            if (!response.ok) {
                // Nếu response không OK, ném lỗi với message từ server
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        })
        .then(data => {      
            // Tạo một biến để lưu số lượng lời mời kết bạn
            let friendRequestsCount = data.countFriend;
            
            data.relationships.forEach(relationship => {

                if (relationship.userOne.id === data.loggedInUser.id) {
                    // nếu userOne là user hiện tại (user gửi lời mời kết bạn) thì giảm biến đếm lên 1
                    
                    friendRequestsCount--;
                }
            });

            // Trả về số lượng lời mời kết bạn
            return friendRequestsCount;
        })
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        });
}

$(document).ready(function() {
    $('#logout-link').on('click', function(event) {
        event.preventDefault(); // Ngăn chặn hành vi mặc định của thẻ <a>

        $.ajax({
            url: '/logout', // Đường dẫn xử lý logout
            type: 'POST',
            success: function() {
                window.location.href = '/login'; // Chuyển hướng về trang đăng nhập sau khi logout thành công
            },
            error: function(error) {
                console.error('Logout failed:', error);
            }
        });
    });
});

function formatTimeAgo(date) {
    console.log('formatTimeAgo');
    const now = new Date();
    const postDate = new Date(date);
    const seconds = Math.floor((now - postDate) / 1000);
    
    let interval = Math.floor(seconds / 31536000);
    if (interval >= 1) return interval + (interval === 1 ? " year ago" : " years ago");
    
    interval = Math.floor(seconds / 2592000);
    if (interval >= 1) return interval + (interval === 1 ? " month ago" : " months ago");
    
    interval = Math.floor(seconds / 86400);
    if (interval >= 1) return interval + (interval === 1 ? " day ago" : " days ago");
    
    interval = Math.floor(seconds / 3600);
    if (interval >= 1) return interval + (interval === 1 ? " hour ago" : " hours ago");
    
    interval = Math.floor(seconds / 60);
    if (interval >= 1) return interval + (interval === 1 ? " minute ago" : " minutes ago");
    
    return Math.floor(seconds) + (seconds === 1 ? " second ago" : " seconds ago");
}