async function loadStatistics() {
    const statisticsList = document.getElementById('statistics-list');
    statisticsList.innerHTML = ''; 

    try {
        // Await both fetch calls
        const usersResponse = await fetch('/sms/api/user', {
            method: 'GET',
            credentials: 'include'
        });
        if (!usersResponse.ok) {
            throw new Error('Failed to fetch users');
        }
        const users = await usersResponse.json();

        const twiliosResponse = await fetch('/sms/api/twilio/accounts', {
            method: 'GET',
            credentials: 'include'
        });
        if (!twiliosResponse.ok) {
            throw new Error('Failed to fetch Twilio accounts');
        }
        const twilios = await twiliosResponse.json();

        // Process the data
        users.forEach(user => { 
            twilios.forEach(twilio => {
                if (twilio.user_id === user.id) {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${user.id}</td>
                        <td>${user.full_name}</td>
                        <td>${twilio.number_of_msg}</td>
                    `;
                    statisticsList.appendChild(row);
                }
            });
        });
    } catch (error) {
        console.error('Error loading statistics:', error);
    }
}

// Call the function to load statistics
document.addEventListener('DOMContentLoaded', loadStatistics);

function logout() {
    fetch('/sms/api/user/logout', {
        method: 'POST',
        credentials: 'include'
    }).then(() => {
        window.location.href = '../Pages/index.html';
    });
}
