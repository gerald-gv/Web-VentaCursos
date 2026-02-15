/**
 * Confirmar eliminación de curso
 */
function cambiarEstadoCurso(button) {
    const idCurso = button.getAttribute('data-curso-id');
    const tituloCurso = button.getAttribute('data-curso-titulo');
    const activo = button.getAttribute('data-curso-activo') === 'true';

    const accion = activo ? 'desactivar' : 'activar';
    const confirmacion = confirm(
        `¿Deseas ${accion} el curso "${tituloCurso}"?\n\n` +
        (activo
            ? '• El curso dejará de aparecer en el catálogo público\n' +
            '• Los usuarios que ya lo compraron seguirán teniendo acceso\n' +
            '• Los módulos y contenido se mantendrán intactos'
            : '• El curso volverá a aparecer en el catálogo público\n' +
            '• Estará disponible para nuevas compras')
    );

    if (confirmacion) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = `/admin/cursos/cambiar-estado/${idCurso}`;

        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'nuevoEstado';
        input.value = !activo;

        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();
    }
}
/**
 * Confirmar salir sin guardar
 */
function confirmarSalirSinGuardar() {
    return confirm(
        '¿Estás seguro de que deseas salir?\n\n' +
        'Los cambios no guardados se perderán.'
    );
}
/**
 * Cambiar estado de usuario (activar/desactivar)
 */
function cambiarEstadoUsuario(button) {
    const idUsuario = button.getAttribute('data-usuario-id');
    const nombreUsuario = button.getAttribute('data-usuario-nombre');
    const activo = button.getAttribute('data-usuario-activo') === 'true';

    const accion = activo ? 'desactivar' : 'activar';
    const confirmacion = confirm(
        `¿Deseas ${accion} al usuario "${nombreUsuario}"?\n\n` +
        (activo
            ? 'Al desactivarlo, no podrá iniciar sesión en la plataforma.'
            : 'Al activarlo, podrá volver a iniciar sesión.')
    );

    if (confirmacion) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = `/admin/usuarios/cambiar-estado/${idUsuario}`;

        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'nuevoEstado';
        input.value = !activo;

        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();
    }
}
/**
 * Detectar cambios en el formulario
 */
let formularioModificado = false;

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('curso-form');

    if (form) {
        // Detectar cambios en inputs
        form.addEventListener('input', function() {
            formularioModificado = true;
        });

        // Detectar cambios en selects
        form.addEventListener('change', function() {
            formularioModificado = true;
        });

        // Resetear al enviar
        form.addEventListener('submit', function() {
            formularioModificado = false;
        });
    }

    // Advertir al salir si hay cambios sin guardar
    window.addEventListener('beforeunload', function(e) {
        if (formularioModificado) {
            e.preventDefault();
            e.returnValue = '';
            return '';
        }
    });

    // Confirmar en botones de cancelar
    const botonesCancel = document.querySelectorAll('a[href="/admin/cursos"]');
    botonesCancel.forEach(boton => {
        boton.addEventListener('click', function(e) {
            if (formularioModificado) {
                if (!confirmarSalirSinGuardar()) {
                    e.preventDefault();
                }
            }
        });
    });
});