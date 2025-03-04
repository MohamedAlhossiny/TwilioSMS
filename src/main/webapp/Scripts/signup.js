document.addEventListener("DOMContentLoaded", function () {

    fetch('/sms/api/user/checkSession')
        .then(response => {
            if (response.ok) {
                window.location.href = '/sms/Pages/customer.html';
            }
        })
        .catch(error => {
            console.error('Error checking session:', error);
        });

    const form = document.getElementById("signup-form");

    if (form) {
        form.addEventListener("submit", function (event) {
            event.preventDefault();

            const full_name = document.getElementById("name").value.trim();
            const username = document.getElementById("username").value.trim();
            const birth_date = new Date(document.getElementById("birthday").value.trim())
                .toISOString()  // Converts to format: 2000-09-05T21:00:00.000Z
                .replace('.000', '') + '[UTC]';  // Removes milliseconds and adds [UTC]
            const job = document.getElementById("job").value.trim();
            const email = document.getElementById("email").value.trim();
            const phone_number = document.getElementById("phone").value.trim();
            const address = document.getElementById("address").value.trim();
            const sid = document.getElementById("twilio-sid").value.trim();
            const token = document.getElementById("twilio-token").value.trim();
            const sender_id = document.getElementById("twilio-sender").value.trim();
            const passwd = document.getElementById("password").value.trim();
            const confirmPassword = document.getElementById("confirm-password").value.trim();

            clearErrors();

            if (!full_name) return showError("name", "Full name is required");
            if (!username) return showError("username", "Username is required");
            if (!birth_date) return showError("birthday", "Birthday is required");
            if (!job) return showError("job", "Job is required");
            if (!email) return showError("email", "Email is required");
            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) return showError("email", "Enter a valid email");
            if (!phone_number) return showError("phone", "Phone number is required");
            if (!/^\+\d{10,15}$/.test(phone_number)) return showError("phone", "Enter a valid phone number starting with + followed by 10-15 digits");
            if (!address) return showError("address", "Address is required");
            if (!sid) return showError("twilio-sid", "Twilio Account SID is required");
            if (!token) return showError("twilio-token", "Twilio Token is required");
            if (!sender_id) return showError("twilio-sender", "Twilio Sender ID is required");
            if (!/^\+\d{10,15}$/.test(sender_id)) return showError("twilio-sender", "Enter a valid sender id starting with + followed by 10-15 digits");
            if (!passwd) return showError("password", "Password is required");
            if (passwd.length < 6) return showError("password", "Password must be at least 6 characters");
            if (!confirmPassword) return showError("confirm-password", "Confirm password is required");
            if (passwd !== confirmPassword) return showError("confirm-password", "Passwords do not match");

            // let users = JSON.parse(localStorage.getItem("users")) || {};
            // if (users[email.toLowerCase()]) {
            //     showError("email", "Email already registered");
            //     return;
            // }
            const userInfo = {
                username,
                full_name,
                birth_date,
                job,
                email,
                address,
                passwd
            };
            const twilioInfo = {
                sid,
                token,
                sender_id,
                phone_number
            };
            registerAndLoginUser(userInfo, twilioInfo);
        
        }
    );
    }

    function showError(fieldId, message) {
        const inputField = document.getElementById(fieldId);
        if (!inputField.parentNode.querySelector(".error-message")) {
            const error = document.createElement("small");
            error.classList.add("error-message");
            error.style.color = "red";
            error.innerText = message;
            inputField.parentNode.appendChild(error);
        }
    }

    function clearErrors() {
        document.querySelectorAll(".error-message").forEach(error => error.remove());
    }
});



async function registerAndLoginUser(userInfo, twilioInfo) {
    try {
        var user_id;
        // Step 1: Register user info
        let registerResponse = await fetch('/sms/api/user', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userInfo)
        });

        if (!registerResponse.ok) {
            showMessage("UserInfo registration failed", "error");
            throw new Error('User registration failed');
        }

        console.log('User registered successfully');

        // Step 2: Login with user info
        let loginResponse = await fetch('/sms/api/user/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: userInfo.username,
                passwd: userInfo.passwd
            }),
            credentials: 'include'
        });

        if (!loginResponse.ok) {
            showMessage("Login failed", "error");
            throw new Error('Login failed');
        }

        const loginData = await loginResponse.json();
        user_id = loginData.id;

        console.log('User logged in successfully');
        twilioInfo.user_id = user_id;
        
        // Step 3: Register Twilio info
        let twilioResponse = await fetch('/sms/api/twilio', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(twilioInfo)
        });

        if (!twilioResponse.ok) {
            showMessage("Error registering twilio info. Please try again later.", "error");
            throw new Error('Twilio info registration failed');
        }

        console.log('Twilio info registered successfully');
        showMessage("Registration successful! Redirecting to profile page...", "success");
        
        // Redirect after 2 seconds
        setTimeout(() => {
            window.location.href = '/sms/Pages/customer.html';
        }, 2000);

    } catch (error) {
        console.error('Error:', error.message);
        showMessage(error.message, "error");
    }
}

function showMessage(message, type) {
    // Remove any existing message
    const existingMessage = document.querySelector('.message');
    if (existingMessage) {
        existingMessage.remove();
    }

    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message');
    messageDiv.style.padding = '10px';
    messageDiv.style.marginTop = '10px';
    messageDiv.style.borderRadius = '4px';
    messageDiv.style.textAlign = 'center';
    
    if (type === "success") {
        messageDiv.style.backgroundColor = '#d4edda';
        messageDiv.style.color = '#155724';
        messageDiv.style.border = '1px solid #c3e6cb';
    } else {
        messageDiv.style.backgroundColor = '#f8d7da';
        messageDiv.style.color = '#721c24';
        messageDiv.style.border = '1px solid #f5c6cb';
    }
    
    messageDiv.textContent = message;
    
    // Insert after the form
    const form = document.getElementById('signup-form');
    form.parentNode.insertBefore(messageDiv, form.nextSibling);
}


