document.addEventListener('DOMContentLoaded', function () {
    const inputs = document.querySelectorAll('.required-input');
    const agreeCheckbox = document.getElementById('agree');
    const submitBtn = document.getElementById('submitBtn');
    const passwordInput = document.getElementById('passwordInput');
    const passwordRules = document.querySelectorAll('.password-rule');

    function validatePasswordRules() {
        if (!passwordInput) return true;

        const value = passwordInput.value;
        const rules = {
            length: value.length >= 12,
            symbol: /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?`~]/.test(value),
            number: /\d/.test(value),
            uppercase: /[A-Z]/.test(value),
            lowercase: /[a-z]/.test(value)
        };

        passwordRules.forEach(rule => {
            const ruleName = rule.dataset.rule;
            const passed = rules[ruleName];
            const dot = rule.querySelector('.rule-dot');

            rule.classList.remove('text-red-500', 'text-green-500', 'text-gray-600');
            if (passed) {
                rule.classList.add('text-green-500');
                dot?.classList.remove('border-red-500', 'border-gray-400');
                dot?.classList.add('border-green-500', 'bg-green-500');
            } else {
                rule.classList.add('text-red-500');
                dot?.classList.remove('border-green-500', 'border-gray-400', 'bg-green-500');
                dot?.classList.add('border-red-500', 'bg-red-500');
            }
        });

        return Object.values(rules).every(Boolean);
    }

    function checkFormValidity() {
        let allFilled = true;

        inputs.forEach(input => {
            if (input.value.trim() === '') {
                allFilled = false;
            }
        });

        const isAgreed = agreeCheckbox ? agreeCheckbox.checked : true;
        const passwordValid = validatePasswordRules();
        const isValid = allFilled && isAgreed && passwordValid;

        if (isValid) {
            submitBtn.disabled = false;
            submitBtn.classList.remove('bg-[#a3a3a3]', 'cursor-not-allowed');
            submitBtn.classList.add('bg-blue-600', 'hover:bg-blue-700', 'cursor-pointer');
        } else {
            submitBtn.disabled = true;
            submitBtn.classList.remove('bg-blue-600', 'hover:bg-blue-700', 'cursor-pointer');
            submitBtn.classList.add('bg-[#a3a3a3]', 'cursor-not-allowed');
        }
    }

    function validateInput(input) {
        const relativeWrapper = input.parentElement;
        const container = relativeWrapper.parentElement;
        let errorMsg = container.querySelector('.js-error-msg');

        if (input.value.trim() === '') {
            input.classList.add('border-red-500', 'focus:border-red-500', 'focus:ring-red-500');
            input.classList.remove('border-gray-300');

            if (!errorMsg) {
                errorMsg = document.createElement('p');
                errorMsg.className = 'text-red-500 text-sm mt-1 js-error-msg';
                errorMsg.textContent = 'Không được để trống';
                relativeWrapper.insertAdjacentElement('afterend', errorMsg);
            }
        } else {
            input.classList.remove('border-red-500', 'focus:border-red-500', 'focus:ring-red-500');
            input.classList.add('border-gray-300');
            if (errorMsg) {
                errorMsg.remove();
            }
        }
    }

    if (submitBtn) {
        inputs.forEach(input => {
            input.addEventListener('input', function () {
                if (input === passwordInput) {
                    validatePasswordRules();
                }

                checkFormValidity();
                if (input.value.trim() !== '') {
                    validateInput(input);
                }
            });
            input.addEventListener('blur', function () {
                validateInput(input);
            });
        });

        if (agreeCheckbox) {
            agreeCheckbox.addEventListener('change', checkFormValidity);
        }
    }

    if (passwordInput) {
        passwordInput.addEventListener('input', validatePasswordRules);
        passwordInput.addEventListener('input', checkFormValidity);
    }

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

    if (passwordInput) {
        validatePasswordRules();
    }
    checkFormValidity();
});
