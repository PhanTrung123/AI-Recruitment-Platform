const menu = document.getElementById("adminMenu");
const overlay = document.getElementById("menuOverlay");
const openBtn = document.getElementById("openMenu");
const closeBtn = document.getElementById("closeMenu");

function updateBreadcrumb(action) {
    const breadcrumbContainer = document.getElementById("breadcrumbContainer");
    if (!breadcrumbContainer) return;

    // Remove existing action breadcrumb (the last span after the last caret)
    const children = Array.from(breadcrumbContainer.children);
    for (let i = children.length - 1; i >= 0; i--) {
        const child = children[i];
        if (child.tagName === 'SPAN' && child.textContent === 'Thêm mới' || child.textContent === 'Chỉnh sửa') {
            const caret = child.previousElementSibling;
            if (caret && caret.classList.contains('ph-caret-right')) {
                caret.remove();
            }
            child.remove();
            break;
        }
    }

    if (action) {
        const caret = document.createElement('i');
        caret.className = 'ph ph-caret-right text-xs text-slate-400';
        breadcrumbContainer.appendChild(caret);

        const actionSpan = document.createElement('span');
        actionSpan.className = 'font-medium text-slate-800';
        actionSpan.textContent = action;
        breadcrumbContainer.appendChild(actionSpan);
    }
}

function resetBreadcrumb() {
    updateBreadcrumb(null);
}

function openMenu() {
    if (!menu || !overlay) return;

    menu.classList.remove("-translate-x-full");
    menu.classList.add("!translate-x-0");
    overlay.classList.remove("hidden");
}

function closeMenu() {
    if (!menu || !overlay) return;

    menu.classList.remove("!translate-x-0");
    menu.classList.add("-translate-x-full");
    overlay.classList.add("hidden");
}

openBtn?.addEventListener("click", openMenu);
closeBtn?.addEventListener("click", closeMenu);
overlay?.addEventListener("click", closeMenu);

window.addEventListener("resize", function () {
    if (!menu || !overlay) return;

    if (window.innerWidth >= 768) {
        menu.classList.remove("-translate-x-full");
        menu.classList.remove("!translate-x-0");
        overlay.classList.add("hidden");
    } else {
        if (!menu.classList.contains("!translate-x-0")) {
            menu.classList.add("-translate-x-full");
        }
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const deleteModal = document.getElementById('deleteModal');
    const cancelDeleteBtn = document.getElementById('cancelDeleteBtn');
    const confirmDeleteLink = document.getElementById('confirmDeleteLink');

    function closeModal() {
        if (deleteModal) {
            deleteModal.classList.add('hidden');
            deleteModal.classList.remove('flex');
        }
    }

    document.addEventListener('click', (e) => {
        const deleteBtn = e.target.closest('.delete-btn');
        if (deleteBtn) {
            const deleteUrl = deleteBtn.getAttribute('data-url');

            if (deleteUrl && confirmDeleteLink && deleteModal) {
                confirmDeleteLink.href = deleteUrl;
                deleteModal.classList.remove('hidden');
                deleteModal.classList.add('flex');
            }
        }
    });

    if (cancelDeleteBtn) {
        cancelDeleteBtn.addEventListener('click', closeModal);
    }

    if (deleteModal) {
        deleteModal.addEventListener('click', (e) => {
            if (e.target === deleteModal) closeModal();
        });
    }
});

function toggleDropdown(type) {
    document.getElementById(`${type}DropdownList`).classList.toggle('hidden');
}

function selectOption(element, type) {
    const value = element.getAttribute('data-value');
    const name = element.getAttribute('data-name');

    const realSelect = document.getElementById(`${type}RealSelect`);
    for (let option of realSelect.options) {
        if (option.value === value) {
            option.selected = true;
            break;
        }
    }

    element.classList.add('hidden');

    const container = document.getElementById(`${type}SelectedTagsContainer`);
    const tag = document.createElement('span');
    tag.className = 'inline-flex items-center bg-blue-50 border border-blue-200 text-blue-700 text-xs font-semibold px-2.5 py-1 rounded-md gap-1.5 transition-all';
    tag.id = `tag-${type}-${value}`;
    tag.innerHTML = `
        ${name}
        <button type="button" class="text-blue-400 hover:text-blue-800 focus:outline-none transition-colors" onclick="removeOption('${value}', '${type}', event)">
            <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
            </svg>
        </button>
    `;
    container.appendChild(tag);

    document.getElementById(`${type}Placeholder`).placeholder = '';
    document.getElementById(`${type}DropdownList`).classList.add('hidden');
}

function removeOption(value, type, event) {
    event.stopPropagation();

    const realSelect = document.getElementById(`${type}RealSelect`);
    for (let option of realSelect.options) {
        if (option.value === value) {
            option.selected = false;
            break;
        }
    }

    document.getElementById(`tag-${type}-${value}`).remove();

    const options = document.querySelectorAll(`.${type}-option`);
    options.forEach(opt => {
        if (opt.getAttribute('data-value') === value) {
            opt.classList.remove('hidden');
        }
    });

    const container = document.getElementById(`${type}SelectedTagsContainer`);
    if (container.children.length === 0) {
        const placeholderEl = document.getElementById(`${type}Placeholder`);
        placeholderEl.placeholder = placeholderEl.getAttribute('data-default') || '-- Chọn --';
    }
}

document.addEventListener('click', function (event) {
    const containers = document.querySelectorAll('.custom-select-container');
    containers.forEach(container => {
        const type = container.getAttribute('data-type');
        if (!container.contains(event.target)) {
            const dropdown = document.getElementById(`${type}DropdownList`);
            if (dropdown) dropdown.classList.add('hidden');
        }
    });
});

// Initialize multi-select fields on page load (for edit mode)
document.addEventListener('DOMContentLoaded', function() {
    ['domain', 'skill'].forEach(type => {
        const realSelect = document.getElementById(`${type}RealSelect`);
        if (!realSelect) return;

        const container = document.getElementById(`${type}SelectedTagsContainer`);
        const placeholder = document.getElementById(`${type}Placeholder`);
        const dropdownOptions = document.querySelectorAll(`.${type}-option`);

        // Get selected values from the hidden select
        const selectedValues = Array.from(realSelect.selectedOptions).map(option => option.value);

        if (selectedValues.length > 0) {
            // Hide placeholder
            if (placeholder) {
                placeholder.placeholder = '';
            }

            // Create tags for each selected value
            selectedValues.forEach(value => {
                const option = Array.from(realSelect.options).find(opt => opt.value === value);
                if (option) {
                    const name = option.text;
                    const tag = document.createElement('span');
                    tag.className = 'inline-flex items-center bg-blue-50 border border-blue-200 text-blue-700 text-xs font-semibold px-2.5 py-1 rounded-md gap-1.5 transition-all';
                    tag.id = `tag-${type}-${value}`;
                    tag.innerHTML = `
                        ${name}
                        <button type="button" class="text-blue-400 hover:text-blue-800 focus:outline-none transition-colors" onclick="removeOption('${value}', '${type}', event)">
                            <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
                            </svg>
                        </button>
                    `;
                    container.appendChild(tag);

                    // Hide the selected option from dropdown
                    dropdownOptions.forEach(opt => {
                        if (opt.getAttribute('data-value') === value) {
                            opt.classList.add('hidden');
                        }
                    });
                }
            });
        }
    });
});
