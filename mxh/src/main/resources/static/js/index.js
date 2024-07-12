document.addEventListener("DOMContentLoaded", function () {
    // Fetch and display status options
    fetch('/status', {
        method: 'GET'
    })
    .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
    .then(data => {
        console.log(data);
        displayStatus(data);
    })
    .catch(error => console.error('Error fetching status post:', error));

    // Fetch and display friend request count
    fetchFriendRequestsCount().then(count => {
        document.getElementById('friend-request-count').innerText = `Lời mời kết bạn (${count})`;
    });
});

function displayStatus(data) {
    const statusSelect = document.getElementById('StatusId');
    data.status.forEach(stt => {
        const option = document.createElement('option');
        option.value = stt.id;
        option.textContent = stt.name;
        statusSelect.appendChild(option);
    });
}

// Đếm số lượng lời mời kết bạn chưa xác nhận
function fetchFriendRequestsCount() {
    return fetch('/countfriend')
        .then(response => response.json())
        .then(data => {      
            // Tạo một biến để lưu số lượng lời mời kết bạn
            let friendRequestsCount = 0;
            
            data.relationships.forEach(relationship => {
                if (relationship.userTwo.id === data.loggedInUser.id) {
                    console.log('Friend requests count:', data.countFriend);
                    friendRequestsCount = data.countFriend;
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
