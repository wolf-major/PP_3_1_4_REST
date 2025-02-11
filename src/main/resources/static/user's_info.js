function showUserInfo() {
    fetch('/admin/info', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('Начинаем получать данные');
            document.getElementById('titleEmailCurrent').innerText = data.email;
            document.getElementById('titleRolesCurrent').innerText = data.roles.join(', ');

            document.getElementById('firstNameCurrent').innerText = data.firstName;
            document.getElementById('lastNameCurrent').innerText = data.lastName;
            document.getElementById('emailCurrent').innerText = data.email;
            document.getElementById('phoneNumberCurrent').innerText = data.phoneNumber;
            document.getElementById('ageCurrent').innerText = data.age;
            document.getElementById('rolesCurrent').innerText = data.roles.join(', ');

            const userRoles = data.roles;

            function hasRoles(role) {
                return userRoles.includes(role);
            }

            const adminTabContainer = document.getElementById('admin-tab-container');
            const userTabContainer = document.getElementById('user-tab-container');

            if (hasRoles('ADMIN')) {
                adminTabContainer.style.display = 'block';
            } else {
                adminTabContainer.style.display = 'none';
            }

            if (hasRoles('USER')) {
                userTabContainer.style.display = 'block';
            } else {
                userTabContainer.style.display = 'none';
            }
        })
        .catch(error => console.error('Error! Error! Error!'));
}