document.addEventListener("DOMContentLoaded", () => {
    const menuBtn = document.getElementById("menu-btn");
    const mobileMenu = document.getElementById("mobile-menu");
    const menuIcon = document.getElementById("menu-icon");

    if (menuBtn && mobileMenu && menuIcon) {
        menuBtn.addEventListener("click", () => {
            mobileMenu.classList.toggle("hidden");
            mobileMenu.classList.toggle("flex");

            if (mobileMenu.classList.contains("hidden")) {
                menuIcon.classList.remove("ph-x");
                menuIcon.classList.add("ph-list");
            } else {
                menuIcon.classList.remove("ph-list");
                menuIcon.classList.add("ph-x");
            }
        });
    }
});

