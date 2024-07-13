// Chấp nhận yêu cầu kết bạn
function acceptFriendRequest(friendId, listItem) {
    fetch(`/relationship`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: friendId, status: 2 })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log('Friend request accepted:', data);
        if(data.newStatus == 2){
            listItem.remove();
        }
    })
    .catch(error => console.error('Error accepting friend request:', error));
}

// Xóa bạn bè
function deleteFriend(friendId, listItem) {
    fetch(`/relationship`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: friendId, status: 4 })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log('Friend deleted:', data);
        listItem.remove();
    })
    .catch(error => console.error('Error deleting friend:', error));
}

// Thêm bạn bè
function addFriend(friendId, listItem) {
    fetch(`/relationship`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: friendId, status: 1 })
    })
    .then(response => response.json())
    .then(data => {
        console.log('Friend added:', data);
        // Thêm mã HTML tại đây để di chuyển mục từ "not-friend-list" sang "friend-list"
    })
    .catch(error => console.error('Error adding friend:', error));
}


// Xử lý khi DOM được tải hoàn toàn
document.addEventListener("DOMContentLoaded", function () {

    // Hàm lấy danh sách bạn bè
    function fetchFriends() {
        fetch('/friends')
            .then(response => response.json())
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
            })
            .catch(error => console.error('Error fetching friends:', error));
    }

    // Hàm lấy danh sách bạn bè chưa kết bạn
    function fetchNotFriends() {
        fetch('/notFriend')
            .then(response => response.json())
            .then(data => {
                const notFriendList = document.getElementById('not-friend-list');
                notFriendList.innerHTML = ''; // Xóa nội dung cũ

                data.forEach(notFriend => {
                    fetchMutualFriendsCount(notFriend.id).then(count => {
                        const listItem = createFriendItem(notFriend, 'addFriend', 'deleteFriend', false, count);
                        notFriendList.appendChild(listItem);
                    });
                });
            })
            .catch(error => console.error('Error fetching not friends:', error));
    }


    // Hàm tạo item bạn bè
    function createFriendItem(friend, confirmCallback, deleteCallback, isFriend, mutualFriendsCount) {
        const listItem = document.createElement('li');
        listItem.className = 'friend';

        const friendCard = document.createElement('div');
        friendCard.className = 'friend-card';

        const friendInfo = document.createElement('div');
        friendInfo.className = 'friend-info';

        const name = document.createElement('p');
        name.className = 'name';
        name.textContent = `${friend.firstName} ${friend.lastName}`;

        const mutualFriends = document.createElement('p');
        mutualFriends.className = 'mutual-friends';
        mutualFriends.textContent = `Bạn chung: ${mutualFriendsCount}`;

        const actionButton = document.createElement('button');
        actionButton.className = 'btn confirm';
        actionButton.textContent = isFriend ? 'Xác nhận' : 'Thêm bạn bè';

        const deleteButton = document.createElement('button');
        deleteButton.className = 'btn delete';
        deleteButton.textContent = isFriend ? 'Xóa' : 'Gỡ';

        // Xử lý sự kiện click cho nút xác nhận
        actionButton.onclick = () => {
            if (window[confirmCallback] && typeof window[confirmCallback] === 'function') {
                if (confirm(`Bạn có chắc muốn ${isFriend ? 'xác nhận yêu cầu kết bạn?' : 'gửi lời mời kết bạn?'}`)) {
                    window[confirmCallback](friend.id, listItem);
                    alert(isFriend ? `Bạn và ${friend.firstName} ${friend.lastName} đã trở thành bạn bè!` : 'Gửi lời mời kết bạn thành công!');
                    listItem.remove(); // hoặc listItem.style.display = 'none'; để ẩn đi
                }
            } else {
                console.error(`${confirmCallback} is not a function or not defined.`);
            }
        };

        // Xử lý sự kiện click cho nút xóa
        deleteButton.onclick = () => {
            if (window[deleteCallback] && typeof window[deleteCallback] === 'function') {
                if (confirm('Xóa bạn bè?')) {
                    window[deleteCallback](friend.id, listItem);
                    alert('Xóa bạn bè thành công!');
                    listItem.remove(); // hoặc listItem.style.display = 'none'; để ẩn đi
                }
            } else {
                console.error(`${deleteCallback} is not a function or not defined.`);
            }
        };

        friendInfo.appendChild(name);
        friendInfo.appendChild(mutualFriends);
        friendInfo.appendChild(actionButton);
        friendInfo.appendChild(deleteButton);

        friendCard.appendChild(friendInfo);
        listItem.appendChild(friendCard);

        return listItem;
    }

    

    // Lấy số lượng bạn chung
    function fetchMutualFriendsCount(friendId) {
        return fetch(`/mutualFriend?friendId=${friendId}`)
            .then(response => response.json())
            .then(data => {
                console.log('Mutual friends count:', data.count);
                return data.count;
            })
            .catch(error => {
                console.error('Error fetching mutual friends count:', error);
                return 0; // Trả về 0 nếu có lỗi
            });
    }

    // Tải danh sách bạn bè khi trang được tải
    fetchFriends();
    fetchNotFriends();
});
