document.addEventListener("DOMContentLoaded", function () {
    fetch('/sms/api/user/checkSession',
        {
            method: 'GET',
            credentials: 'include'
        }
    )
    .then(response => {
        if (!response.ok) {
            window.location.href = "/sms/Pages/index.html";
        }
    })
    .catch(error => {
        console.error('Error checking session:', error);
    });

    initializeSendSMS();
    loadSMShistory();
    setupSearchSMS();
});



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
            clearMessage();
            sendSMS(from, to, message);
        });
    }
}

function showMessage(message, type) {
    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message');
    messageDiv.style.padding = '10px';
    messageDiv.style.marginTop = '10px';
    messageDiv.style.borderRadius = '4px';
    messageDiv.style.textAlign = 'center';

    if (type === 'success') {
        messageDiv.style.backgroundColor = '#d4edda';
        messageDiv.style.color = '#155724';
        messageDiv.style.border = '1px solid #c3e6cb';
    } else {
        messageDiv.style.backgroundColor = '#f8d7da';
        messageDiv.style.color = '#721c24';
        messageDiv.style.border = '1px solid #f5c6cb';
    }   
    
    messageDiv.textContent = message;

    const form = document.getElementById('send-sms-form');
    form.parentNode.insertBefore(messageDiv, form.nextSibling);
}

function clearMessage(){
    const messageDiv = document.querySelector('.message');
    if (messageDiv) {
        messageDiv.remove();
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
        credentials: 'include',
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
