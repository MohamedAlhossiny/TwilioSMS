document.addEventListener("DOMContentLoaded", function () {
    checkUserRole("customer");

    initializeSendSMS();
    loadSMShistory();
    setupSearchSMS();
});

function checkUserRole(expectedRole) {
    const role = localStorage.getItem("userRole");
    if (role !== expectedRole) {
        window.location.href = "index.html"; 
    }
}

function initializeSendSMS() {
    const sendSMSForm = document.getElementById("send-sms-form");

    if (sendSMSForm) {
        sendSMSForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const from = document.getElementById("from").value.trim();
            const to = document.getElementById("to").value.trim();
            const message = document.getElementById("message").value.trim();

            if (from === "" || to === "" || message === "") {
                alert("All fields are required!");
                return;
            }

            sendSMS(from, to, message);
        });
    }
}

function sendSMS(from, to, message) {
    const loadingMessage = document.getElementById("loading-message");
    loadingMessage.style.display = "block"; 

    fetch('/api/send-sms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ from, to, message })
    })
    .then(response => response.json())
    .then(data => {
        loadingMessage.style.display = "none"; 
        if (data.status === "success") {
            alert("SMS sent successfully!");
            loadSMShistory(); 
        } else {
            alert("Failed to send SMS. Please try again.");
        }
    })
    .catch(error => {
        loadingMessage.style.display = "none"; 
        alert("Error sending SMS. Please try again later.");
        console.error("Error:", error);
    });
}

function loadSMShistory() {
    fetch('/api/get-sms-history', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        const historyContainer = document.getElementById("sms-history");
        historyContainer.innerHTML = ""; 

        if (data && data.history && data.history.length > 0) {
            data.history.forEach(sms => {
                const smsItem = document.createElement("div");
                smsItem.classList.add("sms-item");
                smsItem.innerHTML = `
                    <p><strong>To:</strong> ${sms.to} <br><strong>Message:</strong> ${sms.message} <br><strong>Date:</strong> ${sms.date}</p>
                    <button onclick="deleteSMS(${sms.id})">Delete</button>
                `;
                historyContainer.appendChild(smsItem);
            });
        } else {
            historyContainer.innerHTML = "<p>No SMS history available.</p>";
        }
    })
    .catch(error => {
        console.error("Error loading SMS history:", error);
    });
}

function deleteSMS(smsId) {
    const confirmation = confirm("Are you sure you want to delete this SMS?");
    if (!confirmation) return;

    fetch(`/api/delete-sms/${smsId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === "success") {
            alert("SMS deleted successfully!");
            loadSMShistory();
        } else {
            alert("Failed to delete SMS. Please try again.");
        }
    })
    .catch(error => {
        console.error("Error deleting SMS:", error);
    });
}

function setupSearchSMS() {
    const searchInput = document.getElementById("sms-search");

    if (searchInput) {
        searchInput.addEventListener("input", function () {
            const searchTerm = searchInput.value.trim();
            searchSMS(searchTerm);
        });
    }
}

function searchSMS(searchTerm) {
    fetch(`/api/search-sms?term=${searchTerm}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        const historyContainer = document.getElementById("sms-history");
        historyContainer.innerHTML = ""; 

        if (data && data.history && data.history.length > 0) {
            data.history.forEach(sms => {
                const smsItem = document.createElement("div");
                smsItem.classList.add("sms-item");
                smsItem.innerHTML = `
                    <p><strong>To:</strong> ${sms.to} <br><strong>Message:</strong> ${sms.message} <br><strong>Date:</strong> ${sms.date}</p>
                    <button onclick="deleteSMS(${sms.id})">Delete</button>
                `;
                historyContainer.appendChild(smsItem);
            });
        } else {
            historyContainer.innerHTML = "<p>No results found for your search.</p>";
        }
    })
    .catch(error => {
        console.error("Error searching SMS:", error);
    });
}
