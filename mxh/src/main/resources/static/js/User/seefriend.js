document.addEventListener("DOMContentLoaded", function () {
    fetchFriends();
    fetchNotFriends();
    fetchMutualFriendsCount();
});

// Lấy danh sách bạn bè
function fetchFriends() {
    fetch('/friends')
        .then(response => response.json())
        .then(data => {
            const friendList = document.getElementById('friend-list');
            friendList.innerHTML = ''; // Clear the list first
            data.forEach(friend => {
                const listItem = createFriendItem(friend, 'acceptFriendRequest', 'deleteFriend');
                friendList.appendChild(listItem);
            });
        })
        .catch(error => console.error('Error fetching friends:', error));
}

// Lấy danh sách bạn bè chưa kết bạn
function fetchNotFriends() {
    fetch('/notFriend')
        .then(response => response.json())
        .then(data => {
            const notFriendList = document.getElementById('not-friend-list');
            notFriendList.innerHTML = ''; // Clear the list first
            data.forEach(notFriend => {
                const listItem = createFriendItem(notFriend, 'addFriend', 'deleteFriend');
                notFriendList.appendChild(listItem);
            });
        })
        .catch(error => console.error('Error fetching not friends:', error));
}

// Tạo item bạn bè
function createFriendItem(friend, confirmCallback, deleteCallback) {
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
    mutualFriends.textContent = `Bạn chung: ${friend.mutualFriendsCount}`;

    const confirmButton = document.createElement('button');
    confirmButton.className = 'btn confirm';
    confirmButton.textContent = 'Xác nhận';
    confirmButton.onclick = () => window[confirmCallback](friend.id);

    const deleteButton = document.createElement('button');
    deleteButton.className = 'btn delete';
    deleteButton.textContent = 'Xóa';
    deleteButton.onclick = () => window[deleteCallback](friend.id);

    friendInfo.appendChild(name);
    friendInfo.appendChild(mutualFriends);
    friendInfo.appendChild(confirmButton);
    friendInfo.appendChild(deleteButton);

    friendCard.appendChild(friendInfo);
    listItem.appendChild(friendCard);

    return listItem;
}

// xác nhận yêu cầu kết bạn
function acceptFriendRequest(friendId) {
    fetch(`/acceptFriendRequest/${friendId}`, { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            console.log('Friend request accepted:', data);
            fetchFriends();
        })
        .catch(error => console.error('Error accepting friend request:', error));
}

// xóa bạn bè
function deleteFriend(friendId) {
    fetch(`/deleteFriend/${friendId}`, { method: 'DELETE' })
        .then(response => response.json())
        .then(data => {
            console.log('Friend deleted:', data);
            fetchFriends();
            fetchNotFriends();
        })
        .catch(error => console.error('Error deleting friend:', error));
}

// thêm bạn bè
function addFriend(friendId) {
    fetch(`/addFriend/${friendId}`, { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            console.log('Friend added:', data);
            fetchNotFriends();
        })
        .catch(error => console.error('Error adding friend:', error));
}

// Lấy số lượng bạn chung
function fetchMutualFriendsCount() {
    fetch('/mutualFriend')
        .then(response => response.json())
        .then(data => {
            const mutualFriendsCount = document.getElementById('mutual-friends');
            mutualFriendsCount.textContent = data.count;
            console.log('Mutual friends count:', data.count);
        })
        .catch(error => console.error('Error fetching mutual friends count:', error));
}
