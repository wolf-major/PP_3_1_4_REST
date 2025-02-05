document.addEventListener("DOMContentLoaded", function () {
    const error = document.querySelector(".alert-danger");
    if (error) {
        const name = modalData;
        const modal = new bootstrap.Modal(document.getElementById('actionsModal'));
        modal.show();
    }
});