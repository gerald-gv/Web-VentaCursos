// Contador global para IDs únicos de módulos
let moduloCounter = 0;

/**
 * Inicializar formulario al cargar la página
 */
document.addEventListener('DOMContentLoaded', function() {
    // Cargar módulos existentes si está editando
    if (typeof modulosExistentes !== 'undefined' && modulosExistentes.length > 0) {
        modulosExistentes.forEach(modulo => {
            agregarModuloExistente(modulo);
        });
    }

    // Actualizar contador
    actualizarContadorModulos();
});

/**
 * Agregar un módulo nuevo (vacío)
 */
function agregarModulo() {
    const container = document.getElementById('modulos-container');
    const moduloId = moduloCounter++;
    const numeroModulo = getNumeroModulo();

    const moduloHTML = `
        <div class="modulo-item bg-neutral-800 border border-slate-700 rounded-lg p-4" data-modulo-id="${moduloId}">
            <div class="flex items-start justify-between mb-4">
                <h3 class="text-lg font-semibold text-white">
                    📹 Módulo <span class="modulo-numero">${numeroModulo}</span>
                </h3>
                <button type="button" 
                        onclick="eliminarModulo(${moduloId})"
                        class="text-red-400 hover:text-red-300 transition">
                    🗑️ Eliminar
                </button>
            </div>
            
            <div class="space-y-3">
                <!-- Título -->
                <div>
                    <label class="block text-sm font-medium text-slate-300 mb-1">
                        Título del Módulo *
                    </label>
                    <input type="text" 
                           name="modulosTitulo"
                           required
                           class="w-full bg-neutral-900 border border-slate-600 rounded-lg px-3 py-2 text-white focus:border-emerald-500 focus:outline-none"
                           placeholder="Ej: Introducción al tema">
                </div>
                
                <!-- Descripción -->
                <div>
                    <label class="block text-sm font-medium text-slate-300 mb-1">
                        Descripción (opcional)
                    </label>
                    <textarea name="modulosDescripcion"
                              rows="2"
                              class="w-full bg-neutral-900 border border-slate-600 rounded-lg px-3 py-2 text-white focus:border-emerald-500 focus:outline-none text-sm"
                              placeholder="Breve descripción del contenido del módulo"></textarea>
                </div>
                
                <!-- URL del Video -->
                <div>
                    <label class="block text-sm font-medium text-slate-300 mb-1">
                        URL del Video *
                    </label>
                    <input type="url" 
                           name="modulosUrl"
                           required
                           class="w-full bg-neutral-900 border border-slate-600 rounded-lg px-3 py-2 text-white focus:border-emerald-500 focus:outline-none text-sm"
                           placeholder="https://www.youtube.com/watch?v=...">
                </div>
                
                <!-- Orden (oculto, se calcula automáticamente) -->
                <input type="hidden" 
                       name="modulosOrden" 
                       value="${numeroModulo}"
                       class="modulo-orden">
            </div>
        </div>
    `;

    container.insertAdjacentHTML('beforeend', moduloHTML);

    // Ocultar mensaje de "sin módulos"
    document.getElementById('empty-modulos').style.display = 'none';

    // Actualizar contador
    actualizarContadorModulos();
}

/**
 * Agregar un módulo existente (al editar curso)
 */
function agregarModuloExistente(modulo) {
    const container = document.getElementById('modulos-container');
    const moduloId = moduloCounter++;

    const moduloHTML = `
        <div class="modulo-item bg-neutral-800 border border-slate-700 rounded-lg p-4" data-modulo-id="${moduloId}">
            <div class="flex items-start justify-between mb-4">
                <h3 class="text-lg font-semibold text-white">
                    📹 Módulo <span class="modulo-numero">${modulo.orden || getNumeroModulo()}</span>
                </h3>
                <button type="button" 
                        onclick="eliminarModulo(${moduloId})"
                        class="text-red-400 hover:text-red-300 transition">
                    🗑️ Eliminar
                </button>
            </div>
            
            <div class="space-y-3">
                <!-- Título -->
                <div>
                    <label class="block text-sm font-medium text-slate-300 mb-1">
                        Título del Módulo *
                    </label>
                    <input type="text" 
                           name="modulosTitulo"
                           value="${escapeHtml(modulo.titulo || '')}"
                           required
                           class="w-full bg-neutral-900 border border-slate-600 rounded-lg px-3 py-2 text-white focus:border-emerald-500 focus:outline-none"
                           placeholder="Ej: Introducción al tema">
                </div>
                
                <!-- Descripción -->
                <div>
                    <label class="block text-sm font-medium text-slate-300 mb-1">
                        Descripción (opcional)
                    </label>
                    <textarea name="modulosDescripcion"
                              rows="2"
                              class="w-full bg-neutral-900 border border-slate-600 rounded-lg px-3 py-2 text-white focus:border-emerald-500 focus:outline-none text-sm"
                              placeholder="Breve descripción del contenido del módulo">${escapeHtml(modulo.descripcion || '')}</textarea>
                </div>
                
                <!-- URL del Video -->
                <div>
                    <label class="block text-sm font-medium text-slate-300 mb-1">
                        URL del Video *
                    </label>
                    <input type="url" 
                           name="modulosUrl"
                           value="${escapeHtml(modulo.url || '')}"
                           required
                           class="w-full bg-neutral-900 border border-slate-600 rounded-lg px-3 py-2 text-white focus:border-emerald-500 focus:outline-none text-sm"
                           placeholder="https://www.youtube.com/watch?v=...">
                </div>
                
                <!-- Orden -->
                <input type="hidden" 
                       name="modulosOrden" 
                       value="${modulo.orden || getNumeroModulo()}"
                       class="modulo-orden">
            </div>
        </div>
    `;

    container.insertAdjacentHTML('beforeend', moduloHTML);

    // Ocultar mensaje de "sin módulos"
    document.getElementById('empty-modulos').style.display = 'none';

    // Actualizar contador
    actualizarContadorModulos();
}

/**Eliminar un módulo**/
function eliminarModulo(moduloId) {
    const modulo = document.querySelector(`[data-modulo-id="${moduloId}"]`);

    if (modulo) {
        modulo.remove();

        // Reordenar módulos restantes
        reordenarModulos();

        // Mostrar mensaje si no hay módulos
        const modulosRestantes = document.querySelectorAll('.modulo-item');
        if (modulosRestantes.length === 0) {
            document.getElementById('empty-modulos').style.display = 'block';
        }

        // Actualizar contador
        actualizarContadorModulos();
    }
}

/**
 * Reordenar módulos después de eliminar uno
 */
function reordenarModulos() {
    const modulos = document.querySelectorAll('.modulo-item');

    modulos.forEach((modulo, index) => {
        const numero = index + 1;

        // Actualizar número visual
        const numeroSpan = modulo.querySelector('.modulo-numero');
        if (numeroSpan) {
            numeroSpan.textContent = numero;
        }

        // Actualizar campo de orden
        const ordenInput = modulo.querySelector('.modulo-orden');
        if (ordenInput) {
            ordenInput.value = numero;
        }
    });
}

/**
 * Obtener el número del próximo módulo
 */
function getNumeroModulo() {
    const modulos = document.querySelectorAll('.modulo-item');
    return modulos.length + 1;
}

/**
 * Actualizar contador de módulos
 */
function actualizarContadorModulos() {
    reordenarModulos();
}

/**
 * Escapar HTML para prevenir XSS
 */
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text ? text.replace(/[&<>"']/g, m => map[m]) : '';
}

/**Validar formulario antes de enviar**/
document.getElementById('curso-form')?.addEventListener('submit', function(e) {
    const modulos = document.querySelectorAll('.modulo-item');

    if (modulos.length === 0) {
        const agregar = confirm('No has agregado ningún módulo. ¿Deseas continuar sin módulos?');
        if (!agregar) {
            e.preventDefault();
            return false;
        }
    }

    return true;
});