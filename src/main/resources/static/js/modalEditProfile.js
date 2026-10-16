// --- 1. Đặt hàm ở global scope (ngoài cùng) để HTML onclick="openModal()" nhận diện được ngay lập tức ---
window.openModal = function () {
    console.log('Global openModal called');
    const modalElement = document.getElementById('editProfileModal');
    if (modalElement) {
        modalElement.classList.remove('hidden');
        document.body.style.overflow = 'hidden'; // Khóa cuộn trang nền
        console.log('Modal opened successfully');
    } else {
        console.error('Không tìm thấy element #editProfileModal trong DOM');
    }
};

window.closeModal = function () {
    console.log('Global closeModal called');
    const modalElement = document.getElementById('editProfileModal');
    if (modalElement) {
        modalElement.classList.add('hidden');
        document.body.style.overflow = ''; // Mở lại cuộn trang nền
        console.log('Modal closed');
    }
};

// --- 2. Xử lý các sự kiện đóng modal khi DOM đã sẵn sàng ---
document.addEventListener('DOMContentLoaded', function () {
    const backdrop = document.getElementById('modalBackdrop');
    const closeBtns = document.querySelectorAll('.closeModalBtn');

    // Đóng khi click nút 'X' hoặc nút 'Huỷ'
    closeBtns.forEach(btn => {
        btn.addEventListener('click', window.closeModal);
    });

    // Đóng khi click vào vùng tối bên ngoài (backdrop)
    if (backdrop) {
        backdrop.addEventListener('click', window.closeModal);
    }

    // Đóng khi nhấn phím ESC
    document.addEventListener('keydown', (e) => {
        const modalElement = document.getElementById('editProfileModal');
        if (e.key === 'Escape' && modalElement && !modalElement.classList.contains('hidden')) {
            window.closeModal();
        }
    });

    console.log('Modal script initialized successfully');
});