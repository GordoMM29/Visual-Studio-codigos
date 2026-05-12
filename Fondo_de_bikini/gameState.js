// Estado centralizado del juego

console.log("Sistema de juego inicializado");

// Estado inicial del juego

const juego = {
  configuracion: {
    modoDificil: true,
    preguntasMaximas: 15,
    pistasDisponibles: 2
  },

  misterio: {
    personajeId: null,  // Se asignara al azar al iniciar
    resuelto: false
  },

  // Preguntas disponibles
  preguntasDisponibles: [
    { id: 1, texto: "¿El personaje es de color amarillo?", propiedad: "color", valor: "amarillo", usada: false },
    { id: 2, texto: "¿El personaje es de color rosa?", propiedad: "color", valor: "rosa", usada: false },
    { id: 3, texto: "¿El personaje es de color rojo?", propiedad: "color", valor: "rojo", usada: false },
    { id: 4, texto: "¿El personaje es de color verde?", propiedad: "color", valor: "verde", usada: false },
    { id: 5, texto: "¿Vive en una piña?", propiedad: "vivienda", valor: "piña", usada: false },
    { id: 6, texto: "¿Vive bajo una roca?", propiedad: "vivienda", valor: "roca", usada: false },
    { id: 7, texto: "¿Trabaja en el Crustaceo Cascarudo?", propiedad: "trabajo", valor: "cocinero", usada: false },
    { id: 8, texto: "¿Toca un instrumento musical?", propiedad: "instrumento", valor: true, usada: false },
    { id: 9, texto: "¿Es un superheroe?", propiedad: "superheroe", valor: true, usada: false },
    { id: 10, texto: "¿Es una chica?", propiedad: "genero", valor: "femenino", usada: false },
    { id: 11, texto: "¿Tiene más de 30 años?", propiedad: "edad", valor: 30, comparacion: "mayor", usada: false },
    { id: 12, texto: "¿Es una mascota?", propiedad: "especie", valor: "caracol", usada: false }
  ],

  // Estado de la investigacion
  investigacion: {
    preguntasRealizadas: [],      // IDs de preguntas hechas
    respuestasObtenidas: [],      // Objetos con {preguntaId, respuesta, personajesDescartados}
    sospechosos: [],              // IDs de personajes que siguen en juego
    descartados: [],              // IDs de personajes descartados
    pistasUsadas: 0
  },

  // Control de la interfaz
  interfaz: {
    personajeIzquierdo: null,     // ID del personaje mostrado a la izquierda
    personajeDerecho: null,       // ID del personaje mostrado a la derecha
    personajeSeleccionado: null,  // ID del personaje seleccionado (para el recuadro de color/imagen)
    dialogoActual: {
      texto: "¡Alguien robo la formula secreta de la Cangreburger! ¿Quien fue?",
      hablante: "Narrador",
      tipo: "normal"              // normal, alerta, exito, error
    },
    ultimaRespuesta: null         // Para animaciones
  }
};

// Funciones

// Funcion para obtener un personaje por su ID
function obtenerPersonajePorId(id) {
  return personajes.find(p => p.id === id);
}

// Gestion del juego

// Llama a la UI correcta segun este definida en el proyecto
function actualizarUIAuto() {
  if (typeof actualizarUICompleta === 'function') {
    actualizarUICompleta();
  } else if (typeof actualizarUI === 'function') {
    actualizarUI();
  }
}

// Funcion para iniciar una nueva partida
function iniciarNuevaPartida() {
  console.log("Iniciando nueva partida...");
  if (typeof ocultarGameOver === 'function') {
    ocultarGameOver();
  }

  // Seleccionar personaje misterioso al azar
  const indiceAleatorio = Math.floor(Math.random() * personajes.length);
  juego.misterio.personajeId = personajes[indiceAleatorio].id;

  // Todos los personajes comienzan como sospechosos
  juego.investigacion.sospechosos = personajes.map(p => p.id);
  juego.investigacion.descartados = [];

  // Reiniciar preguntas
  juego.preguntasDisponibles.forEach(p => p.usada = false);
  juego.investigacion.preguntasRealizadas = [];
  juego.investigacion.respuestasObtenidas = [];

  // Reiniciar pistas
  juego.configuracion.pistasDisponibles = 2;
  juego.investigacion.pistasUsadas = 0;

  // Mensaje de bienvenida
  juego.interfaz.dialogoActual = {
    texto: `¡Alarma en Fondo de Bikini! Alguien robo la formula secreta. Hay ${personajes.length} sospechosos. ¿Quien sera?`,
    hablante: "Narrador",
    tipo: "alerta"
  };

  // Limpiar personajes en pantalla
  juego.interfaz.personajeIzquierdo = null;
  juego.interfaz.personajeDerecho = null;
  juego.interfaz.personajeSeleccionado = null;

  console.log("Personaje misterioso:", obtenerPersonajePorId(juego.misterio.personajeId).nombre);
  actualizarUIAuto();
  return juego;
}


// Funcion para procesar una pregunta
function hacerPregunta(preguntaId) {
  // Buscar la pregunta
  const pregunta = juego.preguntasDisponibles.find(p => p.id === preguntaId);

  if (!pregunta || pregunta.usada) {
    console.log("Pregunta no disponible");
    return false;
  }

  if (juego.investigacion.preguntasRealizadas.length >= juego.configuracion.preguntasMaximas) {
    juego.interfaz.dialogoActual = {
      texto: "¡Has gastado todas tus preguntas! Debes hacer una acusacion ahora.",
      hablante: "Narrador",
      tipo: "error"
    };
    actualizarUIAuto();
    return false;
  }

  // Obtener el personaje misterioso
  const misterioso = obtenerPersonajePorId(juego.misterio.personajeId);

  // Evaluar la respuesta según la pregunta
  let respuesta = false;
  let respuestaTexto = "";

  switch(pregunta.propiedad) {
    case "color":
      respuesta = (misterioso.caracteristicasReales.colorReal === pregunta.valor);
      respuestaTexto = respuesta ? "¡SÍ! El personaje es de ese color." : "NO. No es de ese color.";
      break;

    case "genero":
      respuesta = (misterioso.caracteristicasReales.genero === pregunta.valor);
      respuestaTexto = respuesta ? "Si, es del genero que preguntas." : "No, no es de ese genero.";
      break;

    case "edad":
      if (pregunta.comparacion === "mayor") {
        respuesta = (misterioso.caracteristicasReales.edad >= pregunta.valor);
        respuestaTexto = respuesta ? `Si, tiene mas de ${pregunta.valor} anos.` : `No, es mas joven que ${pregunta.valor}.`;
      }
      break;

    default:
      // Para propiedades booleanas o especificas
      respuesta = (misterioso.caracteristicasReales[pregunta.propiedad] === pregunta.valor);
      respuestaTexto = respuesta ? "Afirmativo." : "Negativo.";
  }

  // Marcar pregunta como usada
  pregunta.usada = true;
  juego.investigacion.preguntasRealizadas.push(preguntaId);

  // Guardar la respuesta
  juego.investigacion.respuestasObtenidas.push({
    preguntaId: preguntaId,
    respuesta: respuesta,
    respuestaTexto: respuestaTexto
  });

  // Actualizar lista de sospechosos según la respuesta
  actualizarSospechososPorRespuesta(pregunta, respuesta);

  // Mostrar respuesta en el diálogo
  juego.interfaz.dialogoActual = {
    texto: respuestaTexto,
    hablante: "Narrador",
    tipo: respuesta ? "exito" : "normal"
  };

  // Guardar para animación
  juego.interfaz.ultimaRespuesta = respuesta;

  actualizarUIAuto();
  return true;
}

// Funcion para actualizar sospechosos basado en la respuesta
function actualizarSospechososPorRespuesta(pregunta, respuesta) {
  const nuevosSospechosos = [];
  const nuevosDescartados = [];

  // Evaluar cada sospechoso actual
  juego.investigacion.sospechosos.forEach(id => {
    const personaje = obtenerPersonajePorId(id);
    let cumpleCondicion = false;

    switch(pregunta.propiedad) {
      case "color":
        cumpleCondicion = (personaje.caracteristicasReales.colorReal === pregunta.valor);
        break;
      case "genero":
        cumpleCondicion = (personaje.caracteristicasReales.genero === pregunta.valor);
        break;
      case "edad":
        if (pregunta.comparacion === "mayor") {
          cumpleCondicion = (personaje.caracteristicasReales.edad >= pregunta.valor);
        }
        break;
      default:
        cumpleCondicion = (personaje.caracteristicasReales[pregunta.propiedad] === pregunta.valor);
    }

    // Si la respuesta es Si, deben cumplir la condición; si es NO, deben NO cumplirla
    if (respuesta) {
      if (cumpleCondicion) {
        nuevosSospechosos.push(id);
      } else {
        nuevosDescartados.push(id);
      }
    } else {
      if (!cumpleCondicion) {
        nuevosSospechosos.push(id);
      } else {
        nuevosDescartados.push(id);
      }
    }
  });

  juego.investigacion.sospechosos = nuevosSospechosos;
  juego.investigacion.descartados = [...juego.investigacion.descartados, ...nuevosDescartados];

  // Verificar si ya solo queda un sospechoso
  if (juego.investigacion.sospechosos.length === 1) {
    juego.interfaz.dialogoActual = {
      texto: "¡Solo queda un sospechoso! Es hora de acusar.",
      hablante: "Narrador",
      tipo: "alerta"
    };
  }
}

// Acciones del jugador

// Funcion para usar una pista
function usarPista() {
  if (juego.configuracion.pistasDisponibles <= juego.investigacion.pistasUsadas) {
    return false;
  }

  const misterioso = obtenerPersonajePorId(juego.misterio.personajeId);
  const pistas = [
    `Pista 1: ${misterioso.descripcionVisible.split('.')[0]}...`,
    `Pista 2: Su color real es el ${misterioso.caracteristicasReales.colorReal}`,
    `Pista 3: ${misterioso.dialogos.confundido}`
  ];

  const pistaIndex = juego.investigacion.pistasUsadas;
  juego.investigacion.pistasUsadas++;

  juego.interfaz.dialogoActual = {
    texto: pistas[pistaIndex],
    hablante: " Pista secreta",
    tipo: "alerta"
  };

  actualizarUIAuto();
  return true;
}

// Funcion para interrogar a un personaje especifico
function interrogarPersonaje(id, lado) {
  const personaje = obtenerPersonajePorId(id);

  if (!personaje) return;

  // Seleccionar un dialogo aleatorio del personaje
  const dialogos = personaje.dialogos;
  const tiposDialogo = Object.keys(dialogos);
  const tipoElegido = tiposDialogo[Math.floor(Math.random() * tiposDialogo.length)];
  const dialogo = dialogos[tipoElegido];

  // Actualizar interfaz
  if (lado === 'izq') {
    juego.interfaz.personajeIzquierdo = id;
  } else {
    juego.interfaz.personajeDerecho = id;
  }

  // Marcar como seleccionado para el recuadro de color / imagen
  juego.interfaz.personajeSeleccionado = id;

  juego.interfaz.dialogoActual = {
    texto: dialogo,
    hablante: juego.configuracion.modoDificil ? "???" : personaje.nombre,
    tipo: "normal"
  };

  actualizarUIAuto();
}

// Funcion para acusar a un personaje
function acusar(id) {
  const esCorrecto = (id === juego.misterio.personajeId);

  if (esCorrecto) {
    const personaje = obtenerPersonajePorId(id);
    juego.interfaz.dialogoActual = {
      texto: `¡CORRECTO! ${personaje.nombre} confiesa: "¡Esta bien, fui yo! Pero tenia mis razones..."`,
      hablante: "VICTORIA",
      tipo: "exito"
    };
    juego.misterio.resuelto = true;
    mostrarGameOver(`¡Felicitaciones! Acertaste. El culpable era ${personaje.nombre}.`, 'victoria');
  } else {
    const falso = obtenerPersonajePorId(id);
    juego.interfaz.dialogoActual = {
      texto: `¡EQUIVOCADO! ${falso.nombre} se ofende: "¿¡Yo!? ¡Claro que no fui!" Has perdido una oportunidad.`,
      hablante: "FALSO",
      tipo: "error"
    };
    mostrarGameOver(`¡Has perdido! El culpable era ${falso.nombre}.`, 'derrota');
  }

  actualizarUIAuto();
}

// Interfaz y actualizacion

function mostrarGameOver(mensaje, tipo) {
  console.log("Mostrando Game Over:", tipo, mensaje);
  
  const panel = document.getElementById('game-over');
  const title = document.getElementById('game-over-title');
  const msg = document.getElementById('game-over-message');

  if (!panel) {
    console.error("Panel game-over no encontrado en el DOM");
    return;
  }
  if (!title) {
    console.error("Elemento game-over-title no encontrado");
    return;
  }
  if (!msg) {
    console.error("Elemento game-over-message no encontrado");
    return;
  }

  // Cambiar el titulo segun el tipo
  if (tipo === 'victoria') {
    title.textContent = ' ¡GANASTE!';
  } else {
    title.textContent = ' PERDISTE';
  }
  
  // Establecer el mensaje
  msg.textContent = mensaje;
  
  // Quitar la clase hidden para que aparezca
  panel.classList.remove('hidden');
  
  // Forzar estilo por si acaso
  panel.style.display = 'flex';
  
  console.log("Panel de Game Over visible");
  console.log("Clases del panel:", panel.className);
}

function ocultarGameOver() {
  console.log("Ocultando Game Over");
  
  const panel = document.getElementById('game-over');
  if (panel) {
    panel.classList.add('hidden');
    panel.style.display = '';
    console.log("Panel ocultado");
  }
}

// Funcion para actualizar toda la interfaz
function actualizarUI() {
  // Actualizar contadores
  document.getElementById('preguntas-restantes').textContent =
    `Preguntas: ${juego.configuracion.preguntasMaximas - juego.investigacion.preguntasRealizadas.length}`;
  document.getElementById('sospechosos-restantes').textContent =
    `Sospechosos: ${juego.investigacion.sospechosos.length}`;

  // Actualizar panel de preguntas
  const preguntasContainer = document.getElementById('preguntas-container');
  if (preguntasContainer) {
    preguntasContainer.innerHTML = '';
    juego.preguntasDisponibles.forEach(pregunta => {
      if (!pregunta.usada) {
        const boton = document.createElement('button');
        boton.className = 'btn-pregunta-juego';
        boton.textContent = pregunta.texto;
        boton.onclick = () => hacerPregunta(pregunta.id);
        preguntasContainer.appendChild(boton);
      }
    });
  }

  // Actualizar lista de sospechosos
  const sospechososList = document.getElementById('sospechosos-list');
  if (sospechososList) {
    sospechososList.innerHTML = '';
    juego.investigacion.sospechosos.forEach((id) => {
      const personaje = obtenerPersonajePorId(id);
      const btn = document.createElement('button');
      btn.className = 'btn-sospechoso';
      btn.textContent = personaje.nombre;
      btn.title = 'Haz clic para interrogar';
      btn.onclick = () => interrogarPersonaje(id, 'izq');
      sospechososList.appendChild(btn);
    });
  }

  // Actualizar lista de descartados (solo nombres)
  const descartadosList = document.getElementById('descartados-list');
  if (descartadosList) {
    descartadosList.innerHTML = '';
    juego.investigacion.descartados.forEach((id) => {
      const personaje = obtenerPersonajePorId(id);
      const span = document.createElement('span');
      span.className = 'descartado-nombre';
      span.textContent = personaje.nombre;
      descartadosList.appendChild(span);
    });
  }

  // Actualizar boton de pista
  const btnPista = document.getElementById('btn-pista');
  if (btnPista) {
    const restantes = juego.configuracion.pistasDisponibles - juego.investigacion.pistasUsadas;
    btnPista.textContent = `Usar Pista (${restantes} disponibles)`;
    btnPista.disabled = (restantes <= 0);
  }

  // Actualizar cuadros de personajes
  const charLeft = document.getElementById('char-left');
  const charRight = document.getElementById('char-right');

  // Limpiar clases y estilos
  charLeft.classList.add('hidden');
  charLeft.style.backgroundImage = '';
  charRight.classList.add('hidden');
  charRight.style.backgroundImage = '';

  // Mostrar personaje izquierdo si existe
  if (juego.interfaz.personajeIzquierdo) {
    const personaje = obtenerPersonajePorId(juego.interfaz.personajeIzquierdo);
    charLeft.classList.remove('hidden');
    // Usar colorFalso para el modo difícil
    charLeft.style.backgroundColor = juego.configuracion.modoDificil ?
      personaje.caracteristicasReales.colorFalso || "#95a5a6" :
      personaje.caracteristicasReales.colorReal || "#95a5a6";
    // Agregar imagen si existe
    if (personaje.imagen) {
      charLeft.style.backgroundImage = `url(${personaje.imagen})`;
      charLeft.style.backgroundSize = 'cover';
      charLeft.style.backgroundPosition = 'center';
    }
  }

  // Mostrar personaje derecho si existe
  if (juego.interfaz.personajeDerecho) {
    const personaje = obtenerPersonajePorId(juego.interfaz.personajeDerecho);
    charRight.classList.remove('hidden');
    charRight.style.backgroundColor = juego.configuracion.modoDificil ?
      personaje.caracteristicasReales.colorFalso || "#95a5a6" :
      personaje.caracteristicasReales.colorReal || "#95a5a6";
    // Agregar imagen si existe
    if (personaje.imagen) {
      charRight.style.backgroundImage = `url(${personaje.imagen})`;
      charRight.style.backgroundSize = 'cover';
      charRight.style.backgroundPosition = 'center';
    }
  }

  // Actualizar cuadro de diálogo
  const speakerName = document.getElementById('speaker-name');
  const dialogueText = document.getElementById('dialogue-text');

  // Actualizar panel de personaje seleccionado (color + imagen)
  const infoNameValue = document.getElementById('info-name-value');
  const infoColorBox = document.getElementById('info-color-box');
  const infoImageBox = document.getElementById('info-image-box');
  const personajeSeleccionado = obtenerPersonajePorId(juego.interfaz.personajeSeleccionado);

  if (infoNameValue) {
    infoNameValue.textContent = personajeSeleccionado ? personajeSeleccionado.nombre : '(elige un sospechoso)';
  }

  if (infoColorBox) {
    const color = personajeSeleccionado?.caracteristicasReales?.colorReal || '#95a5a6';
    infoColorBox.style.backgroundColor = color;
  }

  if (infoImageBox) {
    if (personajeSeleccionado?.imagen) {
      infoImageBox.style.backgroundImage = `url(${personajeSeleccionado.imagen})`;
      infoImageBox.style.backgroundSize = 'cover';
      infoImageBox.style.backgroundPosition = 'center';
      infoImageBox.textContent = '';
    } else {
      infoImageBox.style.backgroundImage = '';
      infoImageBox.textContent = '(próximamente)';
    }
  }

  if (juego.interfaz.dialogoActual.hablante === "Narrador" ||
      juego.interfaz.dialogoActual.hablante.includes("") ||
      juego.interfaz.dialogoActual.hablante.includes("") ||
      juego.interfaz.dialogoActual.hablante.includes("Pista")) {
    speakerName.classList.add('hidden');
  } else {
    speakerName.textContent = juego.interfaz.dialogoActual.hablante;
    speakerName.classList.remove('hidden');
  }

  dialogueText.textContent = juego.interfaz.dialogoActual.texto;

  // Anadir clase para animación si hay respuesta reciente
  if (juego.interfaz.ultimaRespuesta !== null) {
    dialogueText.classList.add('respuesta-recibida');
    setTimeout(() => dialogueText.classList.remove('respuesta-recibida'), 500);
    juego.interfaz.ultimaRespuesta = null;
  }

  // Cambiar color del texto según tipo
  dialogueText.className = '';
  if (juego.interfaz.dialogoActual.tipo === 'alerta') {
    dialogueText.classList.add('narrador-alerta');
  } else if (juego.interfaz.dialogoActual.tipo === 'exito') {
    dialogueText.classList.add('narrador-exito');
  } else if (juego.interfaz.dialogoActual.tipo === 'error') {
    dialogueText.classList.add('narrador-error');
  }
}