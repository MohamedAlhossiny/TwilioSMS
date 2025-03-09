let currentUserId = null;
let currentTwilioId = null;
let currentUsername = null;
document.addEventListener("DOMContentLoaded", function () {
    const profileForm = document.getElementById("profile-form");

    function loadProfileData() {
        let user = null;
        let twilio = null;
        clearMessage();
        fetch('/sms/api/user/checkSession',
            {
                method: 'GET',
                credentials: 'include'
            }
        )
        .then(async response => {
            if (!response.ok) {
                window.location.href = "/sms/Pages/index.html";
            }else{
                user = await response.json();
                currentUserId = user.id;
                console.log(user);
            }
        })
        .catch(error => {
            console.error('Error checking session:', error);
        });

        fetch('/sms/api/twilio/'+user.id,
            {
                method: 'GET',
                credentials: 'include'
            }
        )
        .then(async response => {
            if (!response.ok) {
                window.location.href = "/sms/Pages/index.html";
            }else{
                twilio = await response.json();
                currentTwilioId = twilio.id;
                console.log(twilio);
            }
        })
        .catch(error => {
            console.error('Error checking session:', error);
        });
//        console.log(user);
        console.log(twilio);

        
    }

    loadProfileData();

    profileForm.addEventListener("submit", function (event) {
        event.preventDefault();
        clearMessage();
        let date = new Date(document.getElementById("dob").value);
        const updatedUser = {
            full_name: document.getElementById("name").value.trim(),
            birth_date: date.toISOString()  // Converts to format: 2000-09-05T21:00:00.000Z
            .replace('.000', '') + '[UTC]',  // Removes milliseconds and adds [UTC],
            email: document.getElementById("email").value.trim(),
            address: document.getElementById("address").value.trim(),
            username: currentUsername
        };

        const updatedTwilio = {
            phone_number: document.getElementById("phone").value.trim(),
            sid: document.getElementById("twilio-sid").value.trim(),
            token: document.getElementById("twilio-token").value.trim(),
            sender_id: document.getElementById("twilio-sender").value.trim(),
            user_id: currentUserId
        };

        if (!updatedUser.full_name || !updatedUser.email || !updatedUser.address) {
            showMessage("Please fill in all required fields.", "error");
            return;
        }
        if(updatedTwilio.phone_number == null || updatedTwilio.sid == null || updatedTwilio.token == null || updatedTwilio.sender_id == null){
            showMessage("Please fill in all required fields.", "error");
            return;
        }
        
        fetch('/sms/api/user/' + currentUserId,
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedUser),
                credentials: 'include'
            }
        )
        .then(async response => {
            if (!response.ok) {
                showMessage("Error updating user.", "error");
                return;
            }
        })
        .catch(error => {
            console.error('Error updating user:', error);
            return;
        });
        
        fetch('/sms/api/twilio/',
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedTwilio),
                credentials: 'include'
            }
        )
        .then(async response => {
            if (!response.ok) {
                showMessage("Error updating twilio.", "error");
                return;
            }
        })
        .catch(error => {
            console.error('Error updating twilio:', error);
            return;
        });
        showMessage("Profile updated successfully!", "success");
    });
});


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

    const form = document.getElementById('profile-form');
    form.parentNode.insertBefore(messageDiv, form.nextSibling); 

}   

function clearMessage(){
    const messageDiv = document.querySelector('.message');
    if (messageDiv) {
        messageDiv.remove();
    }
}

