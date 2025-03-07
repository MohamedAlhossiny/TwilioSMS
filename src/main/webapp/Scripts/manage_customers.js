fetch('/sms/api/user/checkSession')
.then(response => {
    if (!response.ok) {
        window.location.href = '../Pages/index.html';
    }
})
.catch(error => {
    console.error('Error checking session:', error);
});

let customers = [
    { id: 1, name: 'Yasmeen', birthday: '1999-01-01', phone: '+1234567890', job: 'Developer', email: 'yasmeen@example.com', address: 'Cairo, Egypt', smsSent: 15 },
    { id: 2, name: 'Mahmoud', birthday: '2000-05-20', phone: '+0987654321', job: 'Designer', email: 'mahmoud@example.com', address: 'Alexandria, Egypt', smsSent: 10 },
];

function loadCustomers() {
    const customerList = document.getElementById('customer-list');
    customerList.innerHTML = '';

    customers.forEach(customer => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${customer.id}</td>
            <td>${customer.name}</td>
            <td>${customer.birthday}</td>
            <td>${customer.phone}</td>
            <td>${customer.job}</td>
            <td>${customer.email}</td>
            <td>${customer.address}</td>
            <td>${customer.smsSent}</td>
            <td>
                <button class="edit" onclick="editCustomer(${customer.id})">Edit</button>
                <button class="delete" onclick="deleteCustomer(${customer.id})">Delete</button>
            </td>
        `;
        customerList.appendChild(row);
    });
}

function editCustomer(id) {
    const customer = customers.find(c => c.id === id);
    if (customer) {
        document.getElementById('editId').value = customer.id;
        document.getElementById('editName').value = customer.name;
        document.getElementById('editBirthday').value = customer.birthday;
        document.getElementById('editPhone').value = customer.phone;
        document.getElementById('editJob').value = customer.job;
        document.getElementById('editEmail').value = customer.email;
        document.getElementById('editAddress').value = customer.address;

        document.getElementById('editModal').classList.add('active');
    } else {
        console.error("Customer not found.");
    }
}

function saveCustomer() {
    const id = parseInt(document.getElementById('editId').value);
    const name = document.getElementById('editName').value.trim();
    const birthday = document.getElementById('editBirthday').value;
    const phone = document.getElementById('editPhone').value.trim();
    const job = document.getElementById('editJob').value.trim();
    const email = document.getElementById('editEmail').value.trim();
    const address = document.getElementById('editAddress').value.trim();

    let customer = customers.find(c => c.id === id);
    if (customer) {
        customer.name = name;
        customer.birthday = birthday;
        customer.phone = phone;
        customer.job = job;
        customer.email = email;
        customer.address = address;

        loadCustomers();
        closeModal();
    } else {
        console.error("Error: Customer not found for updating.");
    }
}


let deleteId = null;

function deleteCustomer(id) {
    deleteId = id; 
    document.getElementById('deleteModal').classList.add('active'); 
}

document.getElementById('confirmDelete').addEventListener('click', function () {
    if (deleteId !== null) {
        customers = customers.filter(c => c.id !== deleteId);
        loadCustomers();
        closeDeleteModal();
    }
});

function closeDeleteModal() {
    document.getElementById('deleteModal').classList.remove('active');
    deleteId = null; 
}


function closeModal() {
    document.getElementById('editModal').classList.remove('active');
}

function logout() {
    window.location.href = "../Pages/index.html";
}

window.onload = loadCustomers;

document.getElementById('editModal').addEventListener("click", function(event) {
    if (event.target === this) {
        closeModal();
    }
});
