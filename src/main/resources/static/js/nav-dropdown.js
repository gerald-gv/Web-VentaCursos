function toggleUserDropdown() {
    const dropdown = document.getElementById("userDropdown");
    dropdown.classList.toggle("hidden");
}

// Listener cuando suceda un click afuera
document.addEventListener("click", function(e) {
    const btn = document.getElementById("userDropdownBtn");
    const dropdown = document.getElementById("userDropdown");

    if (!btn.contains(e.target) && !dropdown.contains(e.target)) {
        dropdown.classList.add("hidden");
    }
});
