console.log("✅ login_script.js cargado");

// Cargar imagen guardada al iniciar
document.addEventListener('DOMContentLoaded', function() {
    loadSavedImage();
});

function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const userType = document.getElementById('userType').value;
    const message = document.getElementById('message');

    // Reset mensaje
    message.className = 'message-container';
    message.innerHTML = '';

    if (!email || !password || !userType) {
        showMessage('Por favor complete todos los campos', 'error');
        return;
    }

    // Validar formato email institucional
    if (!isValidInstitutionalEmail(email)) {
        showMessage('Por favor use un correo institucional válido (@uadec.edu.mx)', 'error');
        return;
    }

    // Validar contraseña
    if (password.length < 4) {
        showMessage('La contraseña debe tener al menos 4 caracteres', 'error');
        return;
    }

    // Simular carga
    const loginBtn = document.querySelector('.login-btn');
    const originalText = loginBtn.innerHTML;
    loginBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verificando...';
    loginBtn.disabled = true;

    // Simular verificación (2 seg)
    setTimeout(() => {
        // Guardar datos de sesión
        localStorage.setItem('userEmail', email);
        localStorage.setItem('userType', userType);
        localStorage.setItem('isLoggedIn', 'true');
        
        showMessage('¡Acceso exitoso! Redirigiendo...', 'success');
        
        // Redirigir después 1 segundo
        setTimeout(() => {
            window.location.href = 'main_windows.html';
        }, 1000);
        
    }, 2000);
}

function isValidInstitutionalEmail(email) {
    // Permitir cualquier email para pruebas, pero validar formato básico
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function showMessage(text, type) {
    const message = document.getElementById('message');
    message.innerHTML = text;
    message.className = `message-container ${type}`;
}

function togglePassword() {
    const passwordInput = document.getElementById('password');
    const eyeIcon = document.getElementById('eyeIcon');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        eyeIcon.className = 'fas fa-eye-slash';
    } else {
        passwordInput.type = 'password';
        eyeIcon.className = 'fas fa-eye';
    }
}

// Funciones para carga de imagen
function previewImage(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('imagePreview');
    
    if (file) {
        // Validar tipo de archivo
        if (!file.type.match('image.*')) {
            showMessage('Por favor seleccione un archivo de imagen válido', 'error');
            return;
        }
        
        // Validar tamaño (2MB max)
        if (file.size > 2 * 1024 * 1024) {
            showMessage('La imagen debe ser menor a 2MB', 'error');
            return;
        }
        
        const reader = new FileReader();
        
        reader.onload = function(e) {
            preview.innerHTML = `<img src="${e.target.result}" alt="Vista previa">`;
            
            // Guardar imagen en localStorage
            localStorage.setItem('customLogo', e.target.result);
            
            // Actualizar logo principal
            document.getElementById('logoImage').src = e.target.result;
            
            showMessage('Logo actualizado correctamente', 'success');
        }
        
        reader.readAsDataURL(file);
    }
}

function loadSavedImage() {
    const savedImage = localStorage.getItem('customLogo');
    if (savedImage) {
        document.getElementById('logoImage').src = savedImage;
        document.getElementById('imagePreview').innerHTML = `<img src="${savedImage}" alt="Logo guardado">`;
    }
}

function removeImage() {
    localStorage.removeItem('customLogo');
    document.getElementById('logoImage').src = 'logo.png';
    document.getElementById('imagePreview').innerHTML = '';
    document.getElementById('imageUpload').value = '';
    showMessage('Logo restaurado al predeterminado', 'success');
}

// Soporte para tecla Enter
document.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        login();
    }
});

// Efectos visuales mejorados
document.querySelectorAll('.form-input').forEach(input => {
    input.addEventListener('focus', function() {
        this.parentElement.classList.add('focused');
    });
    
    input.addEventListener('blur', function() {
        if (!this.value) {
            this.parentElement.classList.remove('focused');
        }
    });
});

function toggleRegister() {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const registerLink = document.getElementById('registerLink');
    const message = document.getElementById('message');
    
    // Limpiar mensajes
    message.className = 'message-container';
    message.innerHTML = '';
    
    if (registerForm.style.display === 'none') {
        // Mostrar registro
        loginForm.style.display = 'none';
        registerForm.style.display = 'block';
        registerLink.style.display = 'none';
        document.querySelector('.image-upload-section').style.display = 'none';
    } else {
        // Mostrar login
        loginForm.style.display = 'block';
        registerForm.style.display = 'none';
        registerLink.style.display = 'inline';
        document.querySelector('.image-upload-section').style.display = 'block';
    }
}

function toggleOrganizationField() {
    const role = document.getElementById('regRole').value;
    const organizationField = document.getElementById('organizationField');
    
    // Mostrar organización solo si es organizador
    if (role === 'organizer') {
        organizationField.style.display = 'block';
        document.getElementById('regOrganization').required = true;
    } else {
        organizationField.style.display = 'none';
        document.getElementById('regOrganization').required = false;
    }
}

async function registerUser() {
    const name = document.getElementById('regName').value;
    const email = document.getElementById('regEmail').value;
    const phone = document.getElementById('regPhone').value;
    const password = document.getElementById('regPassword').value;
    const role = document.getElementById('regRole').value;
    const organization = document.getElementById('regOrganization').value;
    const message = document.getElementById('message');

    // Reset mensaje
    message.className = 'message-container';
    message.innerHTML = '';

    // Validaciones
    if (!name || !email || !password || !role) {
        showMessage('Por favor complete todos los campos obligatorios', 'error');
        return;
    }

    if (password.length < 6) {
        showMessage('La contraseña debe tener al menos 6 caracteres', 'error');
        return;
    }

    if (role === 'organizer' && !organization) {
        showMessage('Los organizadores deben especificar su organización', 'error');
        return;
    }

    // Determinar si es organizador basado en el rol
    const es_organizador = role === 'organizer';
    
    // Determinar el rol para la BD (mapeo de roles)
    const rol_bd = role === 'admin' ? 'admin' : 
                  role === 'organizer' ? 'organizador' : 'participante';

    try {
        // Enviar datos a la API para crear usuario en BD
        const response = await fetch("http://localhost:3000/api/usuarios", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nombre: name,
                email: email,
                telefono: phone,
                usuario: email.split('@')[0], // Generar usuario desde email
                password_hash: password, // En un caso real esto debería estar encriptado
                es_organizador: es_organizador,
                organizacion: es_organizador ? organization : null,
                rol: rol_bd
            })
        });

        const data = await response.json();

        if (data.success) {
            showMessage('¡Cuenta creada exitosamente! Ya puedes iniciar sesión', 'success');

            // Limpiar formulario
            document.getElementById('regName').value = '';
            document.getElementById('regEmail').value = '';
            document.getElementById('regPhone').value = '';
            document.getElementById('regPassword').value = '';
            document.getElementById('regRole').value = '';
            document.getElementById('regOrganization').value = '';
            document.getElementById('organizationField').style.display = 'none';

            // Volver al login después de 2 segundos
            setTimeout(() => {
                toggleRegister();
            }, 2000);
        } else {
            showMessage('Error al crear cuenta: ' + data.message, 'error');
        }
    } catch (error) {
        console.error('Error al registrar usuario:', error);
        showMessage('Error de conexión al crear cuenta', 'error');
    }
}

// Función de login modificada para conectar con BD
async function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const userType = document.getElementById('userType').value;
    const message = document.getElementById('message');

    // Reset mensaje
    message.className = 'message-container';
    message.innerHTML = '';

    if (!email || !password || !userType) {
        showMessage('Por favor complete todos los campos', 'error');
        return;
    }

    // Validar formato email
    if (!isValidEmail(email)) {
        showMessage('Por favor use un correo electrónico válido', 'error');
        return;
    }

    // Validar contraseña
    if (password.length < 4) {
        showMessage('La contraseña debe tener al menos 4 caracteres', 'error');
        return;
    }

    // Simular carga
    const loginBtn = document.querySelector('.login-btn');
    const originalText = loginBtn.innerHTML;
    loginBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verificando...';
    loginBtn.disabled = true;

    try {
        // Enviar credenciales a la API
        const response = await fetch("http://localhost:3000/api/auth/login", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password,
                userType: userType
            })
        });

        const data = await response.json();

        if (data.success) {
            // Guardar datos de sesión
            localStorage.setItem('userEmail', email);
            localStorage.setItem('userType', userType);
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('userId', data.userId);
            localStorage.setItem('userName', data.userName);
            
            showMessage('¡Acceso exitoso! Redirigiendo...', 'success');
            
            // Redirigir después 1 segundo
            setTimeout(() => {
                window.location.href = 'main_windows.html';
            }, 1000);
        } else {
            showMessage('Credenciales incorrectas: ' + data.message, 'error');
            loginBtn.innerHTML = originalText;
            loginBtn.disabled = false;
        }
        
    } catch (error) {
        console.error('Error en login:', error);
        showMessage('Error de conexión. Usando modo demo...', 'warning');
        
        // Fallback: usar sistema demo si la API falla
        setTimeout(() => {
            localStorage.setItem('userEmail', email);
            localStorage.setItem('userType', userType);
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('userId', 'demo-' + Date.now());
            localStorage.setItem('userName', 'Usuario Demo');
            
            showMessage('¡Acceso en modo demo! Redirigiendo...', 'success');
            
            setTimeout(() => {
                window.location.href = 'main_windows.html';
            }, 1000);
        }, 1000);
    }
}

// Función de validación de email más genérica
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}