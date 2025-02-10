const modal = document.getElementById('actionsModal');
const modalForm = document.getElementById('modalWindowAction');

async function editUser(userId) {
    try {
        const response = await fetch(`/admin/users/${userId}`);
        if (!response.ok) {
            throw new Error(`Ошибка получения данных`);
        }
        const userData = await response.json();
        console.log(userData.firstName);
        console.log(userData.email);

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