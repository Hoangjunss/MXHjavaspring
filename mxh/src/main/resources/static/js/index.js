document.addEventListener("DOMContentLoaded", function () {
    // Fetch and display status options
    fetch('/api/status', {
        method: 'GET'
    })
    .then(response => {
        if (!response.ok) {
            // Nếu response không OK, ném lỗi với message từ server
            return response.json().then(errorData => {
                throw new Error(errorData.message || 'An unexpected error occurred');
            });
        }
        return response.json();
    }) // Chuyển đổi phản hồi thành JSON
    .then(data => {
        displayStatus(data);
    })
    .catch(error => {
        console.error('Error fetching chat messages:', error.message);
        // Hiển thị lỗi cho người dùng, ví dụ như bằng cách cập nhật UI
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
