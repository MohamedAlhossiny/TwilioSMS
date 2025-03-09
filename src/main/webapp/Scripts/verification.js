document.addEventListener("DOMContentLoaded", function () {
    const verifyButton = document.getElementById("verify-btn");
    const verificationInput = document.getElementById("verification-code");
    const errorMessage = document.getElementById("error-message");


    fetch('/sms/api/verificationCode/send', {
        method: 'POST',
        credentials: 'include'
        
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Failed to send verification code');
        }
    })
    .catch(error => {
        console.error('Error sending verification code:', error);
    });
    

    verifyButton.addEventListener("click", function () {
        const enteredCode = verificationInput.value.trim();
        fetch('/sms/api/verificationCode/verify', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ verification_code: enteredCode })
        })
        .then(response => { 
            if (response.ok) {
                window.location.href = "../Pages/customer.html"; 
            } else {
                errorMessage.textContent = "Invalid code. Please try again.";
            }
        })
        .catch(error => {
            console.error('Error verifying verification code:', error);
        });

    });
});

