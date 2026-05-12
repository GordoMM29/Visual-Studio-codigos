const imageInput = document.getElementById('imagen_file');
const urlInput = document.getElementById('imagen_url');
const preview = document.getElementById('preview');
const uploadBox = document.querySelector('.item__image-upload');

uploadBox.addEventListener('dragover', (e) => {
    e.preventDefault();
    uploadBox.style.backgroundColor = '#dfe9ff';
});

uploadBox.addEventListener('dragleave', () => {
    uploadBox.style.backgroundColor = '#f9f9ff';
});

uploadBox.addEventListener('drop', (e) => {
    e.preventDefault();
    uploadBox.style.backgroundColor = '#f9f9ff';
    const file = e.dataTransfer.files[0];
    if (file && file.type.startsWith('image/')) {
        mostrarPreview(file);
        imageInput.files = e.dataTransfer.files;
    }
});

imageInput.addEventListener('change', (e) => {
    const file = e.target.files[0];
    if (file && file.type.startsWith('image/')) {
        mostrarPreview(file);
    }
});

urlInput.addEventListener('input', () => {
    const url = urlInput.value.trim();
    if (url.startsWith('http')) {
        mostrarPreview(url);
    }
});

function mostrarPreview(data) {
    let src = '';
    if (typeof data === 'string') {
        src = data;
        imageInput.value = '';
    } else {
        const reader = new FileReader();
        reader.onload = (e) => {
            preview.innerHTML = generarHTMLPreview(e.target.result);
        };
        reader.readAsDataURL(data);
        return;
    }
    preview.innerHTML = generarHTMLPreview(src);
}

function generarHTMLPreview(src) {
    return `
        <div class="image__wrapper">
            <img src="${src}" alt="Vista previa">
            <button type="button" class="remove-btn" id="removeBtn">✖</button>
        </div>
    `;
}

preview.addEventListener('click', (e) => {
    if (e.target.id === 'removeBtn') {
        preview.innerHTML = '';
        imageInput.value = '';
        urlInput.value = '';
    }
});

// Envío del formulario - MODIFICADO PARA COINCIDIR CON BD
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("eventoForm");
    const mensaje = document.getElementById("mensaje");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        // Preparar datos para enviar
        const formData = new FormData();
        
        // Agregar campos que coinciden con la BD
        formData.append("organizador_id", document.getElementById("organizador_id").value);
        formData.append("nombre_evento", document.getElementById("nombre_evento").value);
        formData.append("fecha_hora", document.getElementById("fecha_hora").value);
        formData.append("lugar", document.getElementById("lugar").value);
        formData.append("descripcion_corta", document.getElementById("descripcion_corta").value);
        formData.append("descripcion_larga", document.getElementById("descripcion_larga").value);
        formData.append("cupos", document.getElementById("cupos").value);
        formData.append("costo", document.getElementById("costo").value);
        formData.append("invitado", document.getElementById("invitado").value);
        formData.append("estado", document.getElementById("estado").value);
        formData.append("tipo_evento", document.getElementById("tipo_evento").value);
        formData.append("imagen_url", document.getElementById("imagen_url").value);

        // Agregar archivo de imagen si existe
        const imagenFile = document.getElementById("imagen_file").files[0];
        if (imagenFile) {
            formData.append("imagen_file", imagenFile);
        }

        try {
            const response = await fetch("http://localhost:3000/api/eventos", {
                method: "POST",
                body: formData
            });

            if (!response.ok) {
                const text = await response.text();
                console.error("❌ Error del servidor:", text);
                mensaje.textContent = "Error en el servidor: revisa la consola";
                mensaje.style.color = "red";
                return;
            }

            const data = await response.json();

            if (data.success) {
                mensaje.textContent = "Evento creado con éxito ✅";
                mensaje.style.color = "green";
                form.reset();
                document.getElementById("preview").innerHTML = "";
            } else {
                mensaje.textContent = `Error: ${data.message || 'No se pudo crear el evento'}`;
                mensaje.style.color = "red";
            }
        } catch (error) {
            console.error("❌ Error de conexión:", error);
            mensaje.textContent = `Error de conexión: ${error.message}`;
            mensaje.style.color = "red";
        }
    });
});