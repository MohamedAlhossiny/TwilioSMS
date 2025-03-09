

function loadSmsRecords() {
    const smsRecordsList = document.getElementById('sms-records-list');
    smsRecordsList.innerHTML = ''; 
    fetch('/sms/api/message', {
        method: 'GET',
        credentials: 'include'
    }).then(response => {
        if (!response.ok) {
            throw new Error('Failed to fetch SMS records');
        }
        return response.json();
    }).then(data => {
        data.forEach(record => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${record.id}</td>
                <td>${record.user_id}</td>
                <td>${record.from_num}</td>
                <td>${record.to_num}</td>
                <td>${record.body}</td>
                <td>${record.msg_date}</td>
        `;
            smsRecordsList.appendChild(row);
        });
    }).catch(error => {
        console.error('Error fetching SMS records:', error);
    });
}

window.onload = loadSmsRecords;

function logout() {
    fetch('/sms/api/user/logout', {
        method: 'POST',
        credentials: 'include'
    }).then(() => {
        window.location.href = '../Pages/index.html';
    });
}

