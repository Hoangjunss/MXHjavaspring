document.addEventListener("DOMContentLoaded", function() {
    const searchButton = document.getElementById("searchButton");
    const searchInput = document.getElementById("searchInput");
    const resultContainer = document.getElementById("resultContainer");

    searchButton.addEventListener("click", function(event) {
        event.preventDefault();

        const name = searchInput.value.trim();

        fetch('/searchmessage', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(name)
        })
        .then(response => response.json())
        .then(messages => {
            resultContainer.innerHTML = "";
            if (messages.length === 0) {
                resultContainer.textContent = "No messages found.";
            } else {
                messages.forEach(message => {
                    const messageElement = document.createElement("div");
                    messageElement.textContent = message.content;
                    resultContainer.appendChild(messageElement);
                });
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
    });
});