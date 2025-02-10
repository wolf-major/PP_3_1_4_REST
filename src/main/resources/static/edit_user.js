const modal = document.getElementById('actionsModal');
const modalForm = document.getElementById('modalWindowAction');
const submitButton = document.getElementById('submitButton');
const removeButton = document.getElementById('removeButton');
const editForm = modalForm.querySelector('form');

async function fillModalWindow(userId) {
    try {
        const response = await fetch(`/admin/users/${userId}`);
        if (!response.ok) {
            throw new Error(`Ошибка получения данных`);
        }
        const userData = await response.json();
        console.log(userData.firstName);
        console.log(userData.email);

        modalForm.querySelector('input[name="id"]').value = userData.id;
        modalForm.querySelector('#editFirstName').value = userData.firstName;
        modalForm.querySelector('#editLastName').value = userData.lastName;
        modalForm.querySelector('#editAge').value = userData.age;
        modalForm.querySelector('#editPhoneNumber').value = userData.phoneNumber;
        modalForm.querySelector('#editEmail').value = userData.email;

        const rolesSelect = modalForm.querySelector('#editRolesSelect');
        const allRoles = ['USER', 'ADMIN'];

        rolesSelect.innerHTML = '';

        allRoles.forEach(roleName => {
            const option = document.createElement('option');
            option.value = roleName;
            option.text = roleName;
            option.selected = userData.roles.includes(roleName);
            rolesSelect.appendChild(option);
        });

        const modalInstance = new bootstrap.Modal(modal);
        modalInstance.show();
    } catch (error) {
        console.error('Ошибка при получении данных:', error);
        alert('Не удалось загрузить данные пользователя.');
    }
}

async function sendDataToServer(data) {
    try {
        const response = await fetch('/admin/save_edit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }

        const result = await response.json();
        console.log('Сохранено:', result);
        alert('Данные успешно сохранены!');
    } catch (error) {
        console.error('Ошибка при сохранении данных:', error);
        alert('Не удалось сохранить данные.');
    }
}

function setupEditModal(userId) {
    fillModalWindow(userId);
    modal.querySelector('.modal-title').textContent = 'Edit user';
    submitButton.textContent = 'SUBMIT';
    submitButton.style.display = 'inline-block';
    removeButton.style.display = 'none';
    toggleFields(true);

    submitButton.removeEventListener('click', handleSave);

    submitButton.addEventListener('click', handleSave);

    async function handleSave(event) {
        event.preventDefault();

        const formData = new FormData(modalForm);
        const data = Object.fromEntries(formData.entries());
        const roles = document.querySelectorAll("#editRolesSelect");

        data.roles = Array.from(roles.selectedOptions)
                        .map(option => option.value);

        await sendDataToServer(data);
    }
}

async function editUser(userId) {
    await setupEditModal(userId);
}


async function deleteUser(userId) {
    await fillModalWindow(userId);
    modal.querySelector('.modal-title').textContent = 'Delete user';
    submitButton.textContent = 'DELETE';
    submitButton.style.display = 'none';
    removeButton.style.display = 'inline-block';
    toggleFields(false);
}

function toggleFields(editable) {
    const fields = modalForm.querySelectorAll('input, select');
    fields.forEach(field => {
        if (editable) {
            field.removeAttribute('readonly');
        } else {
            field.setAttribute('readonly', true);
        }
    })
}