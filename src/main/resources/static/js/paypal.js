// ==================== PAYPAL HANDLER ====================

/**
 * Inicializa el botón de PayPal en un contenedor específico
 * @param {string} containerId - ID del contenedor donde se renderizará el botón
 * @param {number} cursoId - ID del curso
 * @param {number} cursoPrecio - Precio del curso
 * @param {string} cursoTitulo - Título del curso
 */
function inicializarBotonPayPal(containerId, cursoId, cursoPrecio, cursoTitulo) {

    // Verificar que PayPal esté disponible
    if (typeof paypal === 'undefined') {
        console.error('PayPal SDK no está cargado');
        document.getElementById(containerId).innerHTML =
            '<div class="text-red-500 text-center p-4">Error: No se pudo cargar PayPal. Por favor recarga la página.</div>';
        return;
    }

    console.log('Renderizando botón PayPal para curso:', cursoId, 'Precio:', cursoPrecio);

    // Renderizar botón de PayPal
    paypal.Buttons({
        style: {
            layout: 'vertical',
            color: 'blue',
            shape: 'rect',
            label: 'pay'
        },

        createOrder: function(data, actions) {
            console.log('Creando orden PayPal...');
            return actions.order.create({
                purchase_units: [{
                    amount: {
                        value: cursoPrecio.toFixed(2),
                        currency_code: 'USD'
                    },
                    description: 'Compra de curso: ' + cursoTitulo
                }]
            });
        },

        onApprove: function(data, actions) {
            console.log('Pago aprobado, capturando orden...');
            return actions.order.capture().then(function(details) {
                console.log('Orden capturada:', details);
                finalizarCompraBackend(details.id, cursoId, containerId);
            });
        },

        onError: function(err) {
            console.error('Error en PayPal:', err);
            alert('Ocurrió un error con el pago de PayPal. Por favor, inténtalo nuevamente.');
        },

        onCancel: function(data) {
            console.log('Pago cancelado por el usuario:', data);
            // Opcional: mostrar mensaje
        }

    }).render('#' + containerId)
        .catch(function(err) {
            console.error('Error al renderizar botón PayPal:', err);
            document.getElementById(containerId).innerHTML =
                '<div class="text-red-500 text-center p-4">Error al cargar el botón de pago. Por favor recarga la página.</div>';
        });
}

/**
 * Finaliza la compra en el backend
 * @param {string} orderId - ID de la orden de PayPal
 * @param {number} idCurso - ID del curso
 * @param {string} containerId - ID del contenedor (para mostrar loading)
 */
function finalizarCompraBackend(orderId, idCurso, containerId) {
    console.log('Finalizando compra en backend...', orderId);

    // Mostrar loading
    if (containerId) {
        document.getElementById(containerId).innerHTML =
            '<div class="text-center py-8"><p class="text-emerald-400 text-lg">⏳ Procesando tu compra...</p></div>';
    }

    fetch('/api/compras/finalizar-paypal', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            orderId: orderId,
            idCurso: idCurso
        })
    })
        .then(async response => {
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || data.mensaje || 'Error al procesar la compra');
            }

            return data;
        })
        .then(data => {
            console.log('Compra finalizada exitosamente:', data);
            // Redirigir a mis cursos con mensaje de éxito
            window.location.href = '/mis-cursos?compraExitosa=true';
        })
        .catch(err => {
            console.error('Error:', err);
            alert(err.message);
            // Recargar para volver a mostrar el botón
            location.reload();
        });
}

/*Abre un modal/popup con el botón de PayPal*/
function abrirModalPayPal(cursoId, cursoPrecio, cursoTitulo) {

    // Crear overlay y modal
    const overlay = document.createElement('div');
    overlay.id = 'paypal-modal-overlay';
    overlay.className = 'fixed inset-0 bg-black bg-opacity-75 z-50 flex items-center justify-center p-4';
    overlay.onclick = function(e) {
        if (e.target === overlay) cerrarModalPayPal();
    };

    const modal = document.createElement('div');
    modal.className = 'bg-neutral-900 border border-slate-800 rounded-xl p-8 max-w-md w-full shadow-2xl';
    modal.innerHTML = `
        <div class="mb-6">
            <h3 class="text-2xl font-bold text-white mb-2">${cursoTitulo}</h3>
            <p class="text-slate-400">Precio: <span class="text-emerald-400 font-bold text-xl">$${cursoPrecio.toFixed(2)} USD</span></p>
        </div>
        
        <div id="paypal-button-modal-container" class="mb-4"></div>
        
        <button onclick="cerrarModalPayPal()" 
                class="w-full border border-slate-700 text-slate-400 py-2 rounded-lg hover:bg-slate-800 transition">
            Cancelar
        </button>
    `;

    overlay.appendChild(modal);
    document.body.appendChild(overlay);

    // Prevenir scroll del body
    document.body.style.overflow = 'hidden';

    // Inicializar PayPal en el modal
    inicializarBotonPayPal('paypal-button-modal-container', cursoId, cursoPrecio, cursoTitulo);
}

/**Cierra el modal de PayPal**/
function cerrarModalPayPal() {
    const overlay = document.getElementById('paypal-modal-overlay');
    if (overlay) {
        overlay.remove();
        document.body.style.overflow = '';
    }
}