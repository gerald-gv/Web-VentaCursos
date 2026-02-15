function toggleUserDropdown() {
    const dropdown = document.getElementById("userDropdown");
    dropdown.classList.toggle("hidden");
}

// Listener cuando suceda un click afuera
document.addEventListener("click", function(e) {
    const btn = document.getElementById("userDropdownBtn");
    const dropdown = document.getElementById("userDropdown");

    // Si btn o dropdown son null, el condicional simplemente será falso y no lanzará error
    if (btn?.contains(e.target) === false && dropdown?.contains(e.target) === false) {
        dropdown.classList.add("hidden");
    }
});
