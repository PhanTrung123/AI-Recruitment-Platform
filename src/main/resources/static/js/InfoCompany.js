function enableInlineEdit(button) {
    let target = button.parentElement.querySelector('.editable-target');
    if (!target && button.parentElement.parentElement) {
        target = button.parentElement.parentElement.querySelector('.editable-target');
    }

    if (!target) return;

    // Nếu đang bật chế độ sửa mà bấm vào nút lần nữa -> Lưu
    if (target.isContentEditable) {
        saveInlineEdit(target, button);
        return;
    }

    // Lấy dữ liệu gốc từ DB
    const rawValue = target.getAttribute('data-value') || '';
    target.innerText = rawValue;

    // Bật chế độ chỉnh sửa trực tiếp (Dùng bg-white để nhìn rõ cả trên nền tối của Banner)
    target.contentEditable = "true";
    target.classList.remove('text-slate-300');
    target.classList.add('bg-white', 'ring-2', 'ring-blue-500', 'outline-none', 'px-2', 'py-0.5', 'text-slate-900');
    target.focus();

    // Đưa con trỏ chuột về cuối chữ
    const range = document.createRange();
    const sel = window.getSelection();
    range.selectNodeContents(target);
    range.collapse(false);
    sel.removeAllRanges();
    sel.addRange(range);
    button.setAttribute('data-original-html', button.innerHTML);
    button.innerHTML = '<i class="ph-bold ph-check text-lg border border-emerald-500 bg-emerald-50 text-emerald-600 rounded p-1"></i>';

    const isMultiline = target.getAttribute('data-multiline') === 'true';
    target.onkeydown = function (e) {
        if (e.key === 'Enter' && !isMultiline) {
            e.preventDefault();
            target.blur();
        } else if (e.key === 'Escape') {
            finishEditingUI(target, button, rawValue);
        }
    };

    target.onblur = function () {
        setTimeout(() => {
            if (target.isContentEditable) {
                saveInlineEdit(target, button);
            }
        }, 150);
    };
}

function saveInlineEdit(target, button) {
    const field = target.getAttribute('data-field');
    const oldValue = target.getAttribute('data-value') || '';
    const newValue = target.innerText.trim();
    if (newValue === oldValue) {
        finishEditingUI(target, button, oldValue);
        return;
    }
    const formData = new URLSearchParams();
    formData.append('field', field);
    formData.append('value', newValue);
    fetch('/employer/dashboard/applicants/company-profile/update-field', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData.toString()
    })
        .then(response => {
            if (!response.ok) throw new Error('Cập nhật thất bại');
            return response.json();
        })
        .then(data => {
            target.setAttribute('data-value', newValue);
            finishEditingUI(target, button, newValue);
            if (field === 'name') {
                const bannerName = document.getElementById('banner-company-name');
                if (bannerName) bannerName.innerText = newValue || 'Tên công ty';
            }
            if (field === 'address') {
                location.reload();
            }
        })
        .catch(err => {
            alert('Có lỗi xảy ra hoặc dữ liệu không hợp lệ (VD: Năm thành lập phải là số)!');
            finishEditingUI(target, button, oldValue);
        });
}

function finishEditingUI(target, button, finalValue) {
    target.contentEditable = "false";
    target.classList.remove('bg-white', 'ring-2', 'ring-blue-500', 'outline-none', 'px-2', 'py-0.5', 'text-slate-900');

    if (target.getAttribute('data-field') === 'slogan') {
        target.classList.add('text-slate-300');
    }

    target.onkeydown = null;
    target.onblur = null;
    const suffix = target.getAttribute('data-suffix') || '';
    const emptyText = target.getAttribute('data-empty-text') || 'Chưa cập nhật';
    const isLink = target.getAttribute('data-is-link') === 'true';
    if (isLink) {
        if (finalValue !== '') {
            const href = finalValue.startsWith('http') ? finalValue : 'https://' + finalValue;
            target.innerHTML = `
                <a href="${href}" target="_blank" rel="noopener noreferrer" class="text-blue-600 hover:underline inline-flex items-start gap-1">
                    <span>${finalValue}</span>
                    <i class="ph ph-arrow-square-out shrink-0 mt-0.5"></i>
                </a>`;
        } else {
            target.innerHTML = `<span class="text-slate-400 italic font-normal">${emptyText}</span>`;
        }
    } else {
        target.innerText = finalValue !== '' ? (finalValue + suffix) : emptyText;
    }
    const originalHtml = button.getAttribute('data-original-html');
    if (originalHtml) button.innerHTML = originalHtml;
}

function saveBenefitsSimple() {
    const value = document.getElementById('benefits-textarea').value;
    const formData = new URLSearchParams({field: 'benefits', value: value});

    fetch('/employer/dashboard/applicants/company-profile/update-field', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: formData.toString()
    }).then(res => {
        if (res.ok) location.reload(); // Load lại trang để Thymeleaf tự vẽ lại icon
        else alert('Lỗi khi lưu phúc lợi!');
    });
}

function uploadLogoFile(input) {
    const file = input.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = function (e) {
        const formData = new URLSearchParams({field: 'logo', value: e.target.result});
        fetch('/employer/dashboard/applicants/company-profile/update-field', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: formData.toString()
        }).then(res => {
            if (res.ok) location.reload();
            else alert('Lỗi khi cập nhật Logo!');
        });
    };
    reader.readAsDataURL(file);
}

