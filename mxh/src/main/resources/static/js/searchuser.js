document.addEventListener("DOMContentLoaded", function() {
    const searchButton = document.getElementById("search-btn");
    const searchInput = document.getElementById("search-input");
    const resultContainer = document.getElementById("search-user");

    searchButton.addEventListener("click", function(event) {
        event.preventDefault();

        const name = searchInput.value;

        fetch('/api/search?name='+name, {
            method: 'GET',
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
            data.users.forEach(user => {
                resultContainer.innerHTML = "";
                        fetchMutualFriendsCount(user.id).then(count => { 
                            const listItem = createFriendItemSearchUser(user, 'acceptFriendRequest', 'deleteFriend', true, count);
                            resultContainer.appendChild(listItem);
                        });
                NProgress.done();
            });
        })
        .catch(error => {
            console.error('Error fetching chat messages:', error.message);
            // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
        });
    });
});

function createFriendItemSearchUser(friend, confirmCallback, deleteCallback, isFriend, mutualFriendsCount) {
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

    friendInfo.appendChild(name);
    friendInfo.appendChild(mutualFriends);

    friendCard.appendChild(friendInfo);
    listItem.appendChild(friendCard);

    return listItem;
}