console.log("Archivo de personajes cargado exitosamente.");
const personajes = [
    {
        id: 1,
        nombre: "Bob Esponja",
        descripcion_visible: " Un animal acuatico que tiene una mascota mas lenta que el tiempo",
        descripcion_real: "Una esponja de mar en pantalones cuadrados, alegre y entusiasta",
        imagen: "https://example.com/bob-esponja.png", // Reemplaza con URL real
        características: { genero: "masculino", edad: 20, color: "amarillo", trabajo: "cocinero" },
        dialogos: {
            neutro : "Yo?, si solo estaba preparando algunas cangreburgers",
            defensivo : "Pero si yo y patricio estabamos atrapando medusas",
            confundido : "¿Que? No entiendo, solo estaba haciendo mi trabajo"
        }
    },
    {
        id: 2,
        nombre: "Patricio Estrella",
        descripcion_visible: "Un asteroidea rosa que puede estar sobre una roca  en todo el Fondo",
        descripcion_real: "Un estrella de mar que es el mejor amigo de Bob Esponja",
        imagen: "https://example.com/patricio.png", // Reemplaza con URL real
        características: { genero: "masculino", edad: 20, color: "rosa", amigo: true },
        dialogos: {
            neutro : "¿Bob? No se donde esta, yo solo estaba jugando con las medusas",
            defensivo : "Nose de que hablas yo estaba en mi casa comiendo cangreburgers",
            confundido : "Espera, donde se supone que deberia de estar?"
        }
    },
    {
        id: 3,
        nombre: "Calamardo Tentaculos",
        descripcion_visible: "Alguein de poco talento con gran experiencia en distintos campos",
        descripcion_real: "Un calamar que toca el clarinete pero siempre esta de mal humor",
        imagen: "https://example.com/calamardo.png", // Reemplaza con URL real
        características: { genero: "masculino", edad: 30, color: "verde", instrumento: "clarinete" },
        dialogos: {
            neutro : "Disculpa?, solo estaba tocando mi clarinete",
            defensivo : "No recuerdo nada de eso solo estyaba aprendiendo una nueva cancion",
            confundido : "Solo recuerdo mi nueva cancion como podre recordar donde estaba?"
        }
    },
    {
        id: 4,
        nombre: "Don Cangrejo",
        descripcion_visible: "Un animal codisioso que simpre busca por el y familia",
        descripcion_real: "Un cangrejo codicioso que es el dueño del Crustaceo Cascarudo",
        imagen: "https://example.com/don-cangrejo.png", 
        características: { genero: "masculino", edad: 70, color: "rojo", rico: true },
        dialogos: {
            neutro: "Estaba en el custraceo cascarudo contando mis billetes",
            defensivo: "Como podre robar mi propia formula, como te atreves",
            confundido: "Plankton, seguro fue otro de sus intentos para robarla"
        }
    },
    {
        id: 5,
        nombre: "Arenita Mejillas",
        descripcion_visible: "Alguien talentosa con grandes habilidades dentro y fuera",
        descripcion_real: "Una ardilla que es maestra de karate y vive bajo el agua en una cupula de aire",
        imagen: "https://example.com/arenita.png", 
        características: { genero: "femenino", edad: 23, especie: "ardilla", karate: true },
        dialogos: {
            neutro: "Estaba haciendo otro experimento junto con Bob",
            defensivo: "Si lo hubiera echo creeme que lo hubiera echo desde hace mucho tiempo",
            confundido: "Con mi karate no podria a llegar a hacer todo este desastre"
        }
    },
    {
        id: 6,
        nombre: "Plankton",
        descripcion_visible: "Alguien de pequena maldad pero grandes ideas sobre como cometer crimenes",
        descripcion_real: "Un pequeño villano con planes para robar la fórmula secreta",
        imagen: "https://example.com/plankton.png", 
        características: { genero: "masculino", edad: 35, color: "verde", dueño: "Chum Bucket" },
        dialogos: {
            neutro: "podria robar la formula si pudiera con solo mis manos",
            defensivo: "Te estoy diciendo que esta vez no fui yo",
            confundido: "Karen estaba conmigo en el laboratorio"
        }
    },
    {
        id: 7,
        nombre: "Gary",
        descripcion_visible: "Alguien muy sabio pero de pocas palabras",
        descripcion_real: "Un caracol que es la mascota de Bob Esponja y solo dice 'miau'",
        imagen: "https://example.com/gary.png", 
        características: { genero: "masculino", especie: "caracol", sonido: "miau" },
        dialogos: {
            neutro: "Miau",
            defensivo: "Miau >:/",
            confundido: "Miau?"
        }
    },
    {
        id: 8,
        nombre: "Senora Puff",
        descripcion_visible: "Alguien de caracter unico pero con un gran problema",
        descripcion_real: "Una maestra de conducir que se infla cuando se enoja",
        imagen: "https://example.com/senora-puff.png", 
        características: { genero: "femenino", especie: "pez globo", profesión: "instructora" },
        dialogos: {
            neutro: "otra vez Bob Esponja intentando sacar un permiso para conducir?",
            defensivo: "Si tengo clases de manejo todos los dias no podria ir al custraceo",
            confundido: "Podria se alguien que si tenga un permiso par conducir"
        }
    },
    {
        id: 9,
        nombre: "Larry el Langosta",
        descripcion_visible: "El mejor en el ambito de la musculacion",
        descripcion_real: "Un habitante musculoso y atlético de Fondo de Bikini",
        imagen: "https://example.com/larry-langosta.png", 
        características: { genero: "masculino", especie: "langosta", musculoso: true },
        dialogos: {
            neutro: "Si la cangreburguer fuera mas saludable compraria una diaria",
            defensivo: "Estaba ayudando a la gente en la playa mientras hacia ejercicio no tengo tiempo para esto",
            confundido: "El ejercicio es lo que mantiene no requiero de una formula"
        }
    },
    {
        id: 10,
        nombre: "Sireno Man",
        descripcion_visible: " A;lguien retirado pero con la misma sabiduria de siempre",
        descripcion_real: "Un héroe de la vejez que vive en una casa submarina",
        imagen: "https://example.com/sireno-man.png", 
        características: { genero: "masculino", edad: 90, superhéroe: true },
        dialogos: {
            neutro: "ESTO ES UN TRABAJO PARA SIRENO MAN Y CHICO PERCEBE!!!",
            defensivo: "Chico percebe sabes donde estabamos aquel dia?",
            confundido: "Que dices?, que la formula fue robada?"
        }
    },
    {
        id: 11,
        nombre: "El Chico Percebe",
        descripcion_visible: "Alguien cansado pero apoyando siempre a su companero",
        descripcion_real: "El ayudante joven de Sireno Man",
        imagen: "https://example.com/chico-percebe.png", 
        características: { genero: "masculino", edad: 20, superhéroe: true },
        dialogos: {
            neutro: "no solemos comer de esas cangreburgers aqui en el asilo",
            defensivo: "Tuve mi tiempo de villano pero no subi tanto como criminal",
            confundido: "No tenemos para encargarnos de esto yo y sireno man"
        }
    },
    {
        id: 12,
        nombre: "Perla Cangrejo",
        descripcion_visible: "Una chica que tiene muchas exigencias pero con un gran corazon",
        descripcion_real: "La hija adolescente de Don Cangrejo, una ballena",
        imagen: "https://example.com/perla.png", 
        características: { genero: "femenino", especie: "ballena", adolescente: true },
        dialogos: {
            neutro: "Mi papi tiene siempre resguardada la formula en su boveda",
            defensivo: "No robaria la formula de mi Papi no es de chicas cool",
            confundido: "Si mi papip tiene el dinero para investigar el caso lo hara todo por su preciada formula"
        }
    },
    {
        id: 13,
        nombre: "Karen",
        descripcion_visible: "Alguien inteligente que siempre tendra la razon pero ama a su pareja",
        descripcion_real: "La computadora esposa de Plankton",
        imagen: "https://example.com/karen.png", 
        características: { genero: "femenino", tipo: "computadora", inteligente: true },
        dialogos: {
            neutro: "Estoy analizando la situación desde mi punto de vista",
            defensivo: "Plankton tiene planes tan poco eficientes que no me incluyen es este",
            confundido: "Intentare buscar alguna pista pero no podria descubrir la verdad"
        }
    },
    {
        id: 14,
        nombre: "El Holandes Errante",
        descripcion_visible: "El terror de todo el Fondo pero a la vez con grandes historias",
        descripcion_real: "Un fantasma marinero que surca los mares",
        imagen: "https://example.com/holandes-errante.png", 
        características: { genero: "masculino", tipo: "fantasma", miedo: true },
        dialogos: {
            neutro: "Que es una cangreburger?",
            defensivo: "Imposible, mi mayor tesoso es mi precioso oro en mi barco",
            confundido: "Tengo que asustar a la gente no perder mi tiempo en una formula"
        }
    },
    {
        id: 15,
        nombre: "Bubble Bass",
        descripcion_visible: "Alguien presumido y exigente solo ve por si mismo",
        descripcion_real: "Un cliente exigente del Crustaceo Cascarudo",
        imagen: "https://example.com/bubble-bass.png", 
        características: { genero: "masculino", especie: "pez", exigente: true },
        dialogos: {
            neutro: "Si e oido de esa famosa formula pero tampoco es una delicia",
            defensivo: "Si crees que la robe solo porque odio la cangreburger estas en un error",
            confundido: "La critica es algo importante en los restaurantes talves para eso la utilizaran"
        }
    }
];

