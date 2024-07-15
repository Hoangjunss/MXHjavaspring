document.addEventListener("DOMContentLoaded", function () {
    // Fetch and display status options
    fetch('/api/status', {
        method: 'GET'
    })
    .then(response => response.json()) // Chuyển đổi phản hồi thành JSON
    .then(data => {
        console.log(data);
        displayStatus(data);
    })
    .catch(error => console.error('Error fetching status post:', error));

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
