document.addEventListener("DOMContentLoaded", function () {
    const verifyButton = document.getElementById("verify-btn");
    const verificationInput = document.getElementById("verification-code");
    const errorMessage = document.getElementById("error-message");

    const staticCode = "180181";

    verifyButton.addEventListener("click", function () {
        const enteredCode = verificationInput.value.trim();

        if (enteredCode === staticCode) {
            window.location.href = "../Pages/index.html"; 
        } else {
            errorMessage.textContent = "Invalid code. Please try again.";
        }
    });
});
