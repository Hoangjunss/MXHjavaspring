// Hàm gửi yêu cầu Fetch để chấp nhận lời mời kết bạn
async function acceptFriendRequest(userId) {
    try {
        const response = await fetch('/acceptFriendRequest', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ 
                userId: userId 
            })
        });

        if (!response.ok) {
            throw new Error('Đã xảy ra lỗi khi chấp nhận lời mời kết bạn: ' + response.statusText);
        }

        const data = await response.json();
        console.log('Lời mời kết bạn đã được chấp nhận.');
        // location.reload(); // Tải lại trang sau khi chấp nhận
    } catch (error) {
        console.error(error.message);
    }
}

// Hàm gửi yêu cầu Fetch để xóa bạn bè
async function deleteFriend(userId) {
    try {
        const response = await fetch('/deleteFriend', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ userId: userId })
        });

        if (!response.ok) {
            throw new Error('Đã xảy ra lỗi khi xóa bạn bè: ' + response.statusText);
        }

        const data = await response.json();
        console.log('Bạn bè đã được xóa.');
        location.reload(); // Tải lại trang sau khi xóa
    } catch (error) {
        console.error(error.message);
    }
}

// Hàm gửi yêu cầu Fetch để thêm bạn bè mới
async function addFriend(userId) {
    try {
        const response = await fetch('/addFriend', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ userId: userId })
        });

        if (!response.ok) {
            throw new Error('Đã xảy ra lỗi khi gửi lời mời kết bạn: ' + response.statusText);
        }

        const data = await response.json();
        console.log('Đã gửi lời mời kết bạn.');
        location.reload(); // Tải lại trang sau khi gửi lời mời
    } catch (error) {
        console.error(error.message);
    }
}
