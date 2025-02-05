const modalManager = {
    data:null,
    saveData(button) {
        this.data = {
            id: button.getAttribute("data-id"),
            firstName : button.getAttribute("data-first-name"),
            lastName: button.getAttribute("data-last-name"),
            email : button.getAttribute("data-email"),
            phoneNumber : button.getAttribute("data-phone-number"),
            age : button.getAttribute("data-age"),
            roles : button.getAttribute("data-roles"),
        };
    },
    getData() {
        return this.data;
    }
};

document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById('actionsModal');
    const editForm = modal.querySelector('form');

    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const action = button.getAttribute('data-bs-whatever');

        if (action === 'edit-button') {
            modalManager.saveData(button);

            const data = modalManager.getData();

            editForm.querySelector('input[name="id"]').value = data.id;
            editForm.querySelector('#editFirstName').value = data.firstName;
            editForm.querySelector('#editLastName').value = data.lastName;
            editForm.querySelector('#editEmail').value = data.email;
            editForm.querySelector('#editPhoneNumber').value = data.phoneNumber;
            editForm.querySelector('#editAge').value = data.age;

            const rolesSelect = editForm.querySelector('#editRolesSelect');

            Array.from(rolesSelect.options).forEach(option => {
                option.selected = data.roles.split(",").map(Number).includes(Number(option.value));
            });

            modal.querySelector('.modal-title').textContent = 'Edit user';
        } else if (action === 'delete-button') {
            modal.querySelector('.modal-title').textContent = 'Delete user';
        }
    });
});

