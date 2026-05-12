console.log("✅ main.js cargado");

// Variables globales
let currentDate = new Date();
let events = [];
let currentUserType = localStorage.getItem('userType') || 'student';

// Función principal para mostrar secciones
function showSection(sectionId) {
    console.log("Mostrando sección:", sectionId);
    
    // Ocultar todas las secciones
    document.querySelectorAll('.main__contenido, .calendar-section, .admin-section').forEach(section => {
        section.style.display = 'none';
    });
    
    // Mostrar sección seleccionada
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.style.display = 'block';
        
        // Si es calendario, inicializarlo
        if (sectionId === 'calendar') {
            initializeCalendar();
        }
        // Si es admin, cargar usuarios
        if (sectionId === 'admin' && currentUserType === 'admin') {
            loadUsers();
        }
    }
}

// Cerrar sesión
function logout() {
    localStorage.clear();
    window.location.href = 'Login.html';
}

// Calendario
async function initializeCalendar() {
    await loadEventsFromDB();
    updateCalendar();
    if (currentUserType === 'student') {
        updateEventRegistrations();
    }
}

// Cargar eventos desde la base de datos
async function loadEventsFromDB() {
    try {
        const response = await fetch("http://localhost:3000/api/eventos");
        const data = await response.json();
        
        if (data.success) {
            events = data.eventos.map(evento => ({
                id: evento.id_evento,
                title: evento.nombre_evento,
                date: evento.fecha_hora.split('T')[0], // Extraer solo la fecha
                time: evento.fecha_hora.split('T')[1]?.substring(0, 5), // Extraer hora
                location: evento.lugar,
                description: evento.descripcion_larga,
                type: getEventTypeName(evento.id_tipo),
                maxCapacity: evento.cupos,
                price: evento.costo,
                priceType: evento.costo > 0 ? 'paid' : 'free',
                registeredUsers: [] // Esto vendría de otra tabla en la BD
            }));
        } else {
            console.error("Error al cargar eventos:", data.message);
            events = [];
        }
    } catch (error) {
        console.error("Error de conexión:", error);
        events = [];
    }
}

// Función auxiliar para obtener el nombre del tipo de evento
function getEventTypeName(idTipo) {
    const tipos = {
        1: 'Conferencia',
        2: 'Taller', 
        3: 'Seminario',
        4: 'Evento Cultural'
    };
    return tipos[idTipo] || 'General';
}

function updateCalendar() {
    const monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                       "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
    
    document.getElementById('currentMonth').textContent = 
        `${monthNames[currentDate.getMonth()]} ${currentDate.getFullYear()}`;
    
    const calendarGrid = document.getElementById('calendarGrid');
    calendarGrid.innerHTML = '';
    
    const firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
    const lastDay = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
    
    // Días vacíos al inicio
    for (let i = 0; i < firstDay.getDay(); i++) {
        const emptyDay = document.createElement('div');
        emptyDay.className = 'calendar-day empty';
        calendarGrid.appendChild(emptyDay);
    }
    
    // Días del mes
    for (let day = 1; day <= lastDay.getDate(); day++) {
        const dayElement = document.createElement('div');
        dayElement.className = 'calendar-day';
        dayElement.innerHTML = `<div class="day-number">${day}</div>`;
        
        const currentDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), day);
        const dayEvents = events.filter(event => {
            const eventDate = new Date(event.date);
            return eventDate.toDateString() === currentDay.toDateString();
        });
        
        dayEvents.forEach(event => {
            const eventElement = document.createElement('div');
            eventElement.className = 'event';
            
            const registeredCount = event.registeredUsers ? event.registeredUsers.length : 0;
            const hasCapacity = !event.maxCapacity || registeredCount < event.maxCapacity;
            const isRegistered = event.registeredUsers && 
                               event.registeredUsers.includes(localStorage.getItem('userEmail'));
            
            eventElement.innerHTML = `
                <div class="event-title">${event.title}</div>
                ${event.time ? `<div class="event-time">🕒 ${event.time}</div>` : ''}
                ${event.location ? `<div class="event-location">📍 ${event.location}</div>` : ''}
                ${event.maxCapacity ? `<div class="event-capacity">👥 ${registeredCount}/${event.maxCapacity}</div>` : ''}
                ${event.priceType === 'paid' ? `<div class="event-price">💰 $${event.price}</div>` : '<div class="event-free">🆓 Gratuito</div>'}
                ${currentUserType === 'student' ? 
                  `<button class="btn-register" onclick="registerForEvent(${event.id})" 
                    ${isRegistered || !hasCapacity ? 'disabled' : ''}>
                    ${isRegistered ? 'Inscrito ✓' : (hasCapacity ? 'Inscribirse' : 'Sin cupo')}
                   </button>` : 
                  `<button class="btn-delete" onclick="deleteEvent(${event.id})">Eliminar</button>`
                }
            `;
            dayElement.appendChild(eventElement);
        });
        
        calendarGrid.appendChild(dayElement);
    }
}

function previousMonth() {
    currentDate.setMonth(currentDate.getMonth() - 1);
    updateCalendar();
}

function nextMonth() {
    currentDate.setMonth(currentDate.getMonth() + 1);
    updateCalendar();
}

function filterEvents(type) {
    // Por ahora mostrar todos los eventos
    updateCalendar();
}

// Estudiante - Inscribirse en eventos
async function registerForEvent(eventId) {
    const userEmail = localStorage.getItem('userEmail');
    const event = events.find(e => e.id === eventId);
    
    if (!event.registeredUsers) event.registeredUsers = [];
    
    if (event.registeredUsers.includes(userEmail)) {
        alert('Ya estás inscrito en este evento');
        return;
    }
    
    try {
        // Aquí harías la llamada a la API para registrar al usuario en el evento
        const response = await fetch(`http://localhost:3000/api/eventos/${eventId}/inscribir`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                usuario: userEmail
            })
        });
        
        const data = await response.json();
        
        if (data.success) {
            event.registeredUsers.push(userEmail);
            updateCalendar();
            updateEventRegistrations();
            alert('Inscrito en: ' + event.title);
        } else {
            alert('Error al inscribirse: ' + data.message);
        }
    } catch (error) {
        console.error('Error al inscribirse:', error);
        alert('Error de conexión al inscribirse');
    }
}

async function unregisterFromEvent(eventId) {
    const userEmail = localStorage.getItem('userEmail');
    const event = events.find(e => e.id === eventId);
    
    try {
        // Aquí harías la llamada a la API para desinscribir al usuario
        const response = await fetch(`http://localhost:3000/api/eventos/${eventId}/desinscribir`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                usuario: userEmail
            })
        });
        
        const data = await response.json();
        
        if (data.success) {
            event.registeredUsers = event.registeredUsers.filter(email => email !== userEmail);
            updateCalendar();
            updateEventRegistrations();
            alert('Desinscrito del evento');
        } else {
            alert('Error al desinscribirse: ' + data.message);
        }
    } catch (error) {
        console.error('Error al desinscribirse:', error);
        alert('Error de conexión al desinscribirse');
    }
}

function updateEventRegistrations() {
    const registrationsList = document.getElementById('registrationsList');
    const userEmail = localStorage.getItem('userEmail');
    const userEvents = events.filter(event => 
        event.registeredUsers && event.registeredUsers.includes(userEmail)
    );
    
    if (userEvents.length === 0) {
        registrationsList.innerHTML = '<p>No estás inscrito en ningún evento</p>';
        return;
    }
    
    registrationsList.innerHTML = userEvents.map(event => `
        <div class="registered-event">
            <strong>${event.title}</strong> - ${new Date(event.date).toLocaleDateString()}
            ${event.time ? `<br>🕒 ${event.time}` : ''}
            ${event.location ? `<br>📍 ${event.location}` : ''}
            <button class="btn-unregister" onclick="unregisterFromEvent(${event.id})">
                Desinscribirse
            </button>
        </div>
    `).join('');
}

// Administración de usuarios
async function createUser() {
    const email = document.getElementById('newUserEmail').value;
    const password = document.getElementById('newUserPassword').value;
    const type = document.getElementById('newUserType').value;
    
    if (!email || !password) {
        alert('Por favor complete todos los campos');
        return;
    }
    
    try {
        const response = await fetch("http://localhost:3000/api/usuarios", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password,
                tipo: type
            })
        });
        
        const data = await response.json();
        
        if (data.success) {
            document.getElementById('newUserEmail').value = '';
            document.getElementById('newUserPassword').value = '';
            loadUsers();
            alert('Usuario creado exitosamente');
        } else {
            alert('Error al crear usuario: ' + data.message);
        }
    } catch (error) {
        console.error('Error al crear usuario:', error);
        alert('Error de conexión al crear usuario');
    }
}

async function loadUsers() {
    try {
        const response = await fetch("http://localhost:3000/api/usuarios");
        const data = await response.json();
        
        const usersList = document.getElementById('usersList');
        
        if (data.success && data.usuarios.length > 0) {
            usersList.innerHTML = data.usuarios.map(user => `
                <div class="user-item">
                    <strong>${user.email}</strong> - ${user.tipo}
                    <button class="btn-delete" onclick="deleteUser(${user.id})">Eliminar</button>
                </div>
            `).join('');
        } else {
            usersList.innerHTML = '<p>No hay usuarios registrados</p>';
        }
    } catch (error) {
        console.error('Error al cargar usuarios:', error);
        document.getElementById('usersList').innerHTML = '<p>Error al cargar usuarios</p>';
    }
}

async function deleteUser(userId) {
    if (confirm('¿Está seguro de eliminar este usuario?')) {
        try {
            const response = await fetch(`http://localhost:3000/api/usuarios/${userId}`, {
                method: 'DELETE'
            });
            
            const data = await response.json();
            
            if (data.success) {
                loadUsers();
                alert('Usuario eliminado');
            } else {
                alert('Error al eliminar usuario: ' + data.message);
            }
        } catch (error) {
            console.error('Error al eliminar usuario:', error);
            alert('Error de conexión al eliminar usuario');
        }
    }
}

async function deleteEvent(eventId) {
    if (confirm('¿Está seguro de eliminar este evento?')) {
        try {
            const response = await fetch(`http://localhost:3000/api/eventos/${eventId}`, {
                method: 'DELETE'
            });
            
            const data = await response.json();
            
            if (data.success) {
                await loadEventsFromDB();
                updateCalendar();
                alert('Evento eliminado');
            } else {
                alert('Error al eliminar evento: ' + data.message);
            }
        } catch (error) {
            console.error('Error al eliminar evento:', error);
            alert('Error de conexión al eliminar evento');
        }
    }
}

// Inicialización
document.addEventListener('DOMContentLoaded', async function() {
    console.log("Página cargada");
    
    // Verificar autenticación
    if (!localStorage.getItem('isLoggedIn')) {
        window.location.href = 'Login.html';
        return;
    }
    
    // Configurar permisos 
    currentUserType = localStorage.getItem('userType') || 'student';
    
    if (currentUserType !== 'admin') {
        document.getElementById('adminBtn').style.display = 'none';
        document.getElementById('studentRegistrations').style.display = 'block';
    } else {
        document.getElementById('studentRegistrations').style.display = 'none';
    }
    
    // Cargar eventos al iniciar
    await loadEventsFromDB();
    
    // Mostrar home por defecto
    showSection('home');
});

// Función para mostrar contenido de eventos por tipo
async function mostrarContenido(tipo) {
    const contenido = document.getElementById("contenido");

    const tipos = {
        facultativos: 1,       // Conferencia
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