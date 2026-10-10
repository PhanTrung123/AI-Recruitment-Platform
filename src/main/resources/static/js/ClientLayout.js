document.addEventListener("DOMContentLoaded", () => {
    const menuBtn = document.getElementById("menu-btn");
    const mobileMenu = document.getElementById("mobile-menu");
    const menuIcon = document.getElementById("menu-icon");
    const itJobsToggle = document.getElementById("it-jobs-toggle");
    const itJobsDropdown = document.getElementById("it-jobs-dropdown");

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

            if (mobileMenu.classList.contains("hidden") && itJobsDropdown) {
                itJobsDropdown.classList.add("hidden");
                itJobsToggle?.setAttribute("aria-expanded", "false");
            }
        });
    }

    if (itJobsToggle && itJobsDropdown) {
        itJobsToggle.addEventListener("click", () => {
            if (window.matchMedia("(min-width: 1024px)").matches) return;

            itJobsDropdown.classList.toggle("hidden");
            itJobsToggle.setAttribute(
                "aria-expanded",
                String(!itJobsDropdown.classList.contains("hidden")),
            );
        });
    }
});

