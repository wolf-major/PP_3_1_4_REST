document.addEventListener('DOMContentLoaded', function () {
    const passwordField = document.getElementById('password');
    const confirmPasswordField = document.getElementById('passwordConfirm');
    const passwordError = document.getElementById('passwordError');
    const form = document.getElementById('add-user');

    function validatePasswords() {
        if (passwordField.value !== confirmPasswordField.value) {
            passwordError.style.display = 'block';
            return false;
        } else {
            passwordError.style.display = 'none';
            return true;
        }
    }
    confirmPasswordField.addEventListener('input', validatePasswords);

    form.addEventListener('submit', function (event) {
        if (!validatePasswords()) {
            event.preventDefault();
        }
    });
});