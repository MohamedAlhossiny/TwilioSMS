document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("login-form");

    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault();
            console.log("Login form submitted");

            const username = document.getElementById("username").value.trim();
            const password = document.getElementById("password").value.trim();

            console.log("Entered Username:", username);
            console.log("Entered Password:", password);

            clearErrors();

            const users = {
                admin: { password: "123456", role: "admin" },
                customer: { password: "customer123", role: "customer" }
            };

            if (!users[username.toLowerCase()]) {
                console.log("Username not found:", username);
                showError("username", "Invalid username or password");
                return;
            }

            if (users[username.toLowerCase()].password !== password) {
                console.log("Incorrect password for:", username);
                showError("password", "Invalid username or password");
                return;
            }

            let userRole = users[username.toLowerCase()].role;
            console.log("User Role:", userRole);

            localStorage.setItem("userRole", userRole);
            localStorage.setItem("username", username);

            const redirectPage = userRole === "admin" ? "manage_customers.html" : "customer.html";
            console.log("Redirecting to:", redirectPage);
            window.location.href = redirectPage;
        });
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
