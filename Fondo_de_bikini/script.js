console.log("Archivo de script cargado exitosamente.");
// Referencias a elementos del DOM

// Elementos del juego
const charLeft = document.getElementById('char-left');
const charRight = document.getElementById('char-right');
const speakerName = document.getElementById('speaker-name');
const dialogueText = document.getElementById('dialogue-text');

// Elementos nuevos para el juego
const btnPista = document.getElementById('btn-pista');
const preguntasContainer = document.getElementById('preguntas-container');
const preguntasRestantesSpan = document.getElementById('preguntas-restantes');
const sospechososRestantesSpan = document.getElementById('sospechosos-restantes');

// Elementos de listas
const sospechososList = document.getElementById('sospechosos-list');
const descartadosList = document.getElementById('descartados-list');

// Elementos de informacion
const infoNameValue = document.getElementById('info-name-value');
const infoColorBox = document.getElementById('info-color-box');
const infoImageBox = document.getElementById('info-image-box');

// Constantes y utilidades

// Diccionario para traducir colores a colores web (CSS)
const coloresTraducidos = {
  "amarillo": "#f1c40f", // Color Bob Esponja
  "rosa": "#ff9ff3",     // Color Patricio
  "verde": "#2ecc71",    // Color Calamardo
  "rojo": "#e74c3c"      // Color Don Cangrejo
};

// Funciones para dialogos

// Funcion para actualizar la caja de texto
function mostrarDialogo(nombre, texto) {
  if (!dialogueText) return;
  dialogueText.textContent = texto;

  if (nombre === "" || nombre === "Narrador" || nombre.includes("🔍") || 
      nombre.includes("🎉") || nombre.includes("❌")) {
    speakerName.classList.add('hidden');
  } else {
    speakerName.textContent = nombre;
    speakerName.classList.remove('hidden');
  }
}

// Funcion para mostrar el cuadro del personaje 
function mostrarPersonaje(lado) {
  charLeft.classList.add('hidden');
  charRight.classList.add('hidden');
  charLeft.style.backgroundImage = '';
  charRight.style.backgroundImage = '';

  if (lado === 'izq') {
    charLeft.classList.remove('hidden');
  } else if (lado === 'der') {
    charRight.classList.remove('hidden');
  }
}

// Principal

// Esta funcion actualiza toda la interfaz 
function actualizarUICompleta() {
  if (typeof juego === 'undefined') {
    console.error("El objeto 'juego' no esta definido");
    return;
  }

  //  Actualizar contadores
  if (preguntasRestantesSpan) {
    preguntasRestantesSpan.textContent = 
      `Preguntas: ${juego.configuracion.preguntasMaximas - juego.investigacion.preguntasRealizadas.length}`;
  }
  if (sospechososRestantesSpan) {
    sospechososRestantesSpan.textContent = 
      `Sospechosos: ${juego.investigacion.sospechosos.length}`;
  }

  //  Actualizar panel de preguntas
  if (preguntasContainer) {
    preguntasContainer.innerHTML = '';
    juego.preguntasDisponibles.forEach(pregunta => {
      if (!pregunta.usada) {
        const boton = document.createElement('button');
        boton.className = 'btn-pregunta-juego';
        boton.textContent = pregunta.texto;
        boton.onclick = () => {
          if (typeof hacerPregunta === 'function') {
            hacerPregunta(pregunta.id);
          }
        };
        preguntasContainer.appendChild(boton);
      }
    });
  }

  //  Actualizar lista de sospechosos
  if (sospechososList) {
    sospechososList.innerHTML = '';
    juego.investigacion.sospechosos.forEach(id => {
      const personaje = obtenerPersonajePorId(id);
      if (personaje) {
        const btn = document.createElement('button');
        btn.className = 'btn-sospechoso';
        btn.textContent = personaje.nombre;
        btn.onclick = () => {
          if (typeof interrogarPersonaje === 'function') {
            interrogarPersonaje(id, 'izq');
          }
        };
        sospechososList.appendChild(btn);
      }
    });
  }

  //  Actualizar lista de descartados
  if (descartadosList) {
    descartadosList.innerHTML = '';
    juego.investigacion.descartados.forEach(id => {
      const personaje = obtenerPersonajePorId(id);
      if (personaje) {
        const span = document.createElement('span');
        span.className = 'descartado-nombre';
        span.textContent = personaje.nombre;
        descartadosList.appendChild(span);
      }
    });
  }

  //  Actualizar botón de pista
  if (btnPista) {
    const restantes = juego.configuracion.pistasDisponibles - juego.investigacion.pistasUsadas;
    btnPista.textContent = ` Usar Pista (${restantes} disponibles)`;
    btnPista.disabled = (restantes <= 0);
  }

  //  Actualizar cuadros de personajes
  mostrarPersonaje('ninguno');
  
  if (juego.interfaz.personajeIzquierdo) {
    const personaje = obtenerPersonajePorId(juego.interfaz.personajeIzquierdo);
    if (personaje) {
      mostrarPersonaje('izq');
      
      // Usar colorFalso para modo difícil, colorReal para modo fácil
      if (juego.configuracion.modoDificil && personaje.características?.colorFalso) {
        charLeft.style.backgroundColor = personaje.características.colorFalso;
      } else if (personaje.características?.color) {
        const colorPersonaje = personaje.características.color;
        charLeft.style.backgroundColor = coloresTraducidos[colorPersonaje] || colorPersonaje;
      } else {
        charLeft.style.backgroundColor = "#95a5a6";
      }
      
      // Agregar imagen (futuro)
      if (personaje.imagen && personaje.imagen !== "https://example.com/...") {
        charLeft.style.backgroundImage = `url(${personaje.imagen})`;
        charLeft.style.backgroundSize = 'cover';
        charLeft.style.backgroundPosition = 'center';
      }
    }
  }
  
  if (juego.interfaz.personajeDerecho) {
    const personaje = obtenerPersonajePorId(juego.interfaz.personajeDerecho);
    if (personaje) {
      mostrarPersonaje('der');
      
      if (juego.configuracion.modoDificil && personaje.características?.colorFalso) {
        charRight.style.backgroundColor = personaje.características.colorFalso;
      } else if (personaje.características?.color) {
        const colorPersonaje = personaje.características.color;
        charRight.style.backgroundColor = coloresTraducidos[colorPersonaje] || colorPersonaje;
      } else {
        charRight.style.backgroundColor = "#95a5a6";
      }
      
      if (personaje.imagen && personaje.imagen !== "https://example.com/...") {
        charRight.style.backgroundImage = `url(${personaje.imagen})`;
        charRight.style.backgroundSize = 'cover';
        charRight.style.backgroundPosition = 'center';
      }
    }
  }

  //  Actualizar panel de informacion 
  if (infoNameValue) {
    const personajeSeleccionado = juego.interfaz.personajeSeleccionado ? 
      obtenerPersonajePorId(juego.interfaz.personajeSeleccionado) : null;
    
    if (personajeSeleccionado) {
      infoNameValue.textContent = personajeSeleccionado.nombre;
      
      if (infoColorBox) {
        const color = personajeSeleccionado.características?.color || '#95a5a6';
        infoColorBox.style.backgroundColor = coloresTraducidos[color] || color;
      }
      
      if (infoImageBox) {
        if (personajeSeleccionado.imagen && personajeSeleccionado.imagen !== "https://example.com/...") {
          infoImageBox.style.backgroundImage = `url(${personajeSeleccionado.imagen})`;
          infoImageBox.style.backgroundSize = 'cover';
          infoImageBox.style.backgroundPosition = 'center';
          infoImageBox.textContent = '';
        } else {
          infoImageBox.style.backgroundImage = '';
          infoImageBox.textContent = '';
        }
      }
    } else {
      infoNameValue.textContent = '(elige un sospechoso)';
      if (infoColorBox) infoColorBox.style.backgroundColor = '#95a5a6';
      if (infoImageBox) {
        infoImageBox.style.backgroundImage = '';
        infoImageBox.textContent = '';
      }
    }
  }

  // Actualizar cuadro de dialogo
  if (juego.interfaz.dialogoActual) {
    mostrarDialogo(
      juego.interfaz.dialogoActual.hablante, 
      juego.interfaz.dialogoActual.texto
    );
    
    // Cambiar color del texto 
    dialogueText.className = '';
    if (juego.interfaz.dialogoActual.tipo === 'alerta') {
      dialogueText.classList.add('narrador-alerta');
    } else if (juego.interfaz.dialogoActual.tipo === 'exito') {
      dialogueText.classList.add('narrador-exito');
    } else if (juego.interfaz.dialogoActual.tipo === 'error') {
      dialogueText.classList.add('narrador-error');
    }
    
    // Animacion para respuestas
    if (juego.interfaz.ultimaRespuesta !== null) {
      dialogueText.classList.add('respuesta-recibida');
      setTimeout(() => dialogueText.classList.remove('respuesta-recibida'), 500);
      juego.interfaz.ultimaRespuesta = null;
    }
  }
}

// checar si esta funcion existe y usa personajes correctamente
if (typeof obtenerPersonajePorId === 'undefined') {
  window.obtenerPersonajePorId = function(id) {
    return personajes.find(p => p.id === id);
  };
}

// checar si hacerPregunta actualiza la UI después de ejecutarse
if (typeof hacerPregunta === 'function') {
  const hacerPreguntaOriginal = hacerPregunta;
  window.hacerPregunta = function(preguntaId) {
    const resultado = hacerPreguntaOriginal(preguntaId);
    if (resultado !== false) {
      setTimeout(actualizarUICompleta, 50);
    }
    return resultado;
  };
}

if (typeof usarPista === 'function') {
  const usarPistaOriginal = usarPista;
  window.usarPista = function() {
    const resultado = usarPistaOriginal();
    if (resultado) {
      setTimeout(actualizarUICompleta, 50);
    }
    return resultado;
  };
}

if (typeof interrogarPersonaje === 'function') {
  const interrogarOriginal = interrogarPersonaje;
  window.interrogarPersonaje = function(id, lado) {
    interrogarOriginal(id, lado);
    setTimeout(actualizarUICompleta, 50);
  };
}

// Funcion para acusar a un personaje 
window.acusar = function(id) {
  if (typeof juego === 'undefined') return;
  
  const esCorrecto = (id === juego.misterio.personajeId);
  const personaje = obtenerPersonajePorId(id);
  
  if (!personaje) return;
  
  if (esCorrecto) {
    juego.interfaz.dialogoActual = {
      texto: `¡CORRECTO! ${personaje.nombre} confiesa: "¡Está bien, fui yo! Pero tenía mis razones..."`,
      hablante: "VICTORIA",
      tipo: "exito"
    };
    juego.misterio.resuelto = true;
    
    // Mostrar al culpable en grande
    juego.interfaz.personajeIzquierdo = id;
    juego.interfaz.personajeDerecho = null;
  } else {
    juego.interfaz.dialogoActual = {
      texto: `¡EQUIVOCADO! ${personaje.nombre} se ofende: "¿¡Yo!? ¡Claro que no fui!"`,
      hablante: "FALSO",
      tipo: "error"
    };
  } 
  actualizarUICompleta();
};
// Iniciar el juego

// Funcion para conectar todos los eventos
function inicializarEventos() {
  // Boton de pista
  if (btnPista) {
    btnPista.addEventListener('click', () => {
      if (typeof usarPista === 'function') {
        usarPista();
      }
    });
  }
  const btnAcusar = document.getElementById('btn-acusar');
  if (btnAcusar) {
    // Eliminar event listeners anteriores (por si acaso)
    btnAcusar.replaceWith(btnAcusar.cloneNode(true));
    const nuevoBtnAcusar = document.getElementById('btn-acusar');
    
    nuevoBtnAcusar.addEventListener('click', () => {
      console.log("Boton acusar clickeado");
      console.log("Personaje seleccionado:", juego.interfaz.personajeSeleccionado);
      
      if (!juego.interfaz.personajeSeleccionado) {
        alert(" Primero selecciona un sospechoso de la lista");
        return;
      }
      
      // Llamar a la función acusar que esta en gameState.js
      if (typeof acusar === 'function') {
        acusar(juego.interfaz.personajeSeleccionado);
      } else {
        console.error("Funcion acusar no encontrada");
        alert("Error: Función acusar no disponible");
      }
    });
    
    console.log("Boton de acusar configurado");
  } else {
    console.error("Boton de acusar no encontrado en el DOM");
  }
 

  // Botón de reinicio (pantalla de fin de juego)
  const btnReiniciar = document.getElementById('game-over-restart');
  if (btnReiniciar) {
    btnReiniciar.addEventListener('click', () => {
      if (typeof ocultarGameOver === 'function') {
        ocultarGameOver();
      }
      if (typeof iniciarNuevaPartida === 'function') {
        iniciarNuevaPartida();
      }
    });
  }
  
  console.log("Eventos inicializados");
}

// Función para corregir el objeto juego si es necesario
function corregirEstructuraJuego() {
  if (typeof juego === 'undefined') return;
  
  // Asegurar que caracteristicasReales existe para todos los personajes
  personajes.forEach(p => {
    if (!p.caracteristicasReales) {
      p.caracteristicasReales = p.características || {};
    }
    if (!p.descripcionVisible) {
      p.descripcionVisible = p.descripcion_visible || `Alguien en Fondo de Bikini...`;
    }
  });
  
  // Asegurar que el juego tiene personajeSeleccionado
  if (!juego.interfaz.personajeSeleccionado) {
    juego.interfaz.personajeSeleccionado = null;
  }
}

// Iniciar el juego automáticamente
setTimeout(() => {
  if (typeof iniciarNuevaPartida === 'function') {
    // Corregir estructura antes de iniciar
    corregirEstructuraJuego();
    
    // Iniciar partida
    iniciarNuevaPartida();
    
    // Actualizar UI
    actualizarUICompleta();
    
    // Inicializar eventos
    inicializarEventos();
    
    console.log("Juego iniciado automaticamente");
    console.log("Personaje sin identificar:", obtenerPersonajePorId(juego.misterio.personajeId)?.nombre);
  } else {
    console.error("gameState.js no está cargado correctamente");
  }
}, 200); // Pequeno retraso para asegurar que todo esta cargado

// Exponer funciones globalmente para depuración
window.mostrarPersonaje = mostrarPersonaje;
window.mostrarDialogo = mostrarDialogo;
window.actualizarUICompleta = actualizarUICompleta;

console.log("Script completamente cargado e integrado");