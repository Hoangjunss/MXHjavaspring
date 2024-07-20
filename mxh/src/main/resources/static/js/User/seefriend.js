// Hàm lấy danh sách bạn bè
function fetchFriends() {
    NProgress.start();
    fetch('/api/friends')
        .then(response =>  {
            if (!response.ok) {
                // Nếu response không OK, ném lỗi với message từ server
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        })
        .then(data => {
            const friendList = document.getElementById('friend-list');
            friendList.innerHTML = ''; // Xóa nội dung cũ
            data.relationships.forEach(relationship => {
                if(relationship.userTwo.id == data.user.id){ // Nếu userTwo là user hiện tại (user được gửi lời mời kết bạn)
                    fetchMutualFriendsCount(relationship.userOne.id).then(count => { 
                        const listItem = createFriendItem(relationship.userOne, 'acceptFriendRequest', 'deleteFriend', true, count);
                        friendList.appendChild(listItem);
                    });
                }
            });
            NProgress.done();
        })
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        });
}

// Hàm lấy danh sách bạn bè chưa kết bạn
function fetchNotFriends() {
    NProgress.start();
    fetch('api/notFriend')
        .then(response =>  {
            if (!response.ok) {
                // Nếu response không OK, ném lỗi với message từ server
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        })
        .then(data => {
            const notFriendList = document.getElementById('not-friend-list');
            notFriendList.innerHTML = ''; // Xóa nội dung cũ
            data.forEach(notFriend => {
                fetchMutualFriendsCount(notFriend.id).then(count => {
                    const listItem = createFriendItem(notFriend, 'addFriend', 'deleteFriend', false, count);
                    notFriendList.appendChild(listItem);
                });
            });
            NProgress.done();
        })
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        });
}

// Hàm tạo item bạn bè
function createFriendItem(friend, confirmCallback, deleteCallback, isFriend, mutualFriendsCount) {
    const listItem = document.createElement('li');
    listItem.className = 'friend';

    const friendCard = document.createElement('div');
    friendCard.className = 'friend-card';

    const friendInfo = document.createElement('div');
    friendInfo.className = 'friend-info';

    const name = document.createElement('a'); // Sử dụng thẻ <a> thay vì <p>
    name.className = 'name';
    name.href = `/profile?id=${friend.id}`; // Đường dẫn đến hồ sơ
    name.textContent = `${friend.firstName} ${friend.lastName}`;
    name.style.textDecoration = 'none'; // Xóa gạch chân mặc định của thẻ <a>
    name.style.color = 'white'; // Đổi màu chữ mặc định của thẻ <a>

    const mutualFriends = document.createElement('p');
    mutualFriends.className = 'mutual-friends';
    mutualFriends.textContent = `Bạn chung: ${mutualFriendsCount}`;

    const actionButton = document.createElement('button');
    actionButton.className = 'btn confirm';
    actionButton.textContent = isFriend ? 'Xác nhận' : 'Thêm bạn bè';

    // Xử lý sự kiện click cho nút xác nhận
    actionButton.onclick = () => {
        if (window[confirmCallback] && typeof window[confirmCallback] === 'function') {
            if (confirm(`Bạn có chắc muốn ${isFriend ? 'xác nhận yêu cầu kết bạn?' : 'gửi lời mời kết bạn?'}`)) {
                window[confirmCallback](friend.id, listItem);
                alert(isFriend ? `Bạn và ${friend.firstName} ${friend.lastName} đã trở thành bạn bè!` : 'Gửi lời mời kết bạn thành công!');
            }
        } else {
            console.error(`${confirmCallback} is not a function or not defined.`);
        }
    };

    friendInfo.appendChild(name);
    friendInfo.appendChild(mutualFriends);
    friendInfo.appendChild(actionButton);

    // Chỉ tạo nút xóa nếu isFriend là true
    if (isFriend) {
        const deleteButton = document.createElement('button');
        deleteButton.className = 'btn delete';
        deleteButton.textContent = 'Xóa';

        // Xử lý sự kiện click cho nút xóa
        deleteButton.onclick = () => {
            if (window[deleteCallback] && typeof window[deleteCallback] === 'function') {
                if (confirm('Xóa bạn bè?')) {
                    window[deleteCallback](friend.id, listItem);
                    alert('Xóa bạn bè thành công!');
                }
            } else {
                console.error(`${deleteCallback} is not a function or not defined.`);
            }
        };

        friendInfo.appendChild(deleteButton);
    }

    friendCard.appendChild(friendInfo);
    listItem.appendChild(friendCard);

    return listItem;
}

// Lấy số lượng bạn chung
function fetchMutualFriendsCount(friendId) {
    return fetch(`/api/mutualFriend?friendId=${friendId}`)
        .then(response =>  {
            if (!response.ok) {
                // Nếu response không OK, ném lỗi với message từ server
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'An unexpected error occurred');
                });
            }
            return response.json();
        })
        .then(data => {
            
            return data.count || 0; // Trả về số lượng bạn chung hoặc 0 nếu không có
        })
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        });
}

// Xử lý khi DOM được tải hoàn toàn
document.addEventListener("DOMContentLoaded", function () {
    // Tải danh sách bạn bè khi trang được tải
    fetchFriends();
    fetchNotFriends();
});

// Chấp nhận yêu cầu kết bạn
function acceptFriendRequest(friendId, listItem) {
    NProgress.start();
    fetch(`/api/relationship`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: friendId, status: 2 })
    })
    .then(response => {
        if (!response.ok) {
            // Nếu response không OK, ném lỗi với message từ server
            return response.json().then(errorData => {
                throw new Error(errorData.message || 'An unexpected error occurred');
            });
        }
        return response.json();
    })
    .then(data => {
        if(data.newStatus == 2){
            fetchFriends(); // Cập nhật lại danh sách bạn bè sau khi chấp nhận yêu cầu kết bạn
            fetchNotFriends(); // Cập nhật lại danh sách bạn bè chưa kết bạn sau khi chấp nhận yêu cầu kết bạn
        }
        NProgress.done();
    })
    .catch(error => {
        console.error('Error fetching chat messages:', error.message);
        // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
    });
}

// Xóa bạn bè
function deleteFriend(friendId, listItem) {
    NProgress.start();
    fetch(`/api/relationship`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: friendId, status: 4 }) // Đảm bảo rằng status là null khi xóa bạn bè
    })
    .then(response =>  {
        if (!response.ok) {
            // Nếu response không OK, ném lỗi với message từ server
            return response.json().then(errorData => {
                throw new Error(errorData.message || 'An unexpected error occurred');
            });
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            listItem.remove(); // Xóa item bạn bè khỏi danh sách
            fetchFriends(); // Tải lại danh sách bạn bè sau khi xóa thành công
            fetchNotFriends(); // Tải lại danh sách bạn bè chưa kết bạn sau khi xóa thành công
        } else {
            console.error('Failed to delete friend:', data.message);
            // Xử lý và hiển thị thông báo lỗi nếu cần
        }
        NProgress.done();
    })
    .catch(error => {
        console.error('Error fetching chat messages:', error.message);
        // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
    });
}

// Thêm bạn bè
function addFriend(friendId, listItem) {
    NProgress.start();
    fetch(`/api/relationship`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: friendId, status: 1 })
    })
    .then(response =>  {
        if (!response.ok) {
            // Nếu response không OK, ném lỗi với message từ server
            return response.json().then(errorData => {
                throw new Error(errorData.message || 'An unexpected error occurred');
            });
        }
        return response.json();
    })
    .then(data => {
        fetchFriends(); // Cập nhật lại danh sách bạn bè sau khi thêm bạn
        fetchNotFriends(); // Cập nhật lại danh sách bạn bè chưa kết bạn sau khi thêm bạn
        NProgress.done();
    })
    .catch(error => {
        console.error('Error fetching chat messages:', error.message);
        // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
    });
}
