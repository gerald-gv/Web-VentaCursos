const modalBackdrop = document.getElementById('modalBackdrop');
const modalBox = modalBackdrop.querySelector('div.bg-neutral-950'); // modal interno

function openModal() {

    // Inicio de la Animacion
    modalBackdrop.style.display = 'flex';
    modalBackdrop.style.opacity = '0';
    modalBox.style.opacity = '0';
    modalBox.style.transform = 'scale(0.95)';

    // Forzar reflow para reiniciar animación
    void modalBackdrop.offsetWidth;

    // Fondo opaco del Modal
    modalBackdrop.style.transition = 'opacity 0.3s ease';
    modalBackdrop.style.opacity = '1';

    // Animacion Suave para el Modal
    modalBox.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
    modalBox.style.opacity = '1';
    modalBox.style.transform = 'scale(1)';

    showLogin();
}

function closeModal() {

    // Animacion de salida
    modalBackdrop.style.opacity = '0';
    modalBox.style.opacity = '0';
    modalBox.style.transform = 'scale(0.95)';

    // Cuando termine la animación ocultamos el modal completamente
    modalBackdrop.addEventListener('transitionend', handleTransitionEnd);
}

function handleTransitionEnd(e) {
    if (e.target === modalBackdrop && modalBackdrop.style.opacity === '0') {
        modalBackdrop.style.display = 'none';
        modalBackdrop.removeEventListener('transitionend', handleTransitionEnd);
    }
}

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
modalBackdrop.addEventListener('click', (event) => {
    if (event.target === modalBackdrop) {
        closeModal();
    }
});
