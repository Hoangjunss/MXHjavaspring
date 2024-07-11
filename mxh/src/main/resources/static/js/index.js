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
            console.log('Friend requests count:', data.countFriend);
            return data.countFriend;
        })
        .catch(error => {
            console.error('Error fetching friend requests count:', error);
            return 0; // Return 0 in case of error
        });
}
