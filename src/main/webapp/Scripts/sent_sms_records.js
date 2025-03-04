let sentSmsRecords = [
    { smsId: 1, customerName: 'yasmen', phone: '+1234567890', message: 'Hello, this is a test SMS!', dateSent: '2025-02-17 10:00' },
    { smsId: 2, customerName: 'mahmoud', phone: '+0987654321', message: 'Your subscription has been updated.', dateSent: '2025-02-17 11:30' },
];

function loadSmsRecords() {
    const smsRecordsList = document.getElementById('sms-records-list');
    smsRecordsList.innerHTML = ''; 

    sentSmsRecords.forEach(record => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${record.smsId}</td>
            <td>${record.customerName}</td>
            <td>${record.phone}</td>
            <td>${record.message}</td>
            <td>${record.dateSent}</td>
        `;
        smsRecordsList.appendChild(row);
    });
}

window.onload = loadSmsRecords;

function logout() {
    window.location.href = '../Pages/index.html';
}

