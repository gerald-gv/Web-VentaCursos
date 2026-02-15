window.openModal = function() {
    console.log("Abriendo modal y guardando URL...");

    const modalBackdrop = document.getElementById('modalBackdrop');
    const modalBox = document.getElementById('modalBox');

    if (modalBackdrop) {
        // 1. Lógica de visibilidad (la que arreglamos antes)
        modalBackdrop.style.display = 'flex';
        void modalBackdrop.offsetWidth;
        modalBackdrop.style.opacity = '1';
        modalBackdrop.style.pointerEvents = 'auto';

        if (modalBox) {
            modalBox.style.opacity = '1';
            modalBox.style.transform = 'scale(1)';
        }

        // Lógica de la URL (la que estaba en el HTML)
        const currentUrl = window.location.pathname;
        const redirectField = document.getElementById('redirect-after-login');
        if (redirectField) {
            redirectField.value = currentUrl;
            console.log("URL guardada:", currentUrl);
        }

        showLogin();
    }
};
window.closeModal = function() {
    const modalBackdrop = document.getElementById('modalBackdrop');
    const modalBox = document.getElementById('modalBox');

    // Desactivar clics INMEDIATAMENTE para evitar ese muro del div back
    modalBackdrop.style.pointerEvents = 'none';

    modalBackdrop.style.opacity = '0';
    if (modalBox) {
        modalBox.style.opacity = '0';
        modalBox.style.transform = 'scale(0.95)';
    }
    setTimeout(() => {
        if (modalBackdrop.style.opacity === "0") {
            modalBackdrop.style.display = 'none';
        }
    }, 300);
};
function showLogin() {
    document.getElementById('loginForm').classList.remove('hidden');
    document.getElementById('registerForm').classList.add('hidden');

    document.getElementById('tabLogin').classList.remove('bg-neutral-900', 'text-slate-400');
    document.getElementById('tabLogin').classList.add('bg-emerald-500', 'text-neutral-900');

    document.getElementById('tabRegister').classList.remove('bg-emerald-500', 'text-neutral-900');
    document.getElementById('tabRegister').classList.add('bg-neutral-900', 'text-slate-400');
}

function showRegister() {
    document.getElementById('loginForm').classList.add('hidden');
    document.getElementById('registerForm').classList.remove('hidden');

    document.getElementById('tabRegister').classList.remove('bg-neutral-900', 'text-slate-400');
    document.getElementById('tabRegister').classList.add('bg-emerald-500', 'text-neutral-900');

    document.getElementById('tabLogin').classList.remove('bg-emerald-500', 'text-neutral-900');
    document.getElementById('tabLogin').classList.add('bg-neutral-900', 'text-slate-400');
}

// Cerrar modal si se hace clic fuera del modalBox
document.addEventListener('click', function(event) {
    const modal = document.getElementById('modalBackdrop');

    // Salir si modal no existe
    if (!modal) {
        return;
    }

    // Cerrar solo si se hizo click en el fondo (backdrop)
    if (event.target === modal) {
        closeModal();
    }
});
