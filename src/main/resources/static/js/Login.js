document.addEventListener('DOMContentLoaded', function () {
    const togglePasswordIcons = document.querySelectorAll('.ph-eye-slash, .ph-eye');
    togglePasswordIcons.forEach(icon => {
        icon.addEventListener('click', function () {
            const input = this.parentElement.querySelector('input');
            if (!input) return;
            const isPassword = input.type === 'password';
            input.type = isPassword ? 'text' : 'password';
            this.classList.toggle('ph-eye-slash', !isPassword);
            this.classList.toggle('ph-eye', isPassword);
        });
    });
});

