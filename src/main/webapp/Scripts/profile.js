document.addEventListener("DOMContentLoaded", function () {
    const profileForm = document.getElementById("profile-form");

    function loadProfileData() {
        const userData = JSON.parse(localStorage.getItem("userProfile")) || {
            name: "",
            dob: "",
            email: "",
            phone: "",
            address: "",
            twilioSid: "",
            twilioToken: "",
            twilioSender: ""
        };

        document.getElementById("name").value = userData.name;
        document.getElementById("dob").value = userData.dob;
        document.getElementById("email").value = userData.email;
        document.getElementById("phone").value = userData.phone;
        document.getElementById("address").value = userData.address;
        document.getElementById("twilio-sid").value = userData.twilioSid;
        document.getElementById("twilio-token").value = userData.twilioToken;
        document.getElementById("twilio-sender").value = userData.twilioSender;
    }

    loadProfileData();

    profileForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const updatedProfile = {
            name: document.getElementById("name").value.trim(),
            dob: document.getElementById("dob").value,
            email: document.getElementById("email").value.trim(),
            phone: document.getElementById("phone").value.trim(),
            address: document.getElementById("address").value.trim(),
            twilioSid: document.getElementById("twilio-sid").value.trim(),
            twilioToken: document.getElementById("twilio-token").value.trim(),
            twilioSender: document.getElementById("twilio-sender").value.trim()
        };

        if (!updatedProfile.name || !updatedProfile.email || !updatedProfile.phone) {
            alert("Please fill in all required fields.");
            return;
        }

        localStorage.setItem("userProfile", JSON.stringify(updatedProfile));
        alert("Profile updated successfully!");
    });
});
