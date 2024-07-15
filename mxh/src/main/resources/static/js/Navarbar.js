document.addEventListener("DOMContentLoaded", function () {
    fetch('/api/usercurrent', {
        method: 'GET'
    }).then(response=> response.json())
    .then(data => {
        console.log(data);
        const profileLink = document.getElementById('profileLink');
        const avatarImage = document.getElementById('avartaruser');
        profileLink.href = '/profile?id='+ data.id;
        if (data.image) {
            avatarImage.src = data.image.urlImage;
        } else {
            avatarImage.src = '/images/users/DefaultAvtUser.png';
        }
    })
    .catch(error => console.error('Error fetching status post:', error));

    fetch(`/api/countNotificationsIsCheck`, {
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
    fetch('/api/notifications', { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            dropContent.innerHTML = '';
            data.notifications.forEach(notification => {
                const notificationElement = document.createElement('li');
                notificationElement.innerHTML = `
                    <a href="/profile?id=${notification.userSend.id}" class="notification-link">
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

// Đếm số lượng lời mời kết bạn chưa xác nhận
function fetchFriendRequestsCount() {
    return fetch('/api/countfriend')
        .then(response => response.json())
        .then(data => {      
            // Tạo một biến để lưu số lượng lời mời kết bạn
            let friendRequestsCount = data.countFriend;
            
            data.relationships.forEach(relationship => {

                if (relationship.userOne.id === data.loggedInUser.id) {
                    // nếu userOne là user hiện tại (user gửi lời mời kết bạn) thì giảm biến đếm lên 1
                    
                    console.log('Friend requests count:', data.countFriend);
                    friendRequestsCount--;
                }
            });

            // Trả về số lượng lời mời kết bạn
            return friendRequestsCount;
        })
        .catch(error => {
            console.error('Error fetching friend requests count:', error);
            return 0; // Trả về 0 trong trường hợp có lỗi
        });
}