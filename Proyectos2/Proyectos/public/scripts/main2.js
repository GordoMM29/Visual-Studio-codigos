async function mostrarContenido(tipo) {
    const contenido = document.getElementById("contenido");

    const tipos = {
        facultativos: 1,       // Con conferencia
        universitarios: 2,     // Taller
        sociedad: 3,           // Seminario
        totales: 4             // Evento cultural
    };

    const idTipo = tipos[tipo];

    try {
        const response = await fetch(`http://localhost:3000/api/eventos/tipo/${idTipo}`);
        const data = await response.json();
        console.log(data);


        if (data.success) {
            if (data.eventos.length === 0) {
                contenido.innerHTML = `<p>No hay eventos de este tipo aún.</p>`;
                return;
            }

            contenido.innerHTML = data.eventos.map(evento => `
                <div class="evento">
                    <h2>${evento.nombre_evento}</h2>
                    <p>${evento.descripcion_corta}</p>
                    <p><strong>Lugar:</strong> ${evento.lugar}</p>
                    <p><strong>Fecha:</strong> ${new Date(evento.fecha_hora).toLocaleString()}</p>
                    ${evento.imagen_url ? `<img src="${evento.imagen_url}" width="300">` : ''}
                </div>
            `).join('');
        } else {
            contenido.innerHTML = `<p>Error al cargar los eventos: ${data.message}</p>`;
        }
    } catch (err) {
        contenido.innerHTML = `<p>Error de conexión: ${err.message}</p>`;
    }

}

