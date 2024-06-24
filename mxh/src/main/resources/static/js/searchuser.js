document.addEventListener("DOMContentLoaded", function() {
    const searchButton = document.getElementById("searchButton");
    const searchInput = document.getElementById("searchInput");
    const resultContainer = document.getElementById("resultContainer");

    searchButton.addEventListener("click", function(event) {
        event.preventDefault();

        const name = searchInput.value;

        fetch('/searchuser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(name)
        })
        .then(response => response.json())
        .then(users => {
            resultContainer.innerHTML = "";
            users.forEach(user => {
                const userElement = document.createElement("div");
                userElement.textContent = `${user.firstName}  ${user.lastName}`;
                resultContainer.appendChild(userElement);
            });
        })
        .catch(error => {
            console.error("Error:", error);
        });
    });
});