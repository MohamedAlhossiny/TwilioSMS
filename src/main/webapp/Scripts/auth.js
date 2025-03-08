document.addEventListener("DOMContentLoaded", function () {

    fetch('/sms/api/user/checkSession')
    .then(async response => {
        if (response.ok) {
            let user = await response.json();
            if(user.role === "admin"){
                window.location.href = '/sms/Pages/manage_customers.html';
            }else{
                window.location.href = '/sms/Pages/customer.html';
            }
        }
    })
    .catch(error => {
        console.error('Error checking session:', error);
    });

    const loginForm = document.getElementById("login-form");

    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const username = document.getElementById("username").value.trim();
            const password = document.getElementById("password").value.trim();


            clearMessage();

            if (!username || !password) {
                showMessage("Please enter a valid username and password", "error");
                return;
            }
            preformLogin(username, password);

        });
    }

    async function preformLogin(username, password) {
        
        let loginResponse = await fetch('/sms/api/user/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: username,
                passwd: password
            }),
            credentials: 'include'
        }).then(response => {
            if (!response.ok) {
                showMessage("Login failed, please check your username and password", "error");
                throw new Error('Login failed');
            }else{
                return response.json();
            }
        }).catch(error => {
            console.error('Error checking session:', error);
        });
        if(loginResponse.role === "admin"){
            window.location.href = '/sms/Pages/manage_customers.html';
        }else{
            window.location.href = '/sms/Pages/customer.html';
        }
    }
});


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
    const form = document.getElementById('login-form');
    form.parentNode.insertBefore(messageDiv, form.nextSibling);
}

function clearMessage(){
    const messageDiv = document.querySelector('.message');
    if (messageDiv) {
        messageDiv.remove();
    }
}

