// Modal Edit Profile JavaScript
document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('editProfileModal');
    const backdrop = document.getElementById('modalBackdrop');
    const closeBtns = document.querySelectorAll('.closeModalBtn');

    // Hàm mở Modal
    window.openModal = function() {
        if (modal) {
            modal.classList.remove('hidden');
            document.body.style.overflow = 'hidden'; // Khóa cuộn trang nền
            console.log('Modal opened');
        } else {
            console.error('Modal element not found');
        }
    }

    // Hàm đóng Modal
    window.closeModal = function() {
        if (modal) {
            modal.classList.add('hidden');
            document.body.style.overflow = ''; // Mở lại cuộn trang
            console.log('Modal closed');
        }
    }

    // Đóng khi click nút 'X' hoặc nút 'Huỷ'
    closeBtns.forEach(btn => btn.addEventListener('click', closeModal));

    // Đóng khi click vào vùng tối bên ngoài (backdrop)
    if (backdrop) {
        backdrop.addEventListener('click', closeModal);
    }

    // Đóng khi nhấn phím ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && modal && !modal.classList.contains('hidden')) {
            closeModal();
        }
    });

    console.log('Modal script loaded');
});
