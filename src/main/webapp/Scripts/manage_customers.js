fetch('/sms/api/user/checkSession')
.then(response => {
    if (!response.ok) {
        window.location.href = '../Pages/index.html';
    }
})
.catch(error => {
    console.error('Error checking session:', error);
});

let customers = [];
function loadCustomers() {
    const customerList = document.getElementById('customer-list');
    customerList.innerHTML = '';
    fetch('/sms/api/user', {
        method: 'GET',
        credentials: 'include'
    }).then(response => {
        if (!response.ok) {
            throw new Error('Failed to fetch customers');
        }else{
            return response.json();
        }
    }).then(data => {
        customers = data;
        customers.forEach(customer => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${customer.id}</td>
                <td>${customer.full_name}</td>
                <td>${customer.birth_date.replace(/Z$/, '')}</td>
                <td>${customer.job}</td>
                <td>${customer.email}</td>
                <td>${customer.address}</td>
                <td>
                    <button class="edit" onclick="editCustomer(${customer.id})">Edit</button>
                    <button class="delete" onclick="deleteCustomer(${customer.id})">Delete</button>
                </td>
            `;
            customerList.appendChild(row);
        });
    }).catch(error => {
        console.error('Error fetching customers:', error);
    });
}

function editCustomer(id) {
    const customer = customers.find(c => c.id === id);
    if (customer) {
        document.getElementById('editId').value = customer.id;
        document.getElementById('editName').value = customer.full_name;
        document.getElementById('editBirthday').value = customer.birth_date.replace(/Z$/, '');
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
    const job = document.getElementById('editJob').value.trim();
    const email = document.getElementById('editEmail').value.trim();
    const address = document.getElementById('editAddress').value.trim();
    const birthDate = new Date(birthday)
                .toISOString()  // Converts to format: 2000-09-05T21:00:00.000Z
                .replace('.000', '') + '[UTC]';  // Removes milliseconds and adds [UTC]
    console.log(id, name, birthday, job, email, address);
    let user = {
        id: id,
        full_name: name,
        birth_date: birthDate,
        job: job,
        email: email,
        address: address
    }

    fetch(`/sms/api/user/${id}`, {
        method: 'PUT',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    }).then(response => {
        if (!response.ok) {
            throw new Error('Failed to update customer');
        }
        return response.json();
    }).then(data => {
        customers = customers.map(c => c.id === id ? data : c); 
        loadCustomers();
        closeModal();
    }).catch(error => {
        console.error('Error updating customer:', error);
    });
}


let deleteId = null;

function deleteCustomer(id) {
    deleteId = id; 
    document.getElementById('deleteModal').classList.add('active'); 
}

document.getElementById('confirmDelete').addEventListener('click', function () {
    if (deleteId !== null) {
        customers = customers.filter(c => c.id !== deleteId);
        fetch(`/sms/api/user/${deleteId}`, {
            method: 'DELETE',
            credentials: 'include'
        }).then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete customer');   
            }
            loadCustomers();
            closeDeleteModal();
        }).catch(error => {
            console.error('Error deleting customer:', error);
        });
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
    fetch('/sms/api/user/logout', {
        method: 'POST',
        credentials: 'include'
    }).then(() => {
        window.location.href = '../Pages/index.html';
    });
}

window.onload = loadCustomers;

document.getElementById('editModal').addEventListener("click", function(event) {
    if (event.target === this) {
        closeModal();
    }
});
