let statistics = [
    { customerId: 1, name: 'yasmen', smsSent: 150, smsReceived: 120 },
    { customerId: 2, name: 'mahmoud', smsSent: 100, smsReceived: 95 },
];

function loadStatistics() {
    const statisticsList = document.getElementById('statistics-list');
    statisticsList.innerHTML = ''; 

    statistics.forEach(stat => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${stat.customerId}</td>
            <td>${stat.name}</td>
            <td>${stat.smsSent}</td>
            <td>${stat.smsReceived}</td>
        `;
        statisticsList.appendChild(row);
    });
}

window.onload = loadStatistics;

function logout() {
    window.location.href = '../Pages/index.html';
}
